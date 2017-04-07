package client;

/**
 * Created by cout970 on 4/7/17.
 */
public class ClientMsg {

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
}
