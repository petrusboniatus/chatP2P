/**
 * Created by cout970 on 4/7/17.
 */

jQuery(document).ready(function () {
    jQuery('.scrollbar-inner').scrollbar();
});

function main() {

}

function loadTab(list) {
    var node = document.getElementById("text");
    node.innerHTML = "";

    for (var i = 0; i < list.size(); i++) {
        var user = list.get(i).getUser();
        var msg = list.get(i).getMsg();

        var newNode = document.createElement("div");
        newNode.id = "" + i;
        newNode.innerHTML = "<p class=\"username\">" + user + ":</p> <p>" + msg + "<\p>"
        node.appendChild(newNode);
    }
}

function updateFriends() {
    var list = controller.getFriends();
    var node = document.getElementById("user-list");
    node.innerHTML = "";

    for (var i = 0; i < list.size(); i++) {
        var newNode = document.createElement("div");
        var connected = list.get(i).isConnected();
        if (connected) {
            newNode.classList.add("conectado");
        } else {
            newNode.classList.add("desconectado");
        }

        newNode.innerText = list.get(i).getName();
        node.appendChild(newNode);
    }
}