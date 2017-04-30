package client;

import api.IClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cout970 on 2017/04/14.
 */
public class Conversation {

    private IClient other;
    private int unreadCount;

    private List<ClientMsg> msgs = new ArrayList<>();

    public Conversation(IClient other) {
        this.other = other;
    }

    public IClient getOther() {
        return other;
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
