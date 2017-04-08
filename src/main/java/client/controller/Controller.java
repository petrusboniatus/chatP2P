package client.controller;

import api.IServer;
import client.Client;
import api.Defaults;
import client.ServerConnection;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
@SuppressWarnings("ALL")
public class Controller {

    private ServerConnection handler;
    private List<IServer.IProfile> friendProfiles = new ArrayList<>(0);
    private String clientName;

    public void openTab(){
        ViewState.MENU.getView().runOnJS("loadTab(list);");
    }

    public void showError(String error) {
        ViewState.LOADING.getView().runOnJS("showError('" + error + "');");
    }

    public boolean tryLogin(String name, String password) {
        boolean success = handler.tryLogin(name, password);

        System.out.println("success = " + success);
        if (success) {
            clientName = name;
            new Thread(() -> {
                ViewState.MENU.load(this);
                onMenuOpen();
            }).start();
            return true;
        } else {
            return false;
        }
    }

    public boolean tryRegister(String name, String password) {
        try {
            handler.getServer().registerUser(name, password);
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
            List<String> list = handler.getServer().searchUsers(handler.getToken(), "");
            List<IServer.IProfile> friends = handler.getServer().getFriends(handler.getToken());
            List<String> ignoredUsers = new ArrayList<>(friends.size());

            for (IServer.IProfile friend : friends) {
                ignoredUsers.add(friend.getName());
            }

            ignoredUsers.add(clientName);

            for (String s : list) {
                if (!ignoredUsers.contains(s)) {
                    System.out.println("send request: "+ s);
                    handler.getServer().sendFriendshiptRequest(handler.getToken(), s);
                }
            }
            Thread.sleep(1000);

            List<IServer.IProfile> req = handler.getServer().getFriendShipRequest(handler.getToken());

            for (IServer.IProfile iProfile : req) {
                handler.getServer().acceptFriendPetition(handler.getToken(), iProfile);
            }
            updateFriends();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateFriends() {
        friendProfiles = handler.getFriends();
        ViewState.MENU.getView().runOnJS("updateFriends();");
    }

    public void log(Object any) {
        System.out.println("js> " + any);
    }

    public void connectToServer() {
        ViewState.LOADING.load(this);
        
        Client client;

        try {
            client = new Client(this);
        } catch (RemoteException e) {
            showError("Error interno, no se pudo crear la clase Cliente");
            return;
        }

        handler = new ServerConnection(client, Defaults.clientURL);

        if(handler.getServer() == null){
            showError("Error al connectar con el servidor");
            return;
        }

        ViewState.LOGIN.load(this);
    }
}
