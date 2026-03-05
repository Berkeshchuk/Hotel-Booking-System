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
                },
                credentials: "include"
            }).then(responce =>{
                if(responce.redirected){
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


