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



    if(!message.author.username.equals(auth)) {
        $("#temporaryIdForMessages").append
        (
            "<div class=\"card-group d-flex flex-column align-items-end\">" +
                "<div class=\"card my-1 d-inline-flex mw-100 border-secondary shadow\" >" +
                    "<div class=\"card-header d-inline-flex align-items-baseline p-1\">" +
                        "<a class=\"text-primary font-size-20\">" + message.author.username + "</a>" +
                        "<a class=\"text-secondary ml-3 font-size-12\">" + message.creationTime + "</a>" +
                    "</div>" +
                    "<div class=\"card-body d-inline-flex mw-100 mx-2 my-1 p-0\">" +
                        "<a>" + message.text + "</a>" +
                    "</div>" +
                "</div>" +
            "</div>"
        );
    }
    else {
        $("#temporaryIdForMessages").append
        (
            "<div class=\"card-group d-flex flex-column align-items-start\">" +
                "<div class=\"card my-1 d-inline-flex mw-100 border-secondary shadow\">" +
                    "<div class=\"card-header d-inline-flex align-items-baseline p-1\">" +
                        "<a class=\"text-primary font-size-20\">" + message.author.username + "</a>" +
                        "<a class=\"text-secondary ml-3 font-size-12\">" + message.creationTime + "</a>" +
                    "</div>" +
                    "<div class=\"card-body d-inline-flex mw-100 mx-2 my-1 p-0\">" +
                        "<a>" + message.text + "</a>" +
                    "</div>" +
                "</div>" +
            "</div>"
        );
    }
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