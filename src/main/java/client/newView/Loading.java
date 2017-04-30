package client.newView;

import javax.swing.*;

/**
 * Created by cout970 on 4/30/17.
 */
public class Loading extends View {
    private JPanel root;
    private JLabel label;

    @Override
    public JPanel getRoot2() {
        return root;
    }

    public void showError(String s) {
        label.setText(s);
    }
}
