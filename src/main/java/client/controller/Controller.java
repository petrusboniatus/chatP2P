package client.controller;

import api.IClient;
import api.IP2P;
import api.IServer;
import client.ClientMsg;
import client.Conversation;
import client.ServerConnection;
import client.newView.ViewHandler;

import javax.swing.*;
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
    private List<IServer.IProfile> friendProfiles = new ArrayList<>(0);
    private List<IServer.IProfile> friendshipPetitions = null;

    private Conversation selectedTab = null;


    public List<IServer.IProfile> getFriendProfiles() {
        return friendProfiles;
    }

    public List<IServer.IProfile> getFriendshipPetitions() {
        return friendshipPetitions;
    }

    public Conversation getSelectedTab() {
        return selectedTab;
    }

    public Map<String, Conversation> getConversations() {
        return allConversations;
    }

    public void setHandler(ServerConnection handler) {
        this.handler = handler;
    }

    // Login

    public boolean tryLogin(String name, String password) {
        boolean success = handler.tryLogin(name, password);

        if (success) {
            clientName = name;
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

    // Profile

    public void acceptFriendshipRequest(IServer.IProfile friend) {
        try {
            handler.acceptFriendPetition(friend);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
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

    public List<String> searchUsers(String str) {
        List<String> results = handler.searchUsers(str);
        results.remove(clientName);
        for (IServer.IProfile friend : friendProfiles) {
            results.remove(friend.getName());
        }
        return results;
    }

    public void sendMsg(String txt) {
        Conversation conv = selectedTab;
        IP2P client = conv.getOther();
        ClientMsg msg = new ClientMsg(clientName, conv.getToken(), txt);

        try {
            client.sendMsg(msg);
            conv.getMsgs().add(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void updateFriends() {
        updatePetitions();
        List<IServer.IProfile> req = handler.getFriends();
        friendProfiles = req;
        ViewHandler.getCurrentView().onUpdate();
    }

    private void updatePetitions() {
        List<IServer.IProfile> req = handler.getFriendshipRequests();
        friendshipPetitions = req;
    }

    public void openTab(String name) {
        IServer.IProfile friend = null;
        for (IServer.IProfile profile : friendProfiles) {
            if (profile.getName().equals(name)) {
                friend = profile;
                break;
            }
        }

        if (friend == null || !friend.isConnected()) {
            return;
        }
        if (!allConversations.containsKey(name)) {
            handler.requestConnection(name);
        }
        if (allConversations.containsKey(name)) {
            Conversation conv = allConversations.get(name);
            conv.resetUnread();
            selectedTab = conv;
            updateFriends();
        }
    }


    public void receiveMsg(ClientMsg msg) {

        if (allConversations.containsKey(msg.getUser())) {
            Conversation conv = allConversations.get(msg.getUser());

            if(msg.check(conv.getToken())){
                conv.getMsgs().add(msg);
                if (selectedTab == conv) {
                    selectedTab = conv;
                } else {
                    conv.incrementUnread();
                }
                updateFriends();
            }
        }
    }

    private IServer.IProfile getProfile(String name) {
        for (IServer.IProfile friend : friendProfiles) {
            if (friend.getName().equals(name)) {
                return friend;
            }
        }
        return null;
    }

    public String getUserName() {
        return clientName;
    }

    public void startConnection(IP2P connection, IServer.IAuthToken token) {
        String name = null;
        try {
            name = connection.getUserName();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (!allConversations.containsKey(name)) {
            IServer.IProfile profile = getProfile(name);
            if (profile == null) {
                return;
            }
            Conversation conv = new Conversation(connection, token);
            allConversations.put(profile.getName(), conv);
        }
    }
}
