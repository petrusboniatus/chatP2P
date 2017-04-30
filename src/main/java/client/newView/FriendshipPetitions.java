package client.newView;

import api.IServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by cout970 on 4/30/17.
 */
public class FriendshipPetitions extends View {
    private JButton volverButton;
    private JPanel root;
    private JPanel results;

    public FriendshipPetitions() {
        LayoutManager layout = new BoxLayout(results, BoxLayout.Y_AXIS);
        results.setLayout(layout);

        volverButton.addActionListener(e -> {
            ViewHandler.MENU.show();
        });
    }

    @Override
    public void onLoad() {
        results.removeAll();
        getController().updateFriends();
        List<IServer.IProfile> profiles = getController().getFriendshipPetitions();
        if (profiles.isEmpty()) {
            results.add(new JLabel("No hay peticiones de amistad"));
        } else {
            for (IServer.IProfile user : profiles) {
                JPanel panel = new JPanel();
                results.add(panel);

                panel.add(new JLabel("Usuario: " + user.getName()));
                JButton button = new JButton("Aceptar peticion");
                button.addActionListener(e1 -> {
                    try {
                        getController().acceptFriendshipRequest(user);
                        onLoad();
                    } catch (IllegalArgumentException e3) {
                        //fine
                    }
                });
                panel.add(button);
            }
        }
        results.revalidate();
        results.repaint();
    }

    @Override
    public JPanel getRoot2() {
        return root;
    }
}
