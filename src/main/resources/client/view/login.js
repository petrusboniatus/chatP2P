/**
 * Created by cout970 on 4/3/17.
 */

function main() {
    document.getElementById("login").addEventListener("click", function (event) {

        document.getElementById("message").style.display = "none";
        var name = document.getElementById("loginUser").value;
        var pass = document.getElementById("loginPassword").value;

        var success = controller.tryLogin(name, pass);

        if (!success) {
            var node = document.getElementById("message");
            node.innerHTML = "<h3>Error, usuario o constraseña invalida</h3>";
            node.style.display = "block";
        }
    });

    document.getElementById("register").addEventListener("click", function (event) {

        document.getElementById("message").style.display = "none";

        var name = document.getElementById("registerUser").value;
        var pass = document.getElementById("registerPassword").value;
        var pass2 = document.getElementById("registerPassword2").value;

        if (pass !== pass2) {
            var node = document.getElementById("message");
            node.innerHTML = "<h3>Error, las contraseñas no coinciden!</h3>";
            node.style.display = "block";
        } else {
            var success = controller.tryRegister(name, pass);
            if (!success) {
                var subNode = document.getElementById("message");
                subNode.innerHTML = "<h3>Error al registrar usuario</h3>";
                subNode.style.display = "block";
            } else {
                document.getElementById("loginUser").value = name;
                document.getElementById("loginPassword").value = pass;

                document.getElementById("registerUser").value = "";
                document.getElementById("registerPassword").value = "";
                document.getElementById("registerPassword2").value = "";
            }
        }
    });
}