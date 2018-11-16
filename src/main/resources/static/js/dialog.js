// todo - clean this (use jquery, or make onclick function like createDialog(), exclude unused arguments (e))
document.getElementById("clearButton").onclick = function (e) {
    document.getElementById("greeting").value = "";
}


let userId;
let username;

$('#exampleModal').on('show.bs.modal', function (event) {
    userId = $(event.relatedTarget).data('user-id');
    username = $(event.relatedTarget).data('username');
});

$(function () {
    $("#greetingsForm").on('submit', function (e) {
        e.preventDefault()
    })
})


function createDialog() {
    $.ajax({
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        url: "/ajax/new_dialog/" + userId,
        type: "POST",
        data: JSON.stringify($("#greeting").val())
    }).done(function () {
        window.location.href = "/chatter?p="+username
    })


}