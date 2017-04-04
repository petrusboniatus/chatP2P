//noinspection ES6ConvertVarToLetConst

/**
 * Created by cout970 on 4/4/17.
 */

var controller = {
    tryLogin: () => {},
    tryRegister: () => {},
    log: () => {},
    getFriends: () => {}
};

function main() {
    updateFriends();
}

function updateFriends() {
    let list = controller.getFriends();
    let node = document.getElementById("user-list");
    node.innerHTML = "";

    for (let i = 0; i < list.size(); i++) {
        let newNode = document.createElement("div");
        let connected = list.get(i).isConnected();
        if (connected) {
            newNode.classList.add("btn-success");
        } else {
            newNode.classList.add("btn-default");
        }

        newNode.innerText = list.get(i).getName();
        node.appendChild(newNode);
    }
}