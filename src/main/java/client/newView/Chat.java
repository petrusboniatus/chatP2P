package client.newView;

import api.IP2P;
import api.IServer;
import client.ClientMsg;
import client.Conversation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Created by cout970 on 4/30/17.
 */
public class Chat extends View {
    private JTextField userInput;
    private JButton volverButton;
    private JTextArea textArea;
    private JPanel root;
    private JPanel sidePanel;
    private JLabel title;

    private static final String template = "Hablando con %user%";

    public Chat() {
        LayoutManager layout = new BoxLayout(sidePanel, BoxLayout.Y_AXIS);
        sidePanel.setLayout(layout);

        userInput.setEnabled(false);

        userInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER && !userInput.getText().isEmpty()) {
                    getController().sendMsg(userInput.getText());
                    userInput.setText("");
                    onLoad();
                }
            }
        });
        volverButton.addActionListener(e -> {
            ViewHandler.MENU.show();
        });
    }

    @Override
    public void onUpdate() {
        title.setText(template.replace("%user%", getController().getUserName()));

        List<IServer.IProfile> friends = getController().getFriendProfiles();
        sidePanel.removeAll();

        for (IServer.IProfile friend : friends) {
            Conversation conv = getController().getConversations().get(friend.getName());
            String extra = (conv != null && conv.getUnreadCount() > 0 ? " " + conv.getUnreadCount() : "");

            JButton button = new JButton(friend.getName() + extra);
            button.setEnabled(friend.isConnected());

            button.addActionListener(e -> {
                getController().openTab(friend.getName());
                onLoad();
            });
            sidePanel.add(button);
        }

        sidePanel.revalidate();
        sidePanel.repaint();

        Conversation conv = getController().getSelectedTab();
        if (conv == null) {
            textArea.removeAll();
            textArea.setText("");
            userInput.setEnabled(false);
        } else {
            userInput.setEnabled(true);
            StringBuilder builder = new StringBuilder();
            for (ClientMsg msg : conv.getMsgs()) {
                builder.append(msg.getUser());
                builder.append(": \n\t");
                builder.append(msg.getMsg());
                builder.append('\n');
            }
            textArea.setText(builder.toString());
        }
    }

    @Override
    public void onLoad() {
        getController().updateFriends();
        onUpdate();
    }

    @Override
    public JPanel getRoot() {
        return root;
    }
}
