let decryption_form = document.getElementById('decryption_form');
let encrypted_text_field = document.getElementById('encrypted_text_field');
let encrypted_text = document.getElementById('encrypted_text');
let input_error = document.getElementById('input_error');
let private_key = document.getElementById('private_key');
let private_key_field = document.getElementById('private_key_field');
let private_key_error = document.getElementById('private_key_error');
let submit_button = document.getElementById('submit_button');

input_error.style.display = 'none';
private_key_error.style.display = 'none';

function validateInput() {
    const key_regex = /^([A-Za-z0-9+\/]{4})*([A-Za-z0-9+\/]{3}=|[A-Za-z0-9+\/]{2}==)?$/;

    if (key_regex.test(encrypted_text.value)) {
        submit_button.disabled = false;
        input_error.style.display = 'none';
        encrypted_text_field.className = 'field';
    } else {
        submit_button.disabled = true;
        input_error.style.display = 'block';
        encrypted_text_field.className = 'field error';
    }
}

function validatePrivateKey() {
    if (private_key.value.startsWith("-----BEGIN PRIVATE KEY-----\n")
        && private_key.value.endsWith("\n-----END PRIVATE KEY-----")) {
        submit_button.disabled = false;
        private_key_error.style.display = 'none';
        private_key_field.className = 'field';
    } else {
        submit_button.disabled = true;
        private_key_error.style.display = 'block';
        private_key_field.className = 'field error';
    }
}

function clearFields() {
    decryption_form.reset();
}