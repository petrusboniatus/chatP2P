package client.controller;

import api.Defaults;
import api.IClient;
import api.IP2P;
import api.IServer;
import client.Client;
import client.ClientMsg;
import client.Conversation;
import client.ServerConnection;
import com.awesome.business.template.api.Observable;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
@SuppressWarnings("ALL")
public class Controller {

    private ServerConnection handler;
    private String clientName;
    private Map<String, Conversation> allConversations = new HashMap<>();

    //Observables
    public Observable<List<IServer.IProfile>> friendProfiles = new Observable<>(new ArrayList<>(0));
    public Observable<Conversation> selectedTab = new Observable<>(null);
    public Observable<List<String>> searchResults = new Observable<>(new ArrayList<>());

    public void showError(String error) {
        ViewState.LOADING.getView().runOnJS("showError('" + error + "');");
    }

    public boolean tryLogin(String name, String password) {
        boolean success = handler.tryLogin(name, password);

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
        return friendProfiles.get();
    }

    public void searchUsers(String str) {
        searchResults.set(handler.searchUsers(str));
    }

    public void sendMsg(String txt) {
        IClient client = selectedTab.get().getOther();
        IP2P tunnel = null;
        try {
            tunnel = client.getP2P();
        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }
        ClientMsg msg = new ClientMsg(clientName, txt);
        Conversation conv = selectedTab.get();
        conv.getMsgs().add(msg);
        selectedTab.set(conv);
        try {
            tunnel.sendMsg(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
                    try {
                        System.out.println("send request: " + s);
                        handler.getServer().sendFriendshiptRequest(handler.getToken(), s);
                    } catch (Exception e) {

                    }
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
        friendProfiles.set(handler.getFriends());
    }

    public void openTab(String name) {
        IServer.IProfile friend = null;
        for (IServer.IProfile profile : getFriends()) {
            if (profile.getName().equals(name)) {
                friend = profile;
                break;
            }
        }

        if (friend == null || !friend.isConnected()) {
            return;
        }
        if (!allConversations.containsKey(name)) {
            IClient client = handler.connect(friend);
            if (client == null) {
                return;
            }
            Conversation c = new Conversation(client);
            allConversations.put(name, c);
        }
        selectedTab.set(allConversations.get(name));
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
            e.printStackTrace();
            return;
        }

        handler = new ServerConnection(client, Defaults.clientURL);

        if (handler.getServer() == null) {
            showError("Error al connectar con el servidor");
            return;
        }

        ViewState.LOGIN.load(this);
    }

    public void receiveMsg(ClientMsg msg) {
        if (!allConversations.containsKey(msg.getUser())) {
            IServer.IProfile profile = null;
            for (IServer.IProfile friend : getFriends()) {
                if (friend.getName() == msg.getUser()) {
                    profile = friend;
                    break;
                }
            }
            if (profile == null) {
                return;
            }
            IClient other = handler.connect(profile);
            Conversation conv = new Conversation(other);
            allConversations.put(profile.getName(), conv);
        }
        Conversation conv = allConversations.get(msg.getUser());
        conv.getMsgs().add(msg);
        if (selectedTab.get() == conv) {
            selectedTab.set(conv);
        }
    }
}
