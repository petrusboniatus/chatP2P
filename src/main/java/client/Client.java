package client;

import api.IClient;
import api.IP2P;
import client.controller.Controller;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
public class Client extends UnicastRemoteObject implements IClient, Serializable {

    private Controller manager;
    private IP2P tunnel;

    public Client(Controller manager) throws RemoteException {
        this.manager = manager;
        tunnel = new Tunnel(this);
    }

    @Override
    public void notifyFriendListUpdates() throws RemoteException {
        manager.updateFriends();
    }

    @Override
    public IP2P getP2P() throws RemoteException {
        return tunnel;
    }

    public Controller getManager() {
        return manager;
    }

    private class Tunnel extends UnicastRemoteObject implements IP2P {

        private Client client;

        public Tunnel(Client client) throws RemoteException {
            this.client = client;
        }

        @Override
        public void sendMsg(ClientMsg msg) {
            client.getManager().receiveMsg(msg);
        }
    }
}
