/**
 * Created by cout970 on 4/7/17.
 */

jQuery(document).ready(function () {
    jQuery('.scrollbar-inner').scrollbar();
});

function main() {

}

function loadTab(list) {
    let node = document.getElementById("text");
    node.innerHTML = "";

    for(let i = 0; i < list.size(); i++){
        let user = list.get(i).getUser();
        let msg = list.get(i).getMsg();

        let newNode = document.createElement("div");
        newNode.id = "" + i;
        newNode.innerHTML = "<p class=\"username\">" + user + ":</p> <p>" + msg + "<\p>"
        node.appendChild(newNode);
    }
}

function updateFriends() {
    let list = controller.getFriends();
    let node = document.getElementById("user-list");
    node.innerHTML = "";

    for (let i = 0; i < list.size(); i++) {
        let newNode = document.createElement("div");
        let connected = list.get(i).isConnected();
        if (connected) {
            newNode.classList.add("conectado");
        } else {
            newNode.classList.add("desconectado");
        }

        newNode.innerText = list.get(i).getName();
        node.appendChild(newNode);
    }
}