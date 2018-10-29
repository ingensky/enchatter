let stompClient = null


function connect() {
    const socket = new SockJS('/enchatter-websocket')
    stompClient = Stomp.over(socket)
    stompClient.connect({}, function (frame) {
        console.log('Connected' + frame)
        stompClient.subscribe('/topic/activity/' + dialogId, function (message) {
            // debugger;
            showGreeting(JSON.parse(message.body))
        })
    })
}


function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect()
    }
    console.log("disconnected")

}

const messagesBlock = $("#messagesBlock");


function showGreeting(message) {
    // TODO -  all changes do here, u must append cards to card-group instead of adding rows to the table (as it now)

    $("#temporaryIdForMessages").append("<tr>" +
        "<td>" + message.id + "</td>" +
        "<td>" + message.author.username + "</td>" +
        "<td>" + message.text + "</td>" +
        "<td>" + message.creationTime + "</td>" +
        "</tr>")

    messagesBlock.scrollTop(messagesBlock[0].scrollHeight);
}

$(function () {
    $("#formInputMessageText").on('submit', function (e) {
        e.preventDefault()
    })
})

let noty

function closeNoty() {
    if (noty) {
        noty.close()
        noty = undefined
    }
}

function sendMessage(dialogId) {
    closeNoty()
    let messageInput = $("#inputMessageText")

    let textLengthError = messageInput.val().length < 2;
    let errorText = stompClient === null ? "You must be on dialog page" :
        textLengthError ? "Your text should be longer then 2 letters minimum" : undefined
    if (errorText) {
        noty = new Noty({
            text: errorText,
            theme: "bootstrap-v4",
            timeout: 2000,
            progressBar: false,
            type: "error",
            layout: "bottomCenter"
        }).show()
    } else {
        stompClient.send("/app/dialog/" + dialogId, {}, JSON.stringify(messageInput.val()))
        messageInput.val('')
    }

}