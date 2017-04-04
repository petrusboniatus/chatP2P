package client;

import api.IServer;
import api.RMI;

import java.rmi.RemoteException;

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
        alive = new Thread(this::runThread);
    }

    public boolean tryLogin(String name, String password) {
        try {
            token = server.login(client, name, password);
            alive.start();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void runThread() {
        while (server != null) {
            try {
                server.imAlive(token);
            } catch (RemoteException e) {
                e.printStackTrace();
                server = null;
                break;
            }
            try {
                Thread.sleep(60 * 1000);
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
}
