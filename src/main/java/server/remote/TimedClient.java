package server.remote;

import api.IClient;

/**
 * Created by pedro on 4/04/17.
 */
class TimedClient {

        IClient client;
        AuthToken aut;
        int timeLeft;

    public TimedClient(IClient client, AuthToken aut, int timeLeft) {
        this.client = client;
        this.aut = aut;
        this.timeLeft = timeLeft;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimedClient that = (TimedClient) o;

        if (timeLeft != that.timeLeft) return false;
        if (client != null ? !client.equals(that.client) : that.client != null) return false;
        return aut != null ? aut.equals(that.aut) : that.aut == null;
    }

    @Override
    public int hashCode() {
        int result = client != null ? client.hashCode() : 0;
        result = 31 * result + (aut != null ? aut.hashCode() : 0);
        result = 31 * result + timeLeft;
        return result;
    }
}
