package client.newView;

import javax.swing.*;

/**
 * Created by cout970 on 4/30/17.
 */
public class ChangePassword extends View {
    private JTextField textField1;
    private JTextField textField2;
    private JButton actualizarContraseñaButton;
    private JButton cancelarButton;
    private JPanel root;
    private JLabel error;

    public ChangePassword() {
        error.setVisible(false);
        actualizarContraseñaButton.addActionListener(e -> {
            boolean success = getController().changePassword(textField1.getText(), textField2.getText());
            if (success) {
                error.setVisible(false);
            } else {
                error.setVisible(true);
                error.setText("No se pudo cambiar la contraseña");
            }
        });
        cancelarButton.addActionListener(e -> {
            ViewHandler.MENU.show();
        });
    }

    @Override
    public JPanel getRoot2() {
        return root;
    }
}
