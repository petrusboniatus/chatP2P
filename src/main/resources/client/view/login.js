//noinspection ES6ConvertVarToLetConst

/**
 * Created by cout970 on 4/3/17.
 */

var controller = {
    tryLogin: () => {},
    tryRegister: () => {},
    log: () => {}
};

function main() {
    document.getElementById("login").addEventListener("click", function (event) {

        document.getElementById("message").style.display = "none";
        let name = document.getElementById("loginUser").value;
        let pass = document.getElementById("loginPassword").value;

        let success = controller.tryLogin(name, pass);

        if (success) {
            let node = document.getElementById("message");
            node.innerHTML = "<h3>Error, usuario o constraseña invalida</h3>";
            node.style.display = "block";
        }
    });

    document.getElementById("register").addEventListener("click", function (event) {

        document.getElementById("message").style.display = "none";

        let name = document.getElementById("registerUser").value;
        let pass = document.getElementById("registerPassword").value;
        let pass2 = document.getElementById("registerPassword2").value;

        if (pass !== pass2) {
            let node = document.getElementById("message");
            node.innerHTML = "<h3>Error, las contraseñas no coinciden!</h3>";
            node.style.display = "block";
        } else {
            controller.log("sfghajdgh");
            let success = controller.tryRegister(name, pass);
            if (!success) {
                let subNode = document.getElementById("message");
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