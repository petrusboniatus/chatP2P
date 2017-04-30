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
import com.awesome.business.template.controller.FXApplication;
import javafx.scene.web.WebEngine;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLInputElement;

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
    public Observable<List<IServer.IProfile>> friendshipPetitions = new Observable<>(null);
    public Observable<List<String>> searchResults = new Observable<>(new ArrayList<>());


    public Map<String, Conversation> getConversations() {
        return allConversations;
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

    public void showError(String error) {
        ViewState.LOADING.getView().runOnJS("showError('" + error + "');");
    }

    // Login

    public boolean tryLogin(String name, String password) {
        boolean success = handler.tryLogin(name, password);

        if (success) {
            clientName = name;
            loadChat();
            updateFriends();
            updatePetitions();
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

    public void loadChat() {
        Utils.runAsync(() -> {
            ViewState.CHAT.load(this);
        });
    }

    // Profile

    public void showProfile() {
        Utils.runAsync(() -> {
            updatePetitions();
            ViewState.PROFILE.load(this);
        });
    }

    private void updatePetitions() {
        friendshipPetitions.set(handler.getFriendshipRequests());
    }

    public void acceptFriendshipRequest(int id) {
        IServer.IProfile friend = friendshipPetitions.get().get(id);
        handler.acceptFriendPetition(friend);
        updatePetitions();
        System.out.println(friendshipPetitions.get());
    }

    public void cancelFriendshipRequest(int id) {
        IServer.IProfile friend = friendshipPetitions.get().get(id);
        handler.cancelFriendPetition(friend);
        updatePetitions();
        System.out.println(friendshipPetitions.get());
    }


    public boolean changePassword(String oldPass, String newPass) {
        try {
            handler.changePassword(oldPass, newPass);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // Chat

    public void sendFriendShipRequest(String user) {
        handler.sendFriendShipRequest(user);
    }

    public List<IServer.IProfile> getFriends() {
        return friendProfiles.get();
    }

    public void searchUsers(String str) {
        List<String> results = handler.searchUsers(str);
        results.remove(clientName);
        for (IServer.IProfile friend : getFriends()) {
            results.remove(friend.getName());
        }
        searchResults.set(results);
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
        Conversation conv = allConversations.get(name);
        conv.resetUnread();
        selectedTab.set(conv);
        updateFriends();
    }


    public void receiveMsg(ClientMsg msg) {

        if (!allConversations.containsKey(msg.getUser())) {
            IServer.IProfile profile = getProfile(msg.getUser());
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
        } else {
            conv.incrementUnread();
        }
        updateFriends();
    }

    private IServer.IProfile getProfile(String name) {
        for (IServer.IProfile friend : getFriends()) {
            if (friend.getName().equals(name)) {
                return friend;
            }
        }
        return null;
    }

    // Debug

    public void log(Object any) {
        System.out.println("js> " + any);
    }
}
