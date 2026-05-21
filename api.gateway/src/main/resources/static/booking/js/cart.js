"use strict";

document.addEventListener("DOMContentLoaded", () => {
    initializeCardInteractions();
});



// 2. ЛОГІКА КАРТОК (ROOMS & SPAS)
function initializeCardInteractions() {

    // Робимо глобальною, щоб сайдбар міг її викликати після видалення елемента
    window.syncCardsUI = function () {
        let cart = JSON.parse(localStorage.getItem('edemium_cart')) || [];

        const counts = {};
        cart.forEach(item => {
            counts[item.serviceUnitId] = (counts[item.serviceUnitId] || 0) + 1;
        });

        // Клас .card використовується і для кімнат, і для спа 
        // Шукаємо за загальним класом .card, який є в обох фрагментах
        document.querySelectorAll('.card').forEach(card => {
            // Беремо ID залежно від того, чи це кімната, чи спа
            const rawId = card.dataset.serviceId;
            if (!rawId) return; // Якщо це якась інша картка, пропускаємо

            const serviceId = parseInt(rawId);
            const count = counts[serviceId] || 0;

            // Шукаємо кнопки (зверни увагу: я додав .btn-select-room та .btn-select-spa, бо у твоїх HTML вони називаються так)
            const selectBtn = card.querySelector('.btn-select, .btn-select-spa');
            const controlsBlock = card.querySelector('.service-quantity-controls');
            const qtySpan = card.querySelector('.service-qty-value');

            if (count > 0) {
                card.classList.add('selected-in-cart');
                if (selectBtn) selectBtn.classList.add('hidden');
                if (controlsBlock) controlsBlock.classList.remove('hidden');
                if (qtySpan) qtySpan.textContent = count;
            } else {
                card.classList.remove('selected-in-cart');
                if (selectBtn) selectBtn.classList.remove('hidden');
                if (controlsBlock) controlsBlock.classList.add('hidden');
            }
        });

        if (typeof window.updateGlobalCartBadge === 'function') {
            window.updateGlobalCartBadge();
        }
    };

    function addToCart(btn) {
        const classType = btn.getAttribute('data-class-type') + "_BOOKING";
        
        const newItem = {
            id: Date.now(),
            serviceUnitId: parseInt(btn.dataset.serviceId),
            serviceName: btn.dataset.serviceType,
            price: parseFloat(btn.dataset.servicePrice),
            maxCapacity: parseInt(btn.dataset.serviceCapacity),
            classType: classType, // 'SPA' або 'ROOM'
            duration: parseInt(btn.getAttribute('data-duration')) || 0, // Тільки для SPA
            start: '',
            end: '',
            clientCount: 1,
            preferedGender: 'ANY' // Дефолтне значення для SPA
        };

        let cart = JSON.parse(localStorage.getItem('edemium_cart')) || [];
        cart.push(newItem);
        localStorage.setItem('edemium_cart', JSON.stringify(cart));

        window.syncCardsUI();

        if (typeof window.triggerGlobalCartNudge === 'function') {
            window.triggerGlobalCartNudge();
        }
    }

    function removeFromCart(serviceId) {
        let cart = JSON.parse(localStorage.getItem('edemium_cart')) || [];
        // Видаляємо останній знайдений об'єкт із таким ID
        const indexToRemove = cart.map(item => item.serviceUnitId).lastIndexOf(parseInt(serviceId));

        if (indexToRemove !== -1) {
            cart.splice(indexToRemove, 1);
            localStorage.setItem('edemium_cart', JSON.stringify(cart));

            window.syncCardsUI();

            if (typeof window.triggerGlobalCartNudge === 'function') {
                window.triggerGlobalCartNudge();
            }
        }
    }

    // Глобальне делегування подій для кнопок на картках
    document.body.addEventListener('click', (e) => {
        // Додавання (через кнопку "Обрати" або "+")
        if (e.target.classList.contains('btn-select') || e.target.classList.contains('btn-qty-plus')) {
            addToCart(e.target);
        }

        // Видалення (через "-")
        if (e.target.classList.contains('btn-qty-minus')) {
            removeFromCart(e.target.dataset.serviceId);
        }
    });

    // Початкова синхронізація при завантаженні сторінки
    window.syncCardsUI();
}