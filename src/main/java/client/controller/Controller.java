package client.controller;

import api.IServer;
import client.ServerHandler;
import javafx.application.Platform;
import org.w3c.dom.ls.LSInput;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
@SuppressWarnings("ALL")
public class Controller {

    private GUI gui;
    private ServerHandler serverHandler;
    private List<IServer.IProfile> friendProfiles = new ArrayList<>(0);


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
            new Thread(() -> {
                gui.load(gui.getMenu());
                onMenuOpen();
            }).start();
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

    public List<IServer.IProfile> getFriends() {
        return friendProfiles;
    }

    private void onMenuOpen() {
        try {
            List<String> list = serverHandler.getServer().searchUsers(serverHandler.getToken(), "");
            List<IServer.IProfile> friends = serverHandler.getServer().getFriends(serverHandler.getToken());
            List<String> ignoredUsers = new ArrayList<>(friends.size());

            for (IServer.IProfile friend : friends) {
                ignoredUsers.add(friend.getName());
            }

            for (String s : list) {
                if (!ignoredUsers.contains(s)) {
                    System.out.println("send request: "+ s);
                    serverHandler.getServer().sendFriendshiptRequest(serverHandler.getToken(), s);
                }
            }
            Thread.sleep(1000);

            List<IServer.IProfile> req = serverHandler.getServer().getFriendShipRequest(serverHandler.getToken());

            for (IServer.IProfile iProfile : req) {
                serverHandler.getServer().acceptFriendPetition(serverHandler.getToken(), iProfile);
            }
            updateFriends();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateFriends() {
        try {
            friendProfiles = serverHandler.getServer().getFriends(serverHandler.getToken());
            gui.getMenu().runOnJS("updateFriends();");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void log(Object any) {
        System.out.println("js> " + any);
    }

}
