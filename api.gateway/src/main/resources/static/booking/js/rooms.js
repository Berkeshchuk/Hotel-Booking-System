"use strict"

document.addEventListener("DOMContentLoaded", () => {
    initializeFacilityInputEvents()
    initializeOpenCloseButtonsEvents()
    initializePostFormEvent()
})

function initializeFacilityInputEvents() {
    const formFacilityContainer = document.querySelector(".form-facility-container")

    if (formFacilityContainer) {
        let draggedEl = null;
        let placeholder = document.createElement("div");
        placeholder.className = "facility-placeholder";

        const createFacilityInput = (value = "") => {
            const div = document.createElement("div");
            div.className = "facility-item";
            div.draggable = true;
            div.innerHTML = `<input type="text" name="facility" placeholder="Напр., 'Wi-Fi'" value="${value}">`;

            div.addEventListener("mouseenter", () => div.classList.add("hover"));
            div.addEventListener("mouseleave", () => div.classList.remove("hover"));

            div.addEventListener("dragstart", e => {
                draggedEl = div;
                div.classList.add("dragging");
                e.dataTransfer.effectAllowed = "move";
                setTimeout(() => div.style.display = "none", 0);
            });

            div.addEventListener("dragend", () => {
                div.style.display = "flex";
                div.classList.remove("dragging");
                placeholder.remove();
                draggedEl = null;
            });

            div.querySelector("input").addEventListener("input", manageInputs);
            return div;
        };

        formFacilityContainer.addEventListener("dragover", e => {
            e.preventDefault();
            const afterElement = getDragAfterElement(formFacilityContainer, e.clientY);
            if (afterElement == null) {
                formFacilityContainer.append(placeholder);
            } else {
                formFacilityContainer.insertBefore(placeholder, afterElement);
            }
        });

        formFacilityContainer.addEventListener("drop", e => {
            e.preventDefault();
            if (draggedEl) {
                formFacilityContainer.insertBefore(draggedEl, placeholder);
            }
        });

        function getDragAfterElement(formFacilityContainer, y) {
            const draggableElements = [...formFacilityContainer.querySelectorAll(".facility-item:not(.dragging)")];
            return draggableElements.reduce((closest, child) => {
                const box = child.getBoundingClientRect();
                const offset = y - box.top - box.height / 2;
                if (offset < 0 && offset > closest.offset) {
                    return { offset: offset, element: child };
                } else {
                    return closest;
                }
            }, { offset: Number.NEGATIVE_INFINITY }).element;
        }

        function manageInputs() {
            const inputs = [...formFacilityContainer.querySelectorAll("input[name='facility']")];
            const empty = inputs.filter(inp => inp.value.trim() === "");

            if (empty.length === 0) {
                formFacilityContainer.append(createFacilityInput());
            }

            if (empty.length > 1) {
                for (let i = 1; i < empty.length; i++) {
                    if (formFacilityContainer.children.length > 1) empty[i].parentElement.remove();
                }
            }
        }
        formFacilityContainer.append(createFacilityInput());
    }
}



function initializeOpenCloseButtonsEvents() {
    const postFormContainer = document.querySelector(".post-form-container")
    const postFormOpener = document.getElementById("post-form-opener")
    const postFormCloser = document.getElementById("post-form-closer")

    postFormOpener.addEventListener("click", () => {
        postFormContainer.classList.remove("hidden")
    })

    postFormCloser.addEventListener("click", () => {
        postFormContainer.classList.add("hidden")
    })
}

function initializePostFormEvent() {
    const tokenHeader = document.querySelector("meta[name='_csrf_header']").content
    const token = document.querySelector("meta[name='_csrf']").content

    const postForm = document.getElementById("post-form")
    postForm.addEventListener("submit", async (event) => {
        event.preventDefault()

        const facilities = [...postForm.querySelectorAll("input[name='facility']")]
            .map(input => input.value.trim())
            .filter(facility => facility.length > 0)
        const roomDto = {
            price: parseFloat(postForm.querySelector("input[name='price']").value),
            description: postForm.querySelector("textarea[name='description']").value,
            type: postForm.querySelector("input[name='type']").value,
            guestCapacity: postForm.querySelector("input[name='guestCapacity']").value,
            facilities: facilities,
            classType: "ROOM"
        }

        const postImages = postForm.querySelector("input[name='post-images']").files

        const formData = new FormData()


        formData.append("room", new Blob(
            [JSON.stringify(roomDto)],
            { type: 'application/json' }
        ))

        for (let postImage of postImages) {
            formData.append("imageFiles", postImage)
        }

        let res = await fetch(postForm.action, {
            method: "POST",
            body: formData,
            headers: {
                [tokenHeader]: token
            }
        })

        const data = await res.json()
    })





}

/*
     * <script>
     * async function submitRoom() {
     * // 1. Беремо файли
     * const files = document.getElementById('images').files;
     * 
     * // 2. Будуємо DTO
     * const roomDto = {
     * description: document.getElementById('description').value,
     * price: parseFloat(document.getElementById('price').value),
     * // додаткові поля DTO за потребою
     * };
     * 
     * // 3. Створюємо FormData
     * const formData = new FormData();
     * 
     * // додаємо DTO як JSON
     * formData.append('room', new Blob([JSON.stringify(roomDto)], { type:
     * 'application/json' }));
     * 
     * // додаємо файли
     * for (let i = 0; i < files.length; i++) {
     * formData.append('imageFiles', files[i]);
     * }
     * 
     * // 4. Відправляємо fetch
     * const response = await fetch('http://localhost:8085/rooms', {
     * method: 'POST',
     * body: formData,
     * });
     * 
     * const data = await response.json();
     * console.log(data);
     * }
     * </script>
     */









