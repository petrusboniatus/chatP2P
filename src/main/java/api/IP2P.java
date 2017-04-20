package api;

import client.ClientMsg;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by cout970 on 2017/04/14.
 */
public interface IP2P extends Serializable, Remote {

    void sendMsg(ClientMsg msg) throws RemoteException;
}
