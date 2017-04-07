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

    public Client(Controller manager) throws RemoteException {
        this.manager = manager;
    }

    @Override
    public void notifyFriendListUpdates() throws RemoteException {
        System.out.println("Friends changed");
        manager.updateFriends();
    }

    public Controller getManager() {
        return manager;
    }
}
