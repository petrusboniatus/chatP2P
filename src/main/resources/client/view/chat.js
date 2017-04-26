/**
 * Created by cout970 on 4/7/17.
 */

controller.log("start");
var controller = {};

jQuery(document).ready(function () {
    jQuery('.scrollbar-inner').scrollbar();
});

function onClick(event) {
    var node = event.target;
    controller.openTab(node.innerText);
}

function updateDisplay() {
    var node = document.getElementById("user-list");
    node.innerHTML = "";
    if (document.getElementById("user-search").value === "") {
        var lista = controller.getFriends();

        for (var i = 0; i < lista.size(); i++) {
            var newNode = document.createElement("div");
            var connected = lista.get(i).isConnected();
            if (connected) {
                newNode.classList.add("conectado");
            } else {
                newNode.classList.add("desconectado");
            }

            newNode.innerText = lista.get(i).getName();
            node.appendChild(newNode);
            newNode.addEventListener('click', onClick);
        }
    } else {
        var listb = controller.searchResults.get();
        for (var j = 0; j < listb.size(); j++) {
            var newNode = document.createElement("div");
            newNode.classList.add("search-result");
            newNode.setAttribute("index", j);
            newNode.innerText = listb.get(j);
            node.appendChild(newNode);
        }
    }
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
    }
}