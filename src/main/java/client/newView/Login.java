package client.newView;

import javax.swing.*;

/**
 * Created by cout970 on 4/30/17.
 */
public class Login extends View {
    private JTextField user;
    private JTextField pass;
    private JButton loginButton;
    private JTextField newUser;
    private JTextField newPass;
    private JButton signupButton;
    private JLabel error;
    private JPanel root;

    public Login() {
        error.setVisible(false);
        loginButton.addActionListener(e -> {
            boolean login = getController().tryLogin(user.getText(), pass.getText());
            if (login) {
                ViewHandler.MENU.show();
                error.setVisible(false);
            } else {
                error.setVisible(true);
                error.setText("Par usuario/contraseña invalido");
            }
        });
        signupButton.addActionListener(e -> {
            boolean registered = getController().tryRegister(newUser.getText(), newPass.getText());
            if (registered) {
                error.setVisible(false);
                user.setText(newUser.getText());
                pass.setText(newPass.getText());

                newUser.setText("");
                newPass.setText("");
            } else {
                error.setVisible(true);
                error.setText("Error, usuario ya registrado");
            }
        });
    }

    @Override
    public JPanel getRoot2() {
        return root;
    }
}
