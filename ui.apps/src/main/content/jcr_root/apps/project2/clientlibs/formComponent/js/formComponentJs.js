// Function to handle form submission (Create/Update)
function submitContactForm() {
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const mobile = document.getElementById('mobile').value;
    const action = document.getElementById('formAction').value;

    const xhr = new XMLHttpRequest();
    xhr.open(action === 'create' ? "POST" : "PUT", "/bin/example/contactdetails", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            alert(xhr.responseText);
            location.reload(); // Reload the page to see the updated contact list
        }
    };
    xhr.send(`name=${encodeURIComponent(name)}&email=${encodeURIComponent(email)}&mobile=${encodeURIComponent(mobile)}`);
}

// Function to handle the delete button click
function deleteContact(name) {
    if (confirm("Are you sure you want to delete this contact?")) {
        const xhr = new XMLHttpRequest();
        xhr.open("DELETE", `/bin/example/contactdetails?name=${encodeURIComponent(name)}`, true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                alert(xhr.responseText);
                location.reload(); // Reload the page to see the updated contact list
            }
        };
        xhr.send();
    }
}

// Function to handle the edit button click
function editContact(name, email, mobile) {
    document.getElementById('name').value = name;
    document.getElementById('email').value = email;
    document.getElementById('mobile').value = mobile;
    document.getElementById('formAction').value = 'update';
}
