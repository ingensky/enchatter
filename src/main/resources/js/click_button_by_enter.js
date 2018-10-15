document.getElementById("message")
    .addEventListener("keyup", function(event) {
        event.preventDefault();
        if (event.keyCode === 13) {
            document.getElementById("send_button").click();
        }
    });

function clearText(){
    document.getElementById("message").innerText = '';
    document.getElementById("message").innerHTML = '';
}
//https://stackoverflow.com/questions/155188/trigger-a-button-click-with-javascript-on-the-enter-key-in-a-text-box