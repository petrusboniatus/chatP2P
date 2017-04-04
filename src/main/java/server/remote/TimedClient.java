package server.remote;

import api.IClient;
import server.daos.Profile;

/**
 * Created by pedro on 4/04/17.
 */
class TimedClient {



    IClient client;
    int timeLeft;
    Profile pefil;

    public TimedClient(IClient client, int timeLeft, Profile pefil) {
        this.client = client;
        this.timeLeft = timeLeft;
        this.pefil = pefil;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimedClient that = (TimedClient) o;

        return client != null ? client.equals(that.client) : that.client == null;
    }

    @Override
    public int hashCode() {
        return client != null ? client.hashCode() : 0;
    }
}
