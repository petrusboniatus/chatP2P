package client.newView;

import client.controller.Controller;

import javax.swing.*;

/**
 * Created by cout970 on 4/30/17.
 */
public abstract class View {

    private Controller controller;

    public void onLoad(){}

    public void onUpdate(){}

    public abstract JPanel getRoot();

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }
}
