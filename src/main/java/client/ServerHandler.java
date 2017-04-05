package client;

import api.IServer;
import api.RMI;
import client.controller.ViewState;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
public class ServerHandler {

    private String url;
    private IServer server = null;
    private Client client = null;
    private IServer.IAuthToken token;
    private Thread alive;

    public ServerHandler(Client client, String url) {
        this.url = url;
        this.client = client;
        alive = new Thread(this::notifyServer);
    }

    public boolean tryLogin(String name, String password) {
        try {
            token = server.login(client, name, password);
            alive.start();
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
                server = null;
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public IServer.IAuthToken getToken() {
        return token;
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
}
