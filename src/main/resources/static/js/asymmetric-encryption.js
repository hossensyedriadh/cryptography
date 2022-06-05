let encryption_form = document.getElementById('encryption_form');
let method = document.getElementById('method');
let encrypted_text = document.getElementById('encrypted_text');
let existing_public_key = document.getElementById('existing_public_key');
let existing_public_key_field = document.getElementById('existing_public_key_field');
let keys = document.getElementById('keys');
let new_public_key = document.getElementById('new_public_key');
let new_private_key = document.getElementById('new_private_key');
let keysize = document.getElementById('keysize');
let key_size_field = document.getElementById('key_size_field');
let notice = document.getElementById('notice');
let submit_button = document.getElementById('submit_button');
let public_key_error = document.getElementById('public_key_error');

public_key_error.style.display = 'none';

if (method.value === "USE_EXISTING_KEY") {
    existing();
} else if (method.value === "GENERATE_NEW_KEY") {
    gen_new();
}

function toggle() {
    if (method.value === "USE_EXISTING_KEY") {
        existing();
        encrypted_text.value = null;
    } else if (method.value === "GENERATE_NEW_KEY") {
        gen_new();
        encrypted_text.value = null;
    }
}

function existing() {
    keys.style.display = "none";
    key_size_field.style.display = 'none';
    existing_public_key_field.style.display = 'block';
    existing_public_key.required = true;
    keysize.required = false;
    notice.style.display = "none";
    new_public_key.value = null;
    new_private_key.value = null;
}

function gen_new() {
    keys.style.display = 'flex';
    key_size_field.style.display = 'block';
    existing_public_key_field.style.display = 'none';
    existing_public_key.required = false;
    keysize.required = true;
    notice.style.display = "block";
    existing_public_key.value = null;
}

function validatePublicKey() {
    if (existing_public_key.value.startsWith("-----BEGIN PUBLIC KEY-----\n")
        && existing_public_key.value.endsWith("\n-----END PUBLIC KEY-----")) {
        submit_button.disabled = false;
        public_key_error.style.display = 'none';
        existing_public_key_field.className = 'field';
    } else {
        submit_button.disabled = true;
        public_key_error.style.display = 'block';
        existing_public_key_field.className = 'field error';
    }
}

function clearFields() {
    encryption_form.reset();
}