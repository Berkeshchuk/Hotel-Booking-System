"use strict"

document.addEventListener("DOMContentLoaded", () => {
    initializeLogoutEvent()
    initializeHandleScrolling()
    initializeBookingPageLink()
})

function initializeLogoutEvent() {
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
}

// Скрипт для ховання хедера при скролі
function initializeHandleScrolling() {
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

function initializeBookingPageLink() {
    const authStatusElement = document.getElementById('authorized');
    const bookingPageLink = document.getElementById('booking-page-link');
    const bookingNoteContainer = document.querySelector(".booking-note-container");

    if (authStatusElement) {
        const isAuthenticated = authStatusElement.getAttribute('data-auth') === 'true';

        bookingPageLink.addEventListener('click', (event) => {
            if (!isAuthenticated) {
                event.preventDefault();
                document.getElementById('login-overlay').classList.remove("hidden");
            }
        });

        bookingNoteContainer.addEventListener("click", (event) => {
            if (!isAuthenticated) {
                document.getElementById('login-overlay').classList.remove("hidden");
            } else {
                const redirectUrl = event.target.getAttribute("data-href");
                window.location.href = redirectUrl;
            }
        });
    }
}








