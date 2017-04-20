package client;

import java.io.Serializable;

/**
 * Created by cout970 on 4/7/17.
 */
public class ClientMsg implements Serializable {

    private String user;
    private String msg;

    public ClientMsg(String user, String msg) {
        this.user = user;
        this.msg = msg;
    }

    public String getUser() {
        return user;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientMsg)) {
            return false;
        }

        ClientMsg clientMsg = (ClientMsg) o;

        if (user != null ? !user.equals(clientMsg.user) : clientMsg.user != null) {
            return false;
        }
        return msg != null ? msg.equals(clientMsg.msg) : clientMsg.msg == null;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (msg != null ? msg.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClientMsg{" +
                "user='" + user + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
