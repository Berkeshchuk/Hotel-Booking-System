// =====================================================================
// --- Глобальні змінні для безпеки ---
// =====================================================================
let csrfToken = '';
let csrfHeader = '';

document.addEventListener("DOMContentLoaded", () => {
    initializeSecurity();
    initializeTabsAndModals();
    initializeUnitsCRUD();
    initializeWorkersCRUD();
    initializeSchedules();
    initializeGlobalEvents();
    initializeInfiniteScrolls();
    initializeCalendar();
    setTimeout(() => {
        if (typeof window.fetchAndDisplayServiceNames === 'function') {
            window.fetchAndDisplayServiceNames();
        }
    }, 100);
});

// =====================================================================
// --- 1. Ініціалізація безпеки (CSRF) ---
// =====================================================================
function initializeSecurity() {
    const csrfTokenMeta = document.querySelector('meta[name="_csrf"]');
    const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');

    if (!csrfTokenMeta || !csrfHeaderMeta) {
        console.error("CSRF tokens not found. Make sure meta tags are included in HTML.");
        return;
    }
    csrfToken = csrfTokenMeta.content;
    csrfHeader = csrfHeaderMeta.content;
}

// =====================================================================
// --- Загальні утиліти (доступні для всіх модулів) ---
// =====================================================================
function showToast(message, type = 'success') {
    const container = document.getElementById('toast-container');
    if (!container) return;
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `<span>${message}</span>`;
    container.appendChild(toast);
    setTimeout(() => toast.classList.add('show'), 10);
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

function setButtonLoading(button, isLoading) {
    if (isLoading) {
        button.disabled = true;
        button.classList.add('btn-loading');
    } else {
        button.disabled = false;
        button.classList.remove('btn-loading');
    }
}

async function sendJsonRequest(url, method, payload) {
    const options = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        }
    };
    if (payload) options.body = JSON.stringify(payload);
    const response = await fetch(url, options);
    if (!response.ok) {
        let errMsg = 'Помилка запиту';
        try { errMsg = await response.text(); } catch (e) { }
        throw new Error(errMsg);
    }
    return response;
}

// =====================================================================
// --- 2. Логіка вкладок (Tabs) та Модалок ---
// =====================================================================
function initializeTabsAndModals() {
    const tabBtns = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');

    tabBtns.forEach(btn => {
        btn.addEventListener('click', (e) => {
            tabBtns.forEach(b => b.classList.remove('active'));
            tabContents.forEach(c => c.classList.remove('active'));
            const targetId = e.currentTarget.getAttribute('data-target');
            e.currentTarget.classList.add('active');
            document.getElementById(targetId).classList.add('active');

            setTimeout(() => {
                window.dispatchEvent(new Event('resize'));
                window.dispatchEvent(new Event('scroll'));
            }, 50);

            // setTimeout(() => {
            //     if (typeof scrollObservers !== 'undefined') {
            //         scrollObservers.forEach(reconnect => reconnect && reconnect());
            //     }
            // }, 50);
        });
    });

    document.querySelectorAll('.modal-close-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.currentTarget.closest('.post-form-container').classList.add('hidden');
        });
    });
}

// =====================================================================
// --- 3. CRUD Приміщення (Units) та МНОЖИННИЙ Autocomplete ---
// =====================================================================
function initializeUnitsCRUD() {
    let selectedUnitServices = [];
    let unitDebounceTimer;
    let skipNextFocusFetch = false;

    const unitModal = document.getElementById('unit-modal');
    const unitServiceBox = document.getElementById('selected-unit-service-box');
    const unitSearchInput = document.getElementById('unit-service-search-input');
    const unitDropdown = document.getElementById('unit-service-dropdown');
    const hiddenUnitServiceInput = document.getElementById('unit-service-id');

    function renderSelectedUnitServices() {
        unitServiceBox.innerHTML = '';
        selectedUnitServices.forEach(service => {
            const chip = document.createElement('div');
            chip.className = 'association-chip';
            chip.innerHTML = `<span>#${service.id} ${service.displayName}</span> <button type="button" data-id="${service.id}" class="remove-unit-service">×</button>`;
            unitServiceBox.appendChild(chip);
        });

        // Записуємо всі вибрані ID через кому
        hiddenUnitServiceInput.value = selectedUnitServices.map(s => s.id).join(',');

        if (selectedUnitServices.length > 0) {
            unitSearchInput.placeholder = "Додати ще...";
        } else {
            unitSearchInput.placeholder = "Пошук за назвою або ID...";
        }
        unitSearchInput.style.display = 'block';
    }

    unitServiceBox?.addEventListener('click', (e) => {
        if (e.target.classList.contains('remove-unit-service')) {
            const idToRemove = parseInt(e.target.getAttribute('data-id'));
            selectedUnitServices = selectedUnitServices.filter(s => s.id !== idToRemove);
            renderSelectedUnitServices();
        }
    });

    document.querySelectorAll('input[name="unitCategory"]').forEach(radio => {
        radio.addEventListener('change', () => {
            selectedUnitServices = [];
            renderSelectedUnitServices();
        });
    });

    const handleUnitSearch = (e) => {

        if (e.type === 'focus' && skipNextFocusFetch) return;

        clearTimeout(unitDebounceTimer);
        const query = e.target.value.trim();
        const selectedCategory = document.querySelector('input[name="unitCategory"]:checked').value;

        unitDebounceTimer = setTimeout(async () => {
            try {
                const res = await fetch(`/api/service-units/search?q=${encodeURIComponent(query)}&category=${selectedCategory}`);
                if (!res.ok) throw new Error(`Помилка сервера: ${res.status}`);
                const data = await res.json();

                if (!Array.isArray(data)) throw new Error("Некоректний формат даних від сервера: " + data);

                const availableServices = data.filter(item => !selectedUnitServices.find(s => s.id === item.id));

                if (availableServices.length === 0) {
                    unitDropdown.innerHTML = '<div class="dropdown-item"><span class="dropdown-item-type">Нічого не знайдено</span></div>';
                } else {
                    unitDropdown.innerHTML = availableServices.map(item => {
                        const imgUrl = item.imageUrl ? item.imageUrl : '/common/images/placeholder.png';
                        const displayName = item.name || item.type;
                        return `
                        <div class="dropdown-item" data-id="${item.id}" data-name="${displayName}">
                            <img src="${imgUrl}" class="dropdown-item-img" alt="img">
                            <div class="dropdown-item-info">
                                <span class="dropdown-item-name">#${item.id} - ${displayName}</span>
                                <span class="dropdown-item-type">${item.type}</span>
                            </div>
                        </div>
                        `;
                    }).join('');
                }
                unitDropdown.classList.remove('hidden');
            } catch (err) {
                console.error("Помилка пошуку послуг", err);
            }
        }, 300);
    };

    unitSearchInput?.addEventListener('input', handleUnitSearch);
    unitSearchInput?.addEventListener('focus', handleUnitSearch);

    // Додавання послуги (Множинний вибір)
    unitDropdown?.addEventListener('click', (e) => {
        e.stopPropagation();
        const item = e.target.closest('.dropdown-item');
        if (item && item.hasAttribute('data-id')) {
            selectedUnitServices.push({
                id: parseInt(item.getAttribute('data-id')),
                displayName: item.getAttribute('data-name')
            });
            renderSelectedUnitServices();

            item.remove();

            if (unitDropdown.querySelectorAll('.dropdown-item').length === 0) {
                unitDropdown.classList.add('hidden');
                unitSearchInput.value = '';
            }

            // Вмикаємо прапорець ПЕРЕД фокусом
            skipNextFocusFetch = true;
            unitSearchInput.focus();

            // Вимикаємо його одразу після (щоб наступні реальні кліки працювали)
            setTimeout(() => skipNextFocusFetch = false, 50);
        }
    });

    document.getElementById('add-unit-btn')?.addEventListener('click', () => {
        document.getElementById('unit-form').reset();
        document.getElementById('unit-id').value = '';
        selectedUnitServices = [];
        renderSelectedUnitServices();
        document.getElementById('unit-modal-title').innerText = 'Додати приміщення';
        unitModal.classList.remove('hidden');
    });

    document.getElementById('units-section').addEventListener('click', async (e) => {
        const editBtn = e.target.closest('.unit-edit-btn');
        const deleteBtn = e.target.closest('.unit-delete-btn');
        const card = e.target.closest('.admin-card');

        if (!card) return;

        // --- ЛОГІКА РЕДАГУВАННЯ ---
        if (editBtn) {
            document.getElementById('unit-modal-title').innerText = 'Редагувати приміщення';
            document.getElementById('unit-id').value = card.dataset.id;
            document.getElementById('unit-premises').value = card.dataset.premises;
            document.getElementById('unit-capacity').value = card.dataset.capacity;
            document.getElementById('unit-cleaning').value = card.dataset.cleaning;
            document.getElementById('unit-out-of-service').checked = (card.dataset.outOfService === 'true');

            const rawIds = card.dataset.serviceId;
            selectedUnitServices = [];

            if (rawIds && rawIds.trim() !== "") {
                try {
                    const res = await fetch(`/api/service-units/short-by-ids?ids=${rawIds}`);
                    if (!res.ok) throw new Error();
                    const data = await res.json();

                    selectedUnitServices = data.map(item => ({
                        id: item.id,
                        displayName: item.name || item.type
                    }));

                    if (data.length > 0) {
                        const categoryRadio = document.querySelector(`input[name="unitCategory"][value="${data[0].category}"]`);
                        if (categoryRadio) categoryRadio.checked = true;
                    }
                } catch (err) {
                    console.error("Не вдалося завантажити імена послуг");
                }
            }
            renderSelectedUnitServices();
            unitModal.classList.remove('hidden');
        }

        // --- ЛОГІКА ВИДАЛЕННЯ (РАНІШЕ БУЛА ВІДСУТНЯ) ---
        if (deleteBtn) {
            const id = card.dataset.id;

            if (!confirm(`Увага! Ви впевнені, що хочете видалити це приміщення?`)) return;

            const originalText = deleteBtn.innerText;
            deleteBtn.innerText = "...";
            deleteBtn.disabled = true;

            try {
                await sendJsonRequest(`/api/physical-units/${id}`, 'DELETE');
                showToast("Приміщення успішно видалено");
                card.remove(); // Видаляємо картку
            } catch (error) {
                showToast(error.message, "error");
                deleteBtn.innerText = originalText;
                deleteBtn.disabled = false;
            }
        }
    });

    document.getElementById('unit-form')?.addEventListener('submit', async (e) => {
        e.preventDefault();
        const form = e.target;
        const submitBtn = form.querySelector('button[type="submit"]');

        const serviceIdsRaw = form.querySelector('#unit-service-id').value;
        if (!serviceIdsRaw) {
            showToast("Будь ласка, оберіть хоча б одну послугу з каталогу!", "warning");
            return;
        }

        const data = {
            id: form.querySelector('#unit-id').value || null,
            premisesNumber: form.querySelector('#unit-premises').value,
            // ВИПРАВЛЕНО: Відправляємо масив чисел
            serviceUnitIds: serviceIdsRaw.split(',').map(Number),
            clientCapacity: parseInt(form.querySelector('#unit-capacity').value),
            cleaningTimeInMinutes: parseInt(form.querySelector('#unit-cleaning').value),
            outOfService: form.querySelector('#unit-out-of-service').checked
        };

        const url = data.id ? `/api/physical-units/${data.id}` : '/api/physical-units';
        const method = data.id ? 'PUT' : 'POST';

        setButtonLoading(submitBtn, true);
        try {
            await sendJsonRequest(url, method, data);
            showToast("Приміщення успішно збережено!");

            if (method === 'PUT') {
                // Знаходимо картку приміщення в DOM
                const card = document.querySelector(`#units-section .admin-card[data-id="${data.id}"]`);
                if (card) {
                    // 1. Оновлюємо data-атрибути
                    card.dataset.premises = data.premisesNumber;
                    card.dataset.capacity = data.clientCapacity;
                    card.dataset.cleaning = data.cleaningTimeInMinutes;
                    card.dataset.outOfService = data.outOfService;
                    card.dataset.serviceId = data.serviceUnitIds.join(',');

                    // 2. Оновлюємо текстові значення в HTML
                    card.querySelector('.card-header-row h3 span').innerText = data.premisesNumber;

                    const statusDiv = card.querySelector('.card-status');
                    statusDiv.className = `card-status ${data.outOfService ? 'status-danger' : 'status-success'}`;
                    statusDiv.innerText = data.outOfService ? 'Не працює' : 'Активно';

                    const pTags = card.querySelectorAll('p');
                    pTags[0].querySelector('span').innerText = data.clientCapacity;
                    pTags[1].querySelector('span').innerText = data.cleaningTimeInMinutes;

                    // 3. Очищаємо блок з послугами і викликаємо вашу функцію для перемальовування міні-карток
                    const nameContainer = card.querySelector('.service-name-display');
                    if (nameContainer) nameContainer.innerHTML = '';
                    fetchAndDisplayServiceNames();
                }

                // Закриваємо модалку
                document.getElementById('unit-modal').classList.add('hidden');
            } else {
                // Якщо це POST (нове приміщення), перезавантажуємо сторінку
                setTimeout(() => window.location.reload(), 800);
            }
        } catch (error) {
            showToast(error.message, "error");
        } finally {
            setButtonLoading(submitBtn, false);
        }
    });
}

// =====================================================================
// --- 4. CRUD Персонал (Workers) та МНОЖИННИЙ Autocomplete ---
// =====================================================================
function initializeWorkersCRUD() {
    let selectedSkills = [];
    let debounceTimer;

    const workerModal = document.getElementById('worker-modal');
    const skillsBox = document.getElementById('selected-association-box');
    const searchInput = document.getElementById('skill-search-input');
    const dropdown = document.getElementById('skills-dropdown');
    const hiddenSkillsInput = document.getElementById('worker-skills-hidden');

    function renderSelectedSkills() {
        skillsBox.innerHTML = '';
        selectedSkills.forEach(skill => {
            const chip = document.createElement('div');
            chip.className = 'association-chip';
            chip.innerHTML = `<span>#${skill.id} ${skill.displayName}</span> <button type="button" data-id="${skill.id}">×</button>`;
            skillsBox.appendChild(chip);
        });
        hiddenSkillsInput.value = selectedSkills.map(s => s.id).join(',');
    }

    skillsBox?.addEventListener('click', (e) => {
        if (e.target.tagName === 'BUTTON') {
            const idToRemove = parseInt(e.target.getAttribute('data-id'));
            selectedSkills = selectedSkills.filter(s => s.id !== idToRemove);
            renderSelectedSkills();
        }
    });

    const handleSkillSearch = (e) => {
        clearTimeout(debounceTimer);
        const query = e.target.value.trim();

        debounceTimer = setTimeout(async () => {
            try {
                const res = await fetch(`/api/service-units/search?q=${encodeURIComponent(query)}&category=SPA`);
                if (!res.ok) throw new Error(`Помилка сервера: ${res.status}`);
                const data = await res.json();
                if (!Array.isArray(data)) throw new Error("Некоректний формат даних від сервера: " + data);

                const availableSkills = data.filter(item => !selectedSkills.find(s => s.id === item.id));

                if (availableSkills.length === 0) {
                    dropdown.innerHTML = '<div class="dropdown-item"><span class="dropdown-item-type">Нічого не знайдено</span></div>';
                } else {
                    dropdown.innerHTML = availableSkills.map(item => {
                        const imgUrl = item.imageUrl ? item.imageUrl : '/common/images/placeholder.png';
                        const displayName = item.name || item.type;
                        return `
                        <div class="dropdown-item" data-id="${item.id}" data-name="${displayName}">
                            <img src="${imgUrl}" class="dropdown-item-img" alt="img">
                            <div class="dropdown-item-info">
                                <span class="dropdown-item-name">#${item.id} - ${displayName}</span>
                                <span class="dropdown-item-type">${item.type}</span>
                            </div>
                        </div>
                        `;
                    }).join('');
                }
                dropdown.classList.remove('hidden');
            } catch (err) {
                console.error("Помилка пошуку послуг", err);
            }
        }, 300);
    };

    searchInput?.addEventListener('input', handleSkillSearch);
    searchInput?.addEventListener('focus', handleSkillSearch);

    dropdown?.addEventListener('click', (e) => {
        e.stopPropagation();
        const item = e.target.closest('.dropdown-item');
        if (item && item.hasAttribute('data-id')) {
            selectedSkills.push({
                id: parseInt(item.getAttribute('data-id')),
                displayName: item.getAttribute('data-name')
            });
            renderSelectedSkills();
            item.remove();

            if (dropdown.querySelectorAll('.dropdown-item').length === 0) {
                dropdown.classList.add('hidden');
                searchInput.value = '';
            }
            searchInput.focus();
        }
    });

    document.getElementById('add-worker-btn')?.addEventListener('click', () => {
        document.getElementById('worker-form').reset();
        document.getElementById('worker-id').value = '';
        selectedSkills = [];
        renderSelectedSkills();
        document.getElementById('worker-modal-title').innerText = 'Додати працівника';
        workerModal.classList.remove('hidden');
    });

    document.getElementById('workers-section').addEventListener('click', async (e) => {
        const editBtn = e.target.closest('.worker-edit-btn');
        const deleteBtn = e.target.closest('.worker-delete-btn');
        const card = e.target.closest('.admin-card');

        // ЛОГІКА РЕДАГУВАННЯ
        if (editBtn && card) {
            document.getElementById('worker-modal-title').innerText = 'Редагувати працівника';
            document.getElementById('worker-id').value = card.dataset.id;
            document.getElementById('worker-firstname').value = card.dataset.firstname;
            document.getElementById('worker-lastname').value = card.dataset.lastname;
            document.getElementById('worker-phone').value = card.dataset.phone;
            document.getElementById('worker-gender').value = card.dataset.gender;
            document.getElementById('worker-status').value = card.dataset.status;

            const rawIds = card.dataset.skills;
            if (rawIds && rawIds.trim() !== "") {
                try {
                    const res = await fetch(`/api/service-units/short-by-ids?ids=${rawIds}`);
                    if (!res.ok) throw new Error();
                    const data = await res.json();

                    selectedSkills = data.map(item => ({
                        id: item.id,
                        displayName: item.name || item.type
                    }));
                } catch (err) {
                    selectedSkills = [];
                    console.error("Не вдалося завантажити імена послуг");
                }
            } else {
                selectedSkills = [];
            }

            renderSelectedSkills();
            workerModal.classList.remove('hidden');
        }

        // ЛОГІКА ВИДАЛЕННЯ (ПРАЦІВНИКІВ)
        if (deleteBtn && card) {
            const id = card.dataset.id;

            if (!confirm(`Увага! Ви впевнені, що хочете БЕЗПОВОРОТНО видалити цього працівника? Всі пов'язані з ним розклади також можуть бути видалені. Цю дію неможливо скасувати.`)) return;

            const originalText = deleteBtn.innerText;
            deleteBtn.innerText = "...";
            deleteBtn.disabled = true;

            try {
                // ТУТ БУЛА ПОМИЛКА: Було /api/physical-units/ замість /api/spa-workers/
                await sendJsonRequest(`/api/spa-workers/${id}`, 'DELETE');
                showToast("Працівника успішно видалено");
                card.remove(); // Видаляємо картку
            } catch (error) {
                showToast(error.message, "error");
                deleteBtn.innerText = originalText;
                deleteBtn.disabled = false;
            }
        }
    });

    document.getElementById('worker-form')?.addEventListener('submit', async (e) => {
        e.preventDefault();
        const form = e.target;
        const submitBtn = form.querySelector('button[type="submit"]');

        const skillsRaw = document.getElementById('worker-skills-hidden').value;
        const competentSpaUnitIds = skillsRaw ? skillsRaw.split(',').map(Number) : [];

        if (competentSpaUnitIds.length === 0) {
            showToast("Оберіть хоча б одну компетенцію!", "warning");
            return;
        }

        const data = {
            id: form.querySelector('#worker-id').value || null,
            firstName: form.querySelector('#worker-firstname').value,
            lastName: form.querySelector('#worker-lastname').value,
            workPhoneNumber: form.querySelector('#worker-phone').value,
            gender: form.querySelector('#worker-gender').value,
            status: form.querySelector('#worker-status').value,
            competentSpaUnitIds: competentSpaUnitIds
        };

        const url = data.id ? `/api/spa-workers/${data.id}` : '/api/spa-workers';
        const method = data.id ? 'PUT' : 'POST';

        setButtonLoading(submitBtn, true);
        try {
            await sendJsonRequest(url, method, data);
            showToast("Працівника збережено!");

            if (method === 'PUT') {
                // Знаходимо картку працівника в DOM
                const card = document.querySelector(`#workers-section .admin-card[data-id="${data.id}"]`);
                if (card) {
                    // 1. Оновлюємо data-атрибути
                    card.dataset.firstname = data.firstName;
                    card.dataset.lastname = data.lastName;
                    card.dataset.phone = data.workPhoneNumber;
                    card.dataset.gender = data.gender;
                    card.dataset.status = data.status;
                    card.dataset.skills = data.competentSpaUnitIds.join(',');

                    // 2. Оновлюємо текстові значення
                    card.querySelector('.card-header-row h3').innerText = `${data.firstName} ${data.lastName}`;

                    const statusDiv = card.querySelector('.card-status');
                    statusDiv.className = `card-status ${data.status === 'ACTIVE' ? 'status-success' : 'status-warning'}`;
                    statusDiv.innerText = data.status;

                    const pTags = card.querySelectorAll('p');
                    pTags[0].querySelector('span').innerText = data.gender === 'MALE' ? 'Чоловік' : 'Жінка';
                    pTags[1].querySelector('span').innerText = data.workPhoneNumber;

                    // 3. Перемальовуємо послуги (навички)
                    const nameContainer = card.querySelector('.service-name-display');
                    if (nameContainer) nameContainer.innerHTML = '';
                    fetchAndDisplayServiceNames();
                }

                // Закриваємо модалку
                document.getElementById('worker-modal').classList.add('hidden');
            } else {
                // Для нового працівника перезавантажуємо
                setTimeout(() => window.location.reload(), 800);
            }
        } catch (error) {
            showToast(error.message, "error");
        } finally {
            setButtonLoading(submitBtn, false);
        }
    });
}

// =====================================================================
// --- 5. Управління розкладами ---
// =====================================================================
function initializeSchedules() {
    const scheduleModal = document.getElementById('schedule-modal');
    const breaksContainer = document.getElementById('breaks-container');
    const addBreakBtn = document.getElementById('add-break-btn');
    const scheduleForm = document.getElementById('schedule-form');
    const schedulesList = document.getElementById('existing-schedules-list');

    function formatTime(timeString) {
        if (!timeString) return '';
        // Оскільки тепер приходить чистий час (напр. "13:00:00" або "13:00"), 
        // просто беремо перші 5 символів
        return timeString.substring(0, 5);
    }

    async function loadSchedules(workerId) {
        schedulesList.innerHTML = '<p style="color:var(--text-muted); font-size:14px;">Завантаження...</p>';
        try {
            const res = await fetch(`/api/spa-workers/${workerId}/schedules`);
            if (!res.ok) throw new Error("Не вдалося завантажити розклад");
            const schedules = await res.json();

            if (schedules.length === 0) {
                schedulesList.innerHTML = '<p style="color:var(--text-muted); font-size:14px;">Немає запланованих змін</p>';
                return;
            }

            // 1. Визначаємо правильний порядок днів тижня
            const dayOrder = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];

            const daysMap = {
                'MONDAY': 'Понеділок', 'TUESDAY': 'Вівторок', 'WEDNESDAY': 'Середа',
                'THURSDAY': 'Четвер', 'FRIDAY': 'П\'ятниця', 'SATURDAY': 'Субота', 'SUNDAY': 'Неділя'
            };

            // 2. Сортуємо отриманий масив перед рендерингом
            const sortedSchedules = schedules.sort((a, b) => {
                return dayOrder.indexOf(a.dayOfWeek) - dayOrder.indexOf(b.dayOfWeek);
            });

            // 3. Рендеримо вже відсортований список
            schedulesList.innerHTML = sortedSchedules.map(sch => {
                const breaksHtml = sch.breaks && sch.breaks.length > 0
                    ? `<div class="schedule-breaks">Перерви: ${sch.breaks.map(b => `${formatTime(b.breakStart)} - ${formatTime(b.breakEnd)}`).join(', ')}</div>`
                    : '<div class="schedule-breaks">Без перерв</div>';

                const startStr = formatTime(sch.startTime);
                const endStr = formatTime(sch.endTime);

                return `
                <div class="schedule-card">
                    <div class="schedule-info">
                        <strong>Кожен ${daysMap[sch.dayOfWeek] || sch.dayOfWeek}</strong>
                        <span>⌚ ${startStr} - ${endStr}</span>
                        ${breaksHtml}
                    </div>
                    <button type="button" class="btn-delete-schedule" data-id="${sch.id}">Видалити</button>
                </div>
            `;
            }).join('');

            // Додавання обробників на кнопки видалення (залишається без змін)
            schedulesList.querySelectorAll('.btn-delete-schedule').forEach(btn => {
                btn.addEventListener('click', async (e) => {
                    if (!confirm("Видалити цю зміну?")) return;
                    const schId = e.target.getAttribute('data-id');
                    const deleteBtn = e.target;
                    deleteBtn.disabled = true;
                    deleteBtn.innerText = '...';

                    try {
                        await sendJsonRequest(`/api/spa-workers/schedules/${schId}`, 'DELETE');
                        showToast("Зміну видалено");
                        await loadSchedules(workerId);
                    } catch (error) {
                        showToast(error.message, "error");
                        deleteBtn.disabled = false;
                        deleteBtn.innerText = 'Видалити';
                    }
                });
            });
        } catch (error) {
            schedulesList.innerHTML = '<p style="color:var(--danger-text); font-size:14px;">Помилка завантаження</p>';
        }
    }

    document.getElementById('workers-section').addEventListener('click', async (e) => {
        const scheduleBtn = e.target.closest('.worker-schedule-btn');
        if (!scheduleBtn) return;

        const card = scheduleBtn.closest('.admin-card');
        if (!card) return;

        const workerId = card.dataset.id;
        const firstName = card.dataset.firstname;
        const lastName = card.dataset.lastname;

        document.getElementById('schedule-worker-id').value = workerId;
        document.getElementById('schedule-modal-worker-name').innerText = `${firstName} ${lastName}`;

        scheduleForm.reset();
        breaksContainer.innerHTML = '';

        await loadSchedules(workerId);
        scheduleModal.classList.remove('hidden');
    });

    addBreakBtn?.addEventListener('click', () => {
        const breakRow = document.createElement('div');
        breakRow.className = 'break-row';
        breakRow.innerHTML = `
            <input type="time" class="break-start" required placeholder="Початок">
            <input type="time" class="break-end" required placeholder="Кінець">
            <button type="button" class="btn-remove-break" title="Видалити перерву">×</button>
        `;
        breakRow.querySelector('.btn-remove-break').addEventListener('click', () => breakRow.remove());
        breaksContainer.appendChild(breakRow);
    });

    scheduleForm?.addEventListener('submit', async (e) => {
        e.preventDefault();
        const submitBtn = e.target.querySelector('button[type="submit"]');

        const workerId = document.getElementById('schedule-worker-id').value;

        // ЗМІНЕНО: Беремо значення дня тижня замість дати
        const dayOfWeekVal = document.getElementById('schedule-day-of-week').value;

        const startVal = document.getElementById('schedule-start').value;
        const endVal = document.getElementById('schedule-end').value;

        if (startVal >= endVal) {
            showToast("Час закінчення зміни має бути пізніше за час початку!", "error");
            return;
        }

        let breaksValid = true;
        const breaks = Array.from(document.querySelectorAll('.break-row')).map(row => {
            const bStart = row.querySelector('.break-start').value;
            const bEnd = row.querySelector('.break-end').value;

            if (bStart >= bEnd) {
                showToast("Час закінчення перерви має бути пізніше за її початок!", "error");
                breaksValid = false;
            }
            if (bStart < startVal || bEnd > endVal) {
                showToast("Перерва повинна бути в межах робочої зміни!", "error");
                breaksValid = false;
            }
            return {
                // ЗМІНЕНО: Відправляємо тільки час (формат HH:mm:ss для LocalTime)
                breakStart: `${bStart}:00`,
                breakEnd: `${bEnd}:00`
            };
        });

        if (!breaksValid) return;

        const payload = {
            // ЗМІНЕНО: dayOfWeek замість date
            dayOfWeek: dayOfWeekVal,
            // ЗМІНЕНО: Відправляємо тільки час
            startTime: `${startVal}:00`,
            endTime: `${endVal}:00`,
            breaks: breaks
        };

        setButtonLoading(submitBtn, true);
        try {
            await sendJsonRequest(`/api/spa-workers/${workerId}/schedules`, 'POST', payload);
            showToast("Зміну успішно збережено!");
            scheduleForm.reset();
            document.getElementById('breaks-container').innerHTML = '';
            await loadSchedules(workerId);
        } catch (error) {
            showToast(error.message, "error");
        } finally {
            setButtonLoading(submitBtn, false);
        }
    });
}

// =====================================================================
// --- 6. Глобальні події (закриття dropdown-ів) ---
// =====================================================================
function initializeGlobalEvents() {
    document.addEventListener('click', (e) => {
        const unitDropdown = document.getElementById('unit-service-dropdown');
        if (unitDropdown && !unitDropdown.classList.contains('hidden')) {
            if (!e.target.closest('#unit-service-container')) {
                unitDropdown.classList.add('hidden');
            }
        }

        const skillsDropdown = document.getElementById('skills-dropdown');
        if (skillsDropdown && !skillsDropdown.classList.contains('hidden')) {
            const workerContainer = skillsDropdown.closest('.autocomplete-container');
            if (workerContainer && !workerContainer.contains(e.target)) {
                skillsDropdown.classList.add('hidden');
            }
        }
    });
}

// const scrollObservers = [];
function initializeInfiniteScrolls() {
    // 1. Скролл для приміщень
    setupInfiniteScroll({
        apiUrl: '/api/physical-units',
        gridSelector: '#units-section .inventory-grid',
        statusSelector: '#units-scroll-status',
        renderFunction: createUnitCard
    });

    // 2. Скролл для працівників
    setupInfiniteScroll({
        apiUrl: '/api/spa-workers',
        gridSelector: '#workers-section .inventory-grid',
        statusSelector: '#workers-scroll-status',
        renderFunction: createWorkerCard
    });
}

function setupInfiniteScroll(config) {
    const gridContainer = document.querySelector(config.gridSelector);
    const statusContainer = document.querySelector(config.statusSelector);
    if (!gridContainer || !statusContainer) return;

    const loader = statusContainer.querySelector('.scroll-loader');
    const endBlock = statusContainer.querySelector('.scroll-end');

    let currentPage = 1; // Починаємо з 1, бо 0 вже відрендерено через Thymeleaf
    const pageSize = 4; // Відповідає вашому @PageableDefault(size = 20)
    let isLoading = false;
    let isLastPage = false;

    const setScrollState = (state) => {
        if (loader) loader.classList.add("hidden");
        if (endBlock) endBlock.classList.add("hidden");

        if (state === "loading" && loader) loader.classList.remove("hidden");
        if (state === "end" && endBlock) endBlock.classList.remove("hidden");
    };

    const fetchMoreData = async () => {
        if (isLoading || isLastPage) return;

        isLoading = true;
        setScrollState("loading");

        try {
            const response = await fetch(`${config.apiUrl}?page=${currentPage}&size=${pageSize}`);
            if (!response.ok) throw new Error("Помилка сервера");

            const data = await response.json();

            if (!Array.isArray(data) || data.length === 0) {
                isLastPage = true;
                setScrollState("end");
                return;
            }

            // Додаємо картки в DOM
            data.forEach(item => {
                gridContainer.insertAdjacentHTML('beforeend', config.renderFunction(item));
            });

            // Викликаємо перемалювання міні-карток послуг для нових елементів
            if (typeof fetchAndDisplayServiceNames === 'function') {
                fetchAndDisplayServiceNames();
            }

            // Якщо прийшло менше, ніж розмір сторінки - це кінець
            if (data.length < pageSize) {
                isLastPage = true;
                setScrollState("end");
            } else {
                setScrollState("none");
                currentPage++;
            }

        } catch (error) {
            console.error("Помилка пагінації", error);
            setScrollState("none");
        } finally {
            isLoading = false;
        }
    };

    const observer = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && !isLoading && !isLastPage) {
            fetchMoreData();
        }
    }, { rootMargin: "200px", threshold: 0 });

    observer.observe(statusContainer);
    // return () => {
    //     observer.disconnect();
    //     observer.observe(statusContainer);
    // };
}

// --- ФУНКЦІЇ ГЕНЕРАЦІЇ HTML КАРТОК ---

function createUnitCard(unit) {
    const serviceIds = unit.serviceUnitIds ? unit.serviceUnitIds.join(',') : '';
    const statusClass = unit.outOfService ? 'status-danger' : 'status-success';
    const statusText = unit.outOfService ? 'Не працює' : 'Активно';

    return `
        <div class="card admin-card" data-id="${unit.id}" data-premises="${unit.premisesNumber || ''}" data-service-id="${serviceIds}" data-capacity="${unit.clientCapacity}" data-cleaning="${unit.cleaningTimeInMinutes}" data-out-of-service="${unit.outOfService}">
            <div class="admin-card-content">
                <div class="card-header-row">
                    <h3>Номер: <span>${unit.premisesNumber || 'N/A'}</span></h3>
                    <div class="card-status ${statusClass}">${statusText}</div>
                </div>
                <div>
                    <strong>Послуги:</strong>
                    <div class="service-name-display" style="margin-top: 4px;">ID: <span>${serviceIds}</span></div>
                </div>
                <p><strong>Місткість:</strong> <span>${unit.clientCapacity}</span> осіб</p>
                <p><strong>Час прибирання:</strong> <span>${unit.cleaningTimeInMinutes}</span> хв</p>
            </div>
            <div class="modal-buttons-container">
                <button class="edit-modal-opener unit-edit-btn">Редагувати</button>
                <button class="delete-modal-opener unit-delete-btn">Видалити</button>
            </div>
        </div>
    `;
}

function createWorkerCard(worker) {
    const skills = worker.competentSpaUnitIds ? worker.competentSpaUnitIds.join(',') : '';
    const statusClass = worker.status === 'ACTIVE' ? 'status-success' : 'status-warning';
    const genderText = worker.gender === 'MALE' ? 'Чоловік' : 'Жінка';

    return `
        <div class="card admin-card" data-id="${worker.id}" data-firstname="${worker.firstName}" data-lastname="${worker.lastName}" data-phone="${worker.workPhoneNumber}" data-gender="${worker.gender}" data-status="${worker.status}" data-skills="${skills}">
            <div class="admin-card-content">
                <div class="card-header-row">
                    <h3>${worker.firstName} ${worker.lastName}</h3>
                    <div class="card-status ${statusClass}">${worker.status}</div>
                </div>
                <p><strong>Стать:</strong> <span>${genderText}</span></p>
                <p><strong>Телефон:</strong> <span>${worker.workPhoneNumber || ''}</span></p>
                <div class="mt-8">
                    <p class="mb-8"><strong>Навички (ID послуг):</strong></p>
                    <div class="service-name-display">ID: <span>${skills}</span></div>
                </div>
            </div>
            <div class="modal-buttons-container">
                <button class="schedule-modal-opener worker-schedule-btn">Розклад</button>
                <button class="edit-modal-opener worker-edit-btn">Редаг.</button>
                <button class="delete-modal-opener worker-delete-btn">Звільнити</button>
            </div>
        </div>
    `;
}

// =====================================================================
// --- ГЛОБАЛЬНА ФУНКЦІЯ: ВІДОБРАЖЕННЯ МІНІ-БЛОКІВ ПОСЛУГ ---
// =====================================================================
window.fetchAndDisplayServiceNames = async function () {
    const allCards = document.querySelectorAll('.admin-card');
    const serviceIdsToFetch = new Set();
    const cardsToProcess = [];

    // 1. Збираємо ТІЛЬКИ ті картки, які ще НЕ оброблені
    // (Шукаємо ті, де ще немає '.service-mini-card' всередині)
    allCards.forEach(card => {
        const nameContainer = card.querySelector('.service-name-display');

        if (nameContainer && !nameContainer.querySelector('.service-mini-card')) {
            cardsToProcess.push(card); // Зберігаємо картку для обробки

            const rawIds = card.dataset.serviceId || card.dataset.skills;
            if (rawIds && rawIds.trim() !== '') {
                rawIds.split(',').forEach(id => serviceIdsToFetch.add(id.trim()));
            }
        }
    });

    // Якщо нових карток немає або в них немає послуг - виходимо
    if (cardsToProcess.length === 0 || serviceIdsToFetch.size === 0) return;

    try {
        const res = await fetch(`/api/service-units/short-by-ids?ids=${Array.from(serviceIdsToFetch).join(',')}`);
        if (!res.ok) throw new Error("Сервер відповів помилкою");

        const data = await res.json();
        const idToDataMap = {};
        data.forEach(item => { idToDataMap[item.id] = item; });

        // 2. Рендеримо тільки відібрані НОВІ картки
        cardsToProcess.forEach(card => {
            const rawIds = card.dataset.serviceId || card.dataset.skills;
            const nameContainer = card.querySelector('.service-name-display');

            if (rawIds && rawIds.trim() !== '' && nameContainer) {
                const idsArray = rawIds.split(',').map(id => id.trim());
                let finalHtml = '';

                idsArray.forEach(sId => {
                    if (idToDataMap[sId]) {
                        const svc = idToDataMap[sId];
                        const imgUrl = svc.imageUrl ? svc.imageUrl : '/common/images/placeholder.png';
                        const displayName = svc.name || svc.type || "Невідомо";

                        finalHtml += `
                            <div class="service-mini-card">
                                <img src="${imgUrl}" class="service-mini-img" alt="img">
                                <div class="service-mini-info">
                                    <span class="service-mini-title">#${svc.id} - ${displayName}</span>
                                    <span class="service-mini-type">${svc.type}</span>
                                </div>
                            </div>
                        `;
                    } else {
                        finalHtml += `<div style="color:var(--danger-text); font-weight:bold; margin-top:8px;">ID: ${sId} (Видалено)</div>`;
                    }
                });

                nameContainer.innerHTML = finalHtml;

                // Кнопка "Ще", якщо послуг більше 3
                const maxVisible = 3;
                if (idsArray.length > maxVisible) {
                    const btn = document.createElement('button');
                    btn.className = 'show-more-btn';
                    btn.type = 'button';
                    btn.innerText = `Ще (+${idsArray.length - maxVisible})`;

                    btn.addEventListener('click', () => {
                        nameContainer.classList.toggle('expanded');
                        if (nameContainer.classList.contains('expanded')) {
                            btn.innerText = 'Сховати';
                        } else {
                            btn.innerText = `Ще (+${idsArray.length - maxVisible})`;
                        }
                    });
                    nameContainer.appendChild(btn);
                }
            }
        });
    } catch (e) {
        console.error("Помилка підвантаження назв послуг", e);
    }
};




























// =====================================================================
// --- ГРАФІК ЗАЙНЯТОСТІ (VIS.JS) - SCROLL EDITION ---
// =====================================================================


let timeline = null;
let timelineData = null;
let timelineGroups = null;
let loadedDates = new Set();
let scrollTimeout = null;
let isCalendarBuilt = false;
let activeCalendarRequests = 0;

function initializeCalendar() {
    // Замість того, щоб малювати графік одразу, ми вішаємо слухач на клік по вкладці
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

        timelineGroups.add({ id: 'g_rooms', content: '<b style="color:var(--primary)">Приміщення</b>', nestedGroups: [] });
        units.forEach(u => {
            timelineGroups.add({ id: `room_${u.id}`, content: `Кімната ${u.premisesNumber}` });
            const g = timelineGroups.get('g_rooms');
            g.nestedGroups.push(`room_${u.id}`);
            timelineGroups.update(g);
        });

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

        // !!! ЗМІНЮЄМО ОЦЕЙ РЯДОК !!!
        // Раніше там могло бути 31 день, тепер ставимо жорстко 24 ГІДИ
        zoomMax: 1000 * 60 * 60 * 24,       // Максимальне охоплення: 24 години

        // Якщо 24 години здасться замало, можете поставити 2 дні:
        // zoomMax: 1000 * 60 * 60 * 24 * 2,

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
    toggleCalendarLoader(true); // Показуємо лоадер
    const startIso = new Date(startDate.getTime() - 12 * 3600000).toISOString();
    const endIso = new Date(endDate.getTime() + 12 * 3600000).toISOString();

    try {
        const res = await fetch(`/api/allocations/calendar?start=${startIso}&end=${endIso}`);
        if (!res.ok) throw new Error();
        const allocations = await res.json();

        const newItems = [];
        allocations.forEach(alloc => {
            const baseId = `alloc_${alloc.id}`;
            let className = 'vis-item-active';
            if (alloc.status === 'COMPLETED') className = 'vis-item-completed';
            if (alloc.status === 'CANCELLED') className = 'vis-item-cancelled';

            // Формуємо HTML-посилання. 
            // ЗАМІНІТЬ URL ('/admin/services/...', '/admin/bookings/...') на ті, які реально існують або будуть існувати у вашій системі!
            const serviceLink = `<a href="/admin/services/${alloc.serviceUnitId}" target="_blank" class="timeline-link" title="Відкрити послугу">Послуга #${alloc.serviceUnitId}</a>`;
            const bookingLink = `<a href="/admin/bookings/${alloc.generalBookingId}" target="_blank" class="timeline-link" title="Відкрити деталі бронювання">Зам. #${alloc.generalBookingId}</a>`;

            // Базовий контент для кімнати
            const roomContent = `<div class="timeline-item-content">
                                    ${serviceLink} <br/> 
                                    ${bookingLink}
                                 </div>`;

            // 1. Додаємо в кімнату
            if (alloc.physicalServiceUnitId) {
                const roomId = `${baseId}_room`;
                if (!timelineData.get(roomId)) {
                    newItems.push({
                        id: roomId,
                        group: `room_${alloc.physicalServiceUnitId}`,
                        start: alloc.start,
                        end: alloc.end,
                        content: roomContent,
                        className: className
                    });
                }
            }

            // 2. Додаємо працівникам
            if (alloc.assignedWorkerIds && alloc.assignedWorkerIds.length > 0) {
                alloc.assignedWorkerIds.forEach(wId => {
                    const workerId = `${baseId}_worker_${wId}`;
                    if (!timelineData.get(workerId)) {

                        // Для працівника додаємо ще й лінк на його профіль
                        const workerLink = `<a href="/admin/workers/${wId}" target="_blank" class="timeline-link" title="Відкрити профіль майстра">Майстер #${wId}</a>`;

                        const workerContent = `<div class="timeline-item-content">
                                                  ${serviceLink} <br/>
                                                  ${workerLink} <br/>
                                                  ${bookingLink}
                                               </div>`;

                        newItems.push({
                            id: workerId,
                            group: `worker_${wId}`,
                            start: alloc.start,
                            end: alloc.end,
                            content: workerContent,
                            className: className
                        });
                    }
                });
            }
        });

        if (newItems.length > 0) timelineData.add(newItems);
    } catch (e) {
        console.error("Помилка підвантаження алокацій:", e);
    } finally {
        toggleCalendarLoader(false); // Ховаємо лоадер
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

    // Група-заголовок для Кімнат
    timelineGroups.add({ id: 'g_rooms', content: '<b style="color:var(--primary)">Приміщення</b>', nestedGroups: [] });
    document.querySelectorAll('#units-section .admin-card').forEach(card => {
        const id = card.dataset.id;
        timelineGroups.add({ id: `room_${id}`, content: `Кімната ${card.dataset.premises}` });
        const g = timelineGroups.get('g_rooms');
        g.nestedGroups.push(`room_${id}`);
        timelineGroups.update(g);
    });

    // Група-заголовок для Працівників
    timelineGroups.add({ id: 'g_workers', content: '<b style="color:var(--primary)">Майстри</b>', nestedGroups: [] });
    document.querySelectorAll('#workers-section .admin-card').forEach(card => {
        const id = card.dataset.id;
        timelineGroups.add({ id: `worker_${id}`, content: `${card.dataset.firstname} ${card.dataset.lastname}` });
        const g = timelineGroups.get('g_workers');
        g.nestedGroups.push(`worker_${id}`);
        timelineGroups.update(g);
    });
}
