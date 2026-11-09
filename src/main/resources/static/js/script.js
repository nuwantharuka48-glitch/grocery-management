document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("orderForm");
    const checkboxes = document.querySelectorAll(".item-checkbox");
    const liveTotalDisplay = document.getElementById("liveTotalDisplay");

    // Dynamic Live Total Calculation
    function calculateLiveTotal() {
        let total = 0.0;

        checkboxes.forEach((cb) => {
            const card = cb.closest(".product-card");
            const qtyInput = card.querySelector(".qty-input");

            if (cb.checked) {
                qtyInput.disabled = false;
                const price = parseFloat(cb.getAttribute("data-price"));
                const qty = parseInt(qtyInput.value) || 1;
                total += price * qty;
            } else {
                qtyInput.disabled = true;
            }
        });

        if (liveTotalDisplay) {
            liveTotalDisplay.textContent = "$" + total.toFixed(2);
        }
    }

    checkboxes.forEach((cb) => {
        cb.addEventListener("change", calculateLiveTotal);
        const card = cb.closest(".product-card");
        const qtyInput = card.querySelector(".qty-input");
        qtyInput.addEventListener("input", calculateLiveTotal);
    });

    // Form Client-side Validation
    if (form) {
        form.addEventListener("submit", function (event) {
            let isValid = true;

            const nameInput = document.getElementById("customerName");
            const nameError = document.getElementById("nameError");
            if (nameInput.value.trim().length < 3) {
                nameError.style.display = "block";
                isValid = false;
            } else {
                nameError.style.display = "none";
            }

            const emailInput = document.getElementById("customerEmail");
            const emailError = document.getElementById("emailError");
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(emailInput.value.trim())) {
                emailError.style.display = "block";
                isValid = false;
            } else {
                emailError.style.display = "none";
            }

            const addressInput = document.getElementById("address");
            const addressError = document.getElementById("addressError");
            if (addressInput.value.trim().length < 5) {
                addressError.style.display = "block";
                isValid = false;
            } else {
                addressError.style.display = "none";
            }

            const checkedItems = document.querySelectorAll('.item-checkbox:checked');
            const itemError = document.getElementById("itemError");
            if (checkedItems.length === 0) {
                itemError.style.display = "block";
                isValid = false;
            } else {
                itemError.style.display = "none";
            }

            if (!isValid) {
                event.preventDefault();
            }
        });
    }
});