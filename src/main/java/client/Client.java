package client;

import api.IClient;
import client.controller.Controller;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
public class Client extends UnicastRemoteObject implements IClient, Serializable {

    private Controller manager;

    protected Client() throws RemoteException {}

    @Override
    public void notifyFriendListUpdates() throws RemoteException {
        if (manager != null) {
            manager.updateFriends();
        }
    }

    public Controller getManager() {
        return manager;
    }

    public void setManager(Controller manager) {
        this.manager = manager;
    }
}
