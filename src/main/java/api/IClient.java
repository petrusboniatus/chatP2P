package api;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
public interface IClient extends Remote {


    /**
     * Notifica al cliente que su lista de amigos ha cambiado de estado
     * Esto puede deberse a que han aceptado una peticion de amistad o se ha connectado/desconectado un amigo
     *
     */
    void notifyFriendListUpdates() throws RemoteException;
}
