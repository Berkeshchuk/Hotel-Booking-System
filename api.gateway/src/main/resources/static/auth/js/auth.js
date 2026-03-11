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
                headers: {
                    [tokenHeader]: token,
                    "Content-Type": "application/x-www-form-urlencoded"
                }
            });

            if (res.ok) {
                let data = {};
                if (res.status !== 204 && res.headers.get("content-length") !== "0") {
                    const text = await res.text();
                    data = text ? JSON.parse(text) : {};
                }
                onSuccess(data);
            } else {
                const errorData = await res.json().catch(() => ({}));
                errorSpan.textContent = errorData.error || "Помилка сервера: " + res.status;
            }
        } catch (error) {
            console.error(error);
            errorSpan.textContent = "Помилка з'єднання або обробки даних";
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
