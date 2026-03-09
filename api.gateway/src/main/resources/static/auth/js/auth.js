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



function handleAuthSubmit(formId, endpoint, onSuccess) {
    const form = document.getElementById(formId);
    if (!form) return;

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const errorSpan = form.querySelector(".error-span");
        errorSpan.textContent = "";

        const tokenHeader = document.querySelector('meta[name="_csrf_header"]').content;
        const token = document.querySelector('meta[name="_csrf"]').content;

        const urlEncodedData = new URLSearchParams(new FormData(form));

        try {
            const res = await fetch(endpoint, {
                method: "POST",
                body: urlEncodedData,
                headers: { [tokenHeader]: token }
            });

            const data = await res.json();

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

handleAuthSubmit("sign-in-form", "/sign_in", (data) => {
    window.location.href = data.redirect;
});

handleAuthSubmit("sign-up-form", "/sign_up", () => {
    signUpFormContainer.classList.remove('active');
    setTimeout(() => signInFormContainer.classList.add('active'), 100);
});
