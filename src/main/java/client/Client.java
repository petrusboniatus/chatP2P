package client;

import api.IClient;
import api.IP2P;
import api.IServer;
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
        tunnel = new Tunnel();
    }

    @Override
    public void notifyFriendListUpdates() throws RemoteException {
        manager.updateFriends();
        manager.crearUnusedChats();
    }

    @Override
    public IP2P getP2P() throws RemoteException {
        return tunnel;
    }

    public Controller getManager() {
        return manager;
    }

    private class Tunnel extends UnicastRemoteObject implements IP2P {

        public Tunnel() throws RemoteException {}

        @Override
        public String getUserName() throws RemoteException {
            return getManager().getUserName();
        }

        @Override
        public void sendMsg(ClientMsg msg) {
            getManager().receiveMsg(msg);
        }

        @Override
        public void sendFile(ClientFile msg) throws RemoteException {
            getManager().receiveFile(msg);
        }
    }

    @Override
    public void startConnections(IP2P connection, IServer.IAuthToken token) throws RemoteException {
        manager.startConnection(connection, token);
    }
}
