package server.remote;

import api.IClient;

/**
 * Created by pedro on 4/04/17.
 */
class TimedClient {



    IClient client;
        int timeLeft;


    public TimedClient(IClient client, int timeLeft) {
        this.client = client;
        this.timeLeft = timeLeft;
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
