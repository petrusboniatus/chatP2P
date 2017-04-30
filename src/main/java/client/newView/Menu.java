package client.newView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by cout970 on 4/30/17.
 */
public class Menu extends View{
    private JButton salirButton;
    private JButton cambiarContraseñaButton;
    private JButton buscarAmigosButton;
    private JButton chatearButton;
    private JButton peticionesDeAmistadButton;
    private JPanel root;

    public Menu() {
        salirButton.addActionListener(e -> {
            System.exit(0);
        });
        cambiarContraseñaButton.addActionListener(e -> {
            ViewHandler.CHANGE_PASSWORD.show();
        });
        peticionesDeAmistadButton.addActionListener(e -> {
            ViewHandler.FRIENDSHIP_REQUESTS.show();
        });
        buscarAmigosButton.addActionListener(e -> {
            ViewHandler.SEARCH_USERS.show();
        });
        chatearButton.addActionListener(e -> {
            ViewHandler.CHAT.show();
        });
    }

    @Override
    public JPanel getRoot() {
        return root;
    }
}
