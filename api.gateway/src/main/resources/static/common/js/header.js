"use strict"

document.addEventListener("DOMContentLoaded", () => {
    const imageProfile = document.querySelector(".image-profile")
    const profileOptions = document.querySelector(".profile-options")
    const logoutLink = document.getElementById("logout-link")

    if (logoutLink) {
        logoutLink.addEventListener("click", (event) => {
            event.preventDefault()

            const tokenHeader = document.querySelector("meta[name='_csrf_header']").content
            const token = document.querySelector("meta[name='_csrf']").content

            fetch(logoutLink.href, {
                method: "POST",
                headers: {
                    [tokenHeader]: token
                }
            }).then(responce => {
                if (responce.redirected) {
                    window.location.href = responce.url
                }
            })
        })
    }

    if (imageProfile && profileOptions) {
        imageProfile.addEventListener("click", () => {
            profileOptions.classList.toggle("hidden")
        })
    }

})

// Скрипт для ховання хедера при скролі
function handleScrolling() {
    let lastScroll = 0;
    const header = document.querySelector("header");

    window.addEventListener("scroll", () => {
        const currentScroll = window.scrollY;

        if (currentScroll > lastScroll) {
            header.classList.add("header-hidden");
        } else {
            header.classList.remove("header-hidden");
        }

        lastScroll = currentScroll;
    });
}


handleScrolling()


