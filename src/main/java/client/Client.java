package client;

import api.IClient;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
public class Client extends UnicastRemoteObject implements IClient, Serializable {

    private FriendManager manager;

    protected Client() throws RemoteException {}

    @Override
    public boolean stillAlive() throws RemoteException {
        return true;
    }

    @Override
    public void notifyFriendListUpdates() throws RemoteException {
        if (manager != null) {
            manager.updateFriends();
        }
    }

    public FriendManager getManager() {
        return manager;
    }

    public void setManager(FriendManager manager) {
        this.manager = manager;
    }
}
