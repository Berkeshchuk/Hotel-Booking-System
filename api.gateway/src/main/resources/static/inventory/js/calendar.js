"use strict";

// =====================================================================
// --- ГРАФІК ЗАЙНЯТОСТІ (VIS.JS) ---
// Примітка: цей скрипт використовує глобальні змінні csrfToken та csrfHeader,
// які ініціалізуються у файлі resources-and-staff-management.js
// =====================================================================

let timeline = null;
let timelineData = null;
let timelineGroups = null;
let loadedDates = new Set();
let scrollTimeout = null;
let isCalendarBuilt = false;
let activeCalendarRequests = 0;

document.addEventListener("DOMContentLoaded", () => {
    initializeCalendar();
});

function initializeCalendar() {
    // Вішаємо слухач на клік по вкладці календаря
    const calendarTabBtn = document.querySelector('[data-target="calendar-section"]');
    if (!calendarTabBtn) return;

    calendarTabBtn.addEventListener('click', () => {
        // Даємо браузеру 50мс, щоб змінити display: none на display: block
        setTimeout(() => {
            if (!isCalendarBuilt) {
                buildCalendar(); // Будуємо вперше
                isCalendarBuilt = true;
            } else {
                if (timeline) timeline.redraw(); // Просто перемальовуємо, якщо вже побудовано
            }
        }, 50);
    });
}

async function fetchAllGroupsForTimeline() {
    toggleCalendarLoader(true); // Показуємо лоадер
    try {
        const [unitsRes, workersRes] = await Promise.all([
            fetch('/api/physical-units?page=0&size=1000'),
            fetch('/api/spa-workers?page=0&size=1000')
        ]);

        const units = await unitsRes.json();
        const workers = await workersRes.json();

        timelineGroups.clear();

        // Групи для кімнат
        timelineGroups.add({ id: 'g_rooms', content: '<b style="color:var(--primary)">Приміщення</b>', nestedGroups: [] });
        units.forEach(u => {
            timelineGroups.add({ id: `room_${u.id}`, content: `Кімната ${u.premisesNumber}` });
            const g = timelineGroups.get('g_rooms');
            g.nestedGroups.push(`room_${u.id}`);
            timelineGroups.update(g);
        });

        // Групи для працівників
        timelineGroups.add({ id: 'g_workers', content: '<b style="color:var(--primary)">Працівники</b>', nestedGroups: [] });
        workers.forEach(w => {
            timelineGroups.add({ id: `worker_${w.id}`, content: `${w.firstName} ${w.lastName}` });
            const g = timelineGroups.get('g_workers');
            g.nestedGroups.push(`worker_${w.id}`);
            timelineGroups.update(g);
        });

    } catch (e) {
        console.error("Не вдалося завантажити групи для календаря", e);
    } finally {
        toggleCalendarLoader(false); // Ховаємо лоадер
    }
}

async function buildCalendar() {
    const container = document.getElementById('visualization');
    if (!container) return;

    if (!timelineData) timelineData = new vis.DataSet();
    if (!timelineGroups) timelineGroups = new vis.DataSet();

    await fetchAllGroupsForTimeline();

    const now = new Date();

    // ВСТАНОВЛЮЄМО МЕЖІ: 2 роки назад (730 днів), 1 рік у майбутнє
    const minLimitDate = new Date(now.getTime() - 730 * 24 * 60 * 60 * 1000);
    const maxLimitDate = new Date(now.getTime() + 365 * 24 * 60 * 60 * 1000);

    const options = {
        stack: false,
        horizontalScroll: true,
        zoomKey: 'ctrlKey',
        orientation: 'top',

        start: new Date(now.getTime() - 3 * 60 * 60 * 1000),
        end: new Date(now.getTime() + 12 * 60 * 60 * 1000),

        // --- ОБМЕЖЕННЯ ---
        min: minLimitDate, // 2 роки тому
        max: maxLimitDate, // 1 рік вперед

        zoomMin: 1000 * 60 * 60 * 2,       // 2 години (мінімальне наближення, ок)
        zoomMax: 1000 * 60 * 60 * 24,      // Максимальне охоплення: 24 години

        timeAxis: { scale: 'hour', step: 1 },
        showCurrentTime: true,
        format: {
            minorLabels: { minute: 'HH:mm', hour: 'HH:mm' },
            majorLabels: { day: 'DD MMMM (dddd)', week: 'Week w', month: 'MMMM YYYY' }
        }
    };

    timeline = new vis.Timeline(container, timelineData, timelineGroups, options);

    fetchAllocationsForRange(options.start, options.end);

    timeline.on('rangechanged', (properties) => {
        if (scrollTimeout) clearTimeout(scrollTimeout);
        scrollTimeout = setTimeout(() => {
            fetchAllocationsForRange(properties.start, properties.end);
        }, 300);
    });
}

async function fetchAllocationsForRange(startDate, endDate) {
    toggleCalendarLoader(true); 
    
    // Використовуємо наш безпечний форматер замість toISOString()
    const startIso = toLocalISOString(new Date(startDate.getTime() - 12 * 3600000));
    const endIso = toLocalISOString(new Date(endDate.getTime() + 12 * 3600000));

    try {
        const res = await fetch(`/api/allocations/calendar?start=${startIso}&end=${endIso}`, {
            headers: { [csrfHeader]: csrfToken }
        });
        
        if (!res.ok) throw new Error();
        const allocations = await res.json();

        timelineData.clear();

        const newItems = [];
        const updateItems = [];

        allocations.forEach(alloc => {
            const baseId = `alloc_${alloc.id}`;

            if (alloc.status === 'CANCELLED' || alloc.status === 'REJECTED') {
                // Якщо скасоване бронювання раніше було намальоване в календарі - примусово стираємо його
                if (alloc.physicalServiceUnitId && timelineData.get(`${baseId}_room`)) {
                    timelineData.remove(`${baseId}_room`);
                }
                if (alloc.assignedWorkerIds) {
                    alloc.assignedWorkerIds.forEach(wId => {
                        if (timelineData.get(`${baseId}_worker_${wId}`)) timelineData.remove(`${baseId}_worker_${wId}`);
                    });
                }
                return;
            }

            // Визначаємо CSS-клас для активних та завершених
            let className = 'vis-item-active';
            if (alloc.status === 'COMPLETED') className = 'vis-item-completed';

            const serviceLink = `<a href="/admin/services/${alloc.serviceUnitId}" target="_blank" class="timeline-link" title="Відкрити послугу">Послуга #${alloc.serviceUnitId}</a>`;
            const bookingLink = `<a href="/admin/bookings/${alloc.generalBookingId}" target="_blank" class="timeline-link" title="Відкрити деталі бронювання">Зам. #${alloc.generalBookingId}</a>`;

            const roomContent = `<div class="timeline-item-content">
                                    ${serviceLink} <br/> 
                                    ${bookingLink}
                                 </div>`;

            // 1. Алокація для кімнати
            if (alloc.physicalServiceUnitId) {
                const roomId = `${baseId}_room`;
                const itemConfig = {
                    id: roomId,
                    group: `room_${alloc.physicalServiceUnitId}`,
                    start: alloc.start,
                    end: alloc.end,
                    content: roomContent,
                    className: className
                };

                // Якщо ще немає в календарі - додаємо, якщо є - оновлюємо (наприклад, змінився час або колір)
                if (!timelineData.get(roomId)) newItems.push(itemConfig);
                else updateItems.push(itemConfig);
            }

            // 2. Алокація для працівників
            if (alloc.assignedWorkerIds && alloc.assignedWorkerIds.length > 0) {
                alloc.assignedWorkerIds.forEach(wId => {
                    const workerId = `${baseId}_worker_${wId}`;
                    const workerLink = `<a href="/admin/workers/${wId}" target="_blank" class="timeline-link" title="Відкрити профіль майстра">Майстер #${wId}</a>`;

                    const workerContent = `<div class="timeline-item-content">
                                              ${serviceLink} <br/>
                                              ${workerLink} <br/>
                                              ${bookingLink}
                                           </div>`;

                    const itemConfig = {
                        id: workerId,
                        group: `worker_${wId}`,
                        start: alloc.start,
                        end: alloc.end,
                        content: workerContent,
                        className: className
                    };

                    if (!timelineData.get(workerId)) newItems.push(itemConfig);
                    else updateItems.push(itemConfig);
                });
            }
        });

        // Пакетне додавання та оновлення (працює дуже швидко)
        if (newItems.length > 0) timelineData.add(newItems);
        if (updateItems.length > 0) timelineData.update(updateItems);

    } catch (e) {
        console.error("Помилка підвантаження алокацій:", e);
    } finally {
        toggleCalendarLoader(false);
    }
}

function toggleCalendarLoader(show) {
    const spinner = document.getElementById('calendar-loading-spinner');
    if (!spinner) return;

    if (show) {
        activeCalendarRequests++;
        spinner.classList.remove('hidden');
    } else {
        activeCalendarRequests--;
        if (activeCalendarRequests <= 0) {
            activeCalendarRequests = 0;
            spinner.classList.add('hidden');
        }
    }
}

function extractGroupsFromDOM() {
    timelineGroups.clear();

    timelineGroups.add({ id: 'g_rooms', content: '<b style="color:var(--primary)">Приміщення</b>', nestedGroups: [] });
    document.querySelectorAll('#units-section .admin-card').forEach(card => {
        const id = card.dataset.id;
        timelineGroups.add({ id: `room_${id}`, content: `Кімната ${card.dataset.premises}` });
        const g = timelineGroups.get('g_rooms');
        g.nestedGroups.push(`room_${id}`);
        timelineGroups.update(g);
    });

    timelineGroups.add({ id: 'g_workers', content: '<b style="color:var(--primary)">Майстри</b>', nestedGroups: [] });
    document.querySelectorAll('#workers-section .admin-card').forEach(card => {
        const id = card.dataset.id;
        timelineGroups.add({ id: `worker_${id}`, content: `${card.dataset.firstname} ${card.dataset.lastname}` });
        const g = timelineGroups.get('g_workers');
        g.nestedGroups.push(`worker_${id}`);
        timelineGroups.update(g);
    });
}

function toLocalISOString(date) {
    const pad = n => n < 10 ? '0' + n : n;
    return date.getFullYear() + '-' +
           pad(date.getMonth() + 1) + '-' +
           pad(date.getDate()) + 'T' +
           pad(date.getHours()) + ':' +
           pad(date.getMinutes()) + ':' +
           pad(date.getSeconds());
}