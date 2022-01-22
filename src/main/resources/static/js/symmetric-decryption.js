let decryption_form = document.getElementById('decryption_form');
let encrypted_input_field = document.getElementById('encrypted_input_field');
let encrypted_input = document.getElementById('encrypted_input');
let input_error = document.getElementById('input_error');
let key = document.getElementById('key');
let key_error = document.getElementById('key_error');
let submit_button = document.getElementById('submit_button');

input_error.style.display = 'none';
key_error.style.display = 'none';

const key_regex = /^([A-Za-z0-9+\/]{4})*([A-Za-z0-9+\/]{3}=|[A-Za-z0-9+\/]{2}==)?$/;

function validateInput() {
    if (key_regex.test(encrypted_input.value)) {
        submit_button.disabled = false;
        input_error.style.display = 'none';
        encrypted_input_field.className = 'field';
    } else {
        submit_button.disabled = true;
        input_error.style.display = 'block';
        encrypted_input_field.className = 'field error';
    }
}

function validateKey() {
    if (key_regex.test(key.value)) {
        submit_button.disabled = false;
        key_error.style.display = 'none';
        key_field.className = 'field';
    } else {
        submit_button.disabled = true;
        key_error.style.display = 'block';
        key_field.className = 'field error';
    }
}

function clearFields() {
    decryption_form.reset();
}