package client;

import api.IServer;
import api.RMI;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
public class ServerHandler {

    private String url;
    private IServer server = null;
    private Client client = null;
    private IServer.IAuthToken token;

    public ServerHandler(Client client, String url) {
        this.url = url;
        this.client = client;
    }

    public boolean tryLogin(String name, String password) {
        try {
            token = server.login(client, name, password);
        } catch (Exception e) {
            return false;
        }
        return true;
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

    public boolean tryRegister(String name, String password) {
        try {
            getServer().registerUser(name, password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
