package client.newView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cout970 on 4/30/17.
 */
public class SearchUsers extends View {
    private JTextField searchBar;
    private JPanel results;
    private JButton volverButton;
    private JPanel root;

    public SearchUsers() {
        LayoutManager layout = new BoxLayout(results, BoxLayout.Y_AXIS);
        results.setLayout(layout);

        volverButton.addActionListener(e -> {
            ViewHandler.MENU.show();
        });
        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateList();
            }
        });
    }

    private List<String> sendedPetitions = new ArrayList<>();

    private void updateList(){
        getController().updateFriends();
        List<String> list = getController().searchUsers(searchBar.getText());

        list.removeAll(sendedPetitions);
        results.removeAll();

        for (String user : list) {
            JPanel panel = new JPanel();
            results.add(panel);

            panel.add(new JLabel("Usuario: " + user));
            JButton button = new JButton("Enviar peticion");
            button.addActionListener(e1 -> {
                try {
                    getController().sendFriendShipRequest(user);
                    sendedPetitions.add(user);
                } catch (IllegalArgumentException e3) {
                    //fine
                }
                updateList();
            });
            panel.add(button);
        }
        results.revalidate();
        results.repaint();
    }

    @Override
    public JPanel getRoot2() {
        return root;
    }
}
