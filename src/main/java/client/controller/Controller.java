package client.controller;

import client.ServerHandler;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
public class Controller {

    private GUI gui;
    private ServerHandler serverHandler;

    public Controller(GUI gui) {
        this.gui = gui;
    }

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
        gui.load(gui.getLogin());
    }

    public void showError(String error) {
        gui.getLoading().runOnJS("showError('" + error + "');");
    }

    public boolean tryLogin(String name, String password) {
        boolean success = serverHandler.tryLogin(name, password);

        if (success) {
            gui.load(gui.getMenu());
            return true;
        } else {
            return false;
        }
    }

    public boolean tryRegister(String name, String password) {
        return serverHandler.tryRegister(name, password);
    }


    public void log(Object any) {
        System.out.println("js> " + any);
    }

}
