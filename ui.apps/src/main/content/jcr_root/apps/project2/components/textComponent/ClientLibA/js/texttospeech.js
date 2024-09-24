document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("convert-btn").addEventListener("click", function () {
        const text = document.getElementById("text-input").value;
        fetch('/bin/textToSpeech', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ text: text })
        })
        .then(response => response.json())
        .then(data => {
            const audio = document.getElementById("audio-output");
            audio.src = data.audio_url; // Use the correct key from the API response
            audio.play();
        })
        .catch(error => console.error('Error:', error));
    });
});