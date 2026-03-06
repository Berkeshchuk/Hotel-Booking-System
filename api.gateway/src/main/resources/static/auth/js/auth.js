"use strict"

const signInFormContainer = document.querySelector('.sign-in-form-container');
const signUpFormContainer = document.querySelector('.sign-up-form-container');

document.querySelector('.registration-link').addEventListener('click', () => {
    signInFormContainer.classList.remove('active');
    setTimeout(() => signUpFormContainer.classList.add('active'), 100);
});

document.querySelector('.login-link').addEventListener('click', () => {
    signUpFormContainer.classList.remove('active');
    setTimeout(() => signInFormContainer.classList.add('active'), 100);
});


// 1. Створюємо універсальну функцію для обох форм
function handleAuthSubmit(formId, endpoint, onSuccess) {
    const form = document.getElementById(formId);
    if (!form) return;

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        // Знаходимо і чистимо поле з помилкою
        const errorSpan = form.querySelector(".error-span");
        errorSpan.textContent = "";

        // Отримуємо CSRF токени
        const tokenHeader = document.querySelector('meta[name="_csrf_header"]').content;
        const token = document.querySelector('meta[name="_csrf"]').content;

        // ВАЖЛИВО: Правильно конвертуємо форму в x-www-form-urlencoded
        const urlEncodedData = new URLSearchParams(new FormData(form));

        try {
            // Відправляємо універсальний запит
            const res = await fetch(endpoint, {
                method: "POST",
                body: urlEncodedData,
                headers: { [tokenHeader]: token }
            });

            const data = await res.json();

            // Викликаємо унікальну логіку (callback) або показуємо помилку
            if (res.ok) {
                onSuccess(data);
            } else {
                errorSpan.textContent = data.error || "Невідома помилка";
            }
        } catch (error) {
            errorSpan.textContent = "Помилка з'єднання з сервером";
        }
    });
}

// 2. Застосовуємо функцію для форми ВХОДУ
handleAuthSubmit("sign-in-form", "/sign_in", (data) => {
    window.location.href = data.redirect;
});

// 3. Застосовуємо функцію для форми РЕЄСТРАЦІЇ
handleAuthSubmit("sign-up-form", "/sign_up", () => {
    // Припускаємо, що ці змінні (контейнери) оголошені вище у вашому файлі
    signUpFormContainer.classList.remove('active');
    setTimeout(() => signInFormContainer.classList.add('active'), 100);
});
