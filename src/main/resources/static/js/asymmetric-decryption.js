let decryption_form = document.getElementById('decryption_form');
let private_key = document.getElementById('private_key');
let private_key_field = document.getElementById('private_key_field');
let private_key_error = document.getElementById('private_key_error');
let submit_button = document.getElementById('submit_button');

private_key_error.style.display = 'none';

function validatePrivateKey() {
    if (private_key.value.startsWith("-----BEGIN RSA PRIVATE KEY-----\n")
        && private_key.value.endsWith("\n-----END RSA PRIVATE KEY-----\n")) {
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