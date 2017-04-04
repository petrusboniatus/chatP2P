package client;

import api.IServer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
public class FriendManager {

    private ServerHandler handler;
    private List<IServer.IProfile> profiles = new ArrayList<>(0);

    public FriendManager(ServerHandler handler) {
        this.handler = handler;
    }

    public void updateFriends(){
        try {
            profiles = handler.getServer().getFriends(handler.getToken());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public List<IServer.IProfile> getProfiles() {
        return profiles;
    }
}
