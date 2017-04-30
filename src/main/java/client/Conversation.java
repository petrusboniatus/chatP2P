package client;

import api.IClient;
import api.IP2P;
import api.IServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cout970 on 2017/04/14.
 */
public class Conversation {

    private IP2P other;
    private IServer.IAuthToken token;
    private int unreadCount;

    private List<ClientMsg> msgs = new ArrayList<>();

    public Conversation(IP2P other, IServer.IAuthToken token) {
        this.other = other;
        this.token = token;
    }

    public IP2P getOther() {
        return other;
    }

    public IServer.IAuthToken getToken() {
        return token;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public List<ClientMsg> getMsgs() {
        return msgs;
    }

    public void incrementUnread() {
        unreadCount++;
    }

    public void resetUnread(){
        unreadCount = 0;
    }
}
