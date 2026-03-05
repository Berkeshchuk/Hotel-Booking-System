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


const signInForm = document.getElementById("sign-in-form")

signInForm.addEventListener("submit", async (event) => {
    event.preventDefault()

    const errorSpan = signInForm.querySelector(".error-span");
    errorSpan.textContent = ""
    const tokenHeader = document.querySelector('meta[name="_csrf"]').content;
    const token = document.querySelector('meta[name="_csrf_header"]').content;

    const formData = new FormData(signInForm)

    const res = await fetch("/sign_in", {
        method: "POST",
        body: formData,
        headers: {
            [tokenHeader]: token
        }
    })

    if (res.ok) {
        const data = await res.json()
        window.location.href = data.redirect
        // alert(data.redirect)
    } else {
        const data = await res.json()
        errorSpan.textContent = data.error
    }
})

const signUpForm = document.getElementById("sign-up-form")
signUpForm.addEventListener("submit", async (event) => {
    event.preventDefault()

    const errorSpan = signUpForm.querySelector(".error-span")
    errorSpan.textContent = ""
    const tokenHeader = document.querySelector('meta[name="_csrf_header"]').content
    const token = document.querySelector('meta[name="_csrf"]').content

    const formData = new FormData(signUpForm)



    const res = await fetch("/sign_up", {
        method: "POST",
        body: formData,
        headers: {
            [tokenHeader]: token
        }
    })

    if (res.ok) {
        signUpFormContainer.classList.remove('active');
        setTimeout(() => signInFormContainer.classList.add('active'), 100);
    } else {
        const data = await res.json()
        errorSpan.textContent = data.error
    }
})
