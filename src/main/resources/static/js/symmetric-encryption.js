let encryption_form = document.getElementById('encryption_form');
let method = document.getElementById('method');
let key_field = document.getElementById('key_field');
let key = document.getElementById('key');
let generate_new_key_field = document.getElementById('generate_new_key_field');
let key_alg = document.getElementById('key_alg');
let generated_key = document.getElementById('generated_key');

function toggle() {
    if (method.value === 'USE_EXISTING_KEY') {
        key.required = true;
        key_field.style.display = 'block';
        generate_new_key_field.style.display = 'none';
        key_alg.required = false;
        generated_key.value = null;
    } else {
        key.required = false;
        key_field.style.display = 'none';
        generate_new_key_field.style.display = 'block';
        key_alg.required = true;
        key.value = null;
    }
}

toggle();

function clearFields() {
    encryption_form.reset();
}