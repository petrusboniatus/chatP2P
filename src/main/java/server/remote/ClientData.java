package server.remote;

import api.IClient;
import server.daos.Profile;

/**
 * Created by pedro on 4/04/17.
 */
public class ClientData {


    private IClient client;
    private int timeLeft;
    private Profile pefil;
    private AuthToken token;

    public ClientData(){}

    public ClientData(IClient client, int timeLeft, Profile pefil, AuthToken token) {
        this.client = client;
        this.timeLeft = timeLeft;
        this.pefil = pefil;
        this.token = token;
    }

    public IClient getClient() {
        return client;
    }

    public void setClient(IClient client) {
        this.client = client;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public Profile getPefil() {
        return pefil;
    }

    public void setPefil(Profile pefil) {
        this.pefil = pefil;
    }

    public AuthToken getToken() {
        return token;
    }

    public void setToken(AuthToken token) {
        this.token = token;
    }


}

