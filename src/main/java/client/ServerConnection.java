package client;

import api.IClient;
import api.IServer;
import api.RMI;
import client.newView.ViewHandler;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
public class ServerConnection {

    private String url;
    private IServer server = null;
    private Client client = null;
    private IServer.IAuthToken token;
    private Credentials credentials = null;
    private Thread alive;

    public ServerConnection(Client client, String url) {
        this.url = url;
        this.client = client;
        alive = new Thread(this::notifyServer);
    }

    public boolean tryLogin(String name, String password) {
        try {
            token = server.login(client, name, password);
            credentials = new Credentials(name, password);
            alive.start();
        } catch (IllegalArgumentException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Thread que avisa al servidor que el cliente esta activo
    private void notifyServer() {
        while (true) {
            try {
                getServer().imAlive(token);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error al connectar, reconnectando...");
                try {
                    token = server.login(client, credentials.getUsername(), credentials.getPassword());
                    getServer().imAlive(token);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    break;
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewHandler.connectToServer();
    }

    public IServer getServer() {
        if (server == null) {
            server = RMI.lookup(url);
        }
        return server;
    }

    public List<IServer.IProfile> getFriends() {
        try {
            return getServer().getFriends(token);
        } catch (RemoteException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<String> searchUsers(String str) {
        try {
            return getServer().searchUsers(token, str);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void acceptFriendPetition(IServer.IProfile name) {
        try {
            getServer().acceptFriendPetition(token, name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cancelFriendPetition(IServer.IProfile friend) {
        try {
            getServer().sendUnFriendshiptRequest(token, friend.getName());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void changePassword(String oldPass, String newPass) {
        try {
            getServer().changePassword(token, oldPass, newPass);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendFriendShipRequest(String user) {
        try {
            getServer().sendFriendshiptRequest(token, user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public List<IServer.IProfile> getFriendshipRequests() {
        try {
            return getServer().getFriendShipRequest(token);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void requestConnection(String name) {
        try {
            getServer().requestConnection(token, name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private class Credentials {
        private String username;
        private String password;

        public Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }

        private String getUsername() {
            return username;
        }

        private String getPassword() {
            return password;
        }
    }
}
