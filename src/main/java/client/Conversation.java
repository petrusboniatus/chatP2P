package client;

import api.IClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cout970 on 2017/04/14.
 */
public class Conversation {

    private IClient other;

    private List<ClientMsg> msgs = new ArrayList<>();

    public Conversation(IClient other) {
        this.other = other;
    }

    public IClient getOther() {
        return other;
    }

    public List<ClientMsg> getMsgs() {
        return msgs;
    }
}
