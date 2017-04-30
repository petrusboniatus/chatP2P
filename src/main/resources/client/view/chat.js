/**
 * Created by cout970 on 4/7/17.
 */


jQuery(document).ready(function () {
    jQuery('.scrollbar-inner').scrollbar();
});

function onClick(event) {
    var node = event.target;
    controller.log(node)
    if (document.getElementById("user-search").value === "") {
        controller.openTab(node.childNodes.item(0).textContent);
    } else {
        var index = +(node.getAttribute("index"))
        controller.log(index)
        var user = controller.searchResults.get().get(index)
        controller.log(user)
        showSendPetitionDialog(user)
    }
}

function updateDisplay() {
    var node = document.getElementById("user-list");
    node.innerHTML = "";

    if (document.getElementById("user-search").value === "") {
        var lista = controller.friendProfiles.get();
        var convMap = controller.getConversations();

        for (var i = 0; i < lista.size(); i++) {
            var newNode = document.createElement("div");
            var connected = lista.get(i).getSecond();
            if (connected) {
                newNode.classList.add("conectado");
            } else {
                newNode.classList.add("desconectado");
            }

            var name = lista.get(i).getFirst();
            newNode.innerText = name

            if (convMap.get(name) !== null) {
                var count = convMap.get(name).getUnreadCount();
                if (count > 0) {
                    var span = document.createElement("span")
                    span.classList.add("unread-msg")
                    span.textContent = "" + count;
                    newNode.appendChild(span)
                }
            }

            node.appendChild(newNode);
            newNode.addEventListener('click', onClick);
        }
    } else {
        var listb = controller.searchResults.get();
        for (var j = 0; j < listb.size(); j++) {
            var newNode = document.createElement("div");
            newNode.classList.add("search-result");
            newNode.setAttribute("index", "" + j);
            newNode.innerText = listb.get(j);
            newNode.addEventListener('click', onClick);
            node.appendChild(newNode);
        }
    }
}

function showSendPetitionDialog(user) {
    document.getElementById("input-text").setAttribute("disabled", true);
    var node = document.getElementById("text");
    node.htmlText = ""

    var h1 = document.createElement("h1")
    h1.textContent = "Usuario: "+user
    node.appendChild(h1)

    var p = document.createElement("p")
    p.textContent = "Este usuario aun no es tu amigo, quieres enviar una peticion de amistad?"
    node.appendChild(p)

    var input = document.createElement("input")
    input.value = "Enviar peticion"
    input.type = "button"
    input.classList.add("btn-success")

    input.addEventListener('click', function (event) {
        var node = event.target;
        var index = +(node.getAttribute("index"))
        var user = controller.searchResults.get().get(index)
        controller.sendFriendShipRequest(user)
    })

    node.appendChild(input)
}

function loadTab(oldValue, conversation) {
    var node = document.getElementById("text");
    node.innerHTML = "";

    if (conversation === null) {
        document.getElementById("input-text").setAttribute("disabled", true);
        return;
    }
    document.getElementById("input-text").removeAttribute("disabled");
    var list = conversation.getMsgs();

    if (list.isEmpty()) {
        node.innerHTML = "Aun no se ha iniciado una conversacion con este usuario";
    } else {
        for (var i = 0; i < list.size(); i++) {
            var user = list.get(i).getUser();
            var msg = list.get(i).getMsg();

            var newNode = document.createElement("div");
            newNode.id = "" + i;
            newNode.innerHTML = "<p class=\"username\">" + user + ":</p> <p>" + msg + "<\p>";
            node.appendChild(newNode);
        }
    }
}

function main() {
    document.getElementById("input-text").setAttribute("disabled", true);
    controller.friendProfiles.addListener(updateDisplay);
    controller.searchResults.addListener(updateDisplay);
    controller.selectedTab.addListener(loadTab);
    document.getElementById("user-search").onkeyup = function (e) {
        controller.searchUsers(document.getElementById("user-search").value);
    };
    document.getElementById("input-text").onkeydown = function (e) {
        var code = event.which || event.keyCode;
        if (code === 13) {
            var node = document.getElementById("input-text");
            var txt = node.value;
            node.value = "";
            controller.sendMsg(txt);
        }
    };
    document.getElementById("profile-button").onclick = function () {
        controller.showProfile()
    }
    updateDisplay();
}