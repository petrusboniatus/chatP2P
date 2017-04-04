package client.controller;

import api.IServer;
import client.ServerHandler;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
@SuppressWarnings("ALL")
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
        try {
            serverHandler.getServer().registerUser(name, password);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<IServer.IProfile> getFriends(){
        try {
            return serverHandler.getServer().getFriends(serverHandler.getToken());
        } catch (RemoteException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void log(Object any) {
        System.out.println("js> " + any);
    }

}
