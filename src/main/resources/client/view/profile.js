/**
 * Created by cout970 on 4/28/17.
 */

function loadPetitions() {
    controller.log("loadPetitions")

    var profiles = controller.friendshipPetitions.get()
    var node = document.getElementById("peticiones")
    //clear
    node.htmlText = ""

    if (profiles.isEmpty()) {
        node.innerHTML = "" +
            "<div class=\"petition\">" +
            "<p>No tienes peticiones de amistad</p>" +
            "</div>"
    } else {
        controller.log("test")
        for (var i = 0; i < profiles.size(); i++) {
            var newNode = document.createElement("div")

            //class="petition"
            newNode.classList.add("petition")

            var name = profiles.get(i).getFirst()

            var p = document.createElement("p")
            p.textContent = "Peticion de amistad de " + name
            newNode.appendChild(p)

            var input1 = document.createElement("input")
            input1.type = "button"
            input1.classList.add("btn-success")
            input1.value = "Aceptar"
            input1.setAttribute("button-id", i.toString())
            input1.addEventListener('click', function (event) {
                var node = event.target;
                var id = node.getAttribute("button-id")
                controller.acceptFriendshipRequest(+id)
            })
            newNode.appendChild(input1)

            var input2 = document.createElement("input")
            input2.type = "button"
            input2.classList.add("btn-danger")
            input2.value = "Cancelar"
            input2.setAttribute("button-id", i.toString())

            input2.addEventListener('click', function (event) {
                controller.log(event)
                var node = event.target;
                var id = node.getAttribute("button-id")
                controller.cancelFriendshipRequest(+id)
            })
            newNode.appendChild(input2)

            node.appendChild(newNode)
        }
    }
}

function main() {
    controller.friendshipPetitions.addListener(loadPetitions)

    document.getElementById("pass-button").addEventListener('click', function (e) {
        var pass1 = document.getElementById("oldpass")
        var pass2 = document.getElementById("newpass")
        var pass3 = document.getElementById("passcheck")

        if (pass2.value === pass3.value) {
            var oldPass = pass1.value
            var newPass = pass2.value

            var result = controller.changePassword(oldPass, newPass)
            if (!result) {
                pass1.style.backgroundColor = "#F33"
            } else {
                pass1.style.backgroundColor = "#FFF"
                pass1.value = ""
                pass2.value = ""
                pass3.value = ""
            }
            pass2.style.backgroundColor = "#FFF"
            pass3.style.backgroundColor = "#FFF"
        } else {
            pass1.style.backgroundColor = "#FFF"
            pass2.style.backgroundColor = "#F33"
            pass3.style.backgroundColor = "#F33"
        }
    })

    document.getElementById("back-button").addEventListener('click', function (e) {
        controller.loadChat()
    })
    loadPetitions()
}