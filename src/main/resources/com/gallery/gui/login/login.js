const toggleCheckbox = document.getElementById('toggle-checkbox');
const flipCardInner = document.getElementById('flip-card');

// Gestione della visualizzazione del form (login/signup)
toggleCheckbox.addEventListener('change', () => {
    if (toggleCheckbox.checked) {
        flipCardInner.classList.add('toggle-active');
    } else {
        flipCardInner.classList.remove('toggle-active');
    }
});

function handleLogin(event) {
    event.preventDefault(); // Impedisce l'invio del form
    var email = document.getElementById("email");
    var password = document.getElementById("password");
    if (typeof ControllerLogin !== "undefined") {
        ControllerLogin.handleLoginButtonClick(email.value, password.value);
    } else {
        console.log("Controllore JavaFX non trovato");
    }
}

function handleSignUp(event) {
    event.preventDefault();
    var name = document.getElementById("signup-name");
    var email = document.getElementById("signup-email");
    var password = document.getElementById("signup-password");
    if (typeof ControllerLogin !== "undefined") {
        ControllerLogin.handleSignUpButtonClick(name.value, email.value, password.value);
    } else {
        console.log("Controllore JavaFX non trovato");
    }
}