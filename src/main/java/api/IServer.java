package api;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
public interface IServer extends Remote {

    /**
     * Avisa al servidor de que el cliente sigue activo
     *
     * @param me token autentificacion
     */
    void imAlive(IAuthToken me) throws RemoteException;

    /**
     * Se registra un usuario nuevo
     *
     * @throws IllegalArgumentException si el registrar falla se lanza la excepcion
     */
    void registerUser(String name, String password) throws RemoteException;

    /**
     * Se hace login en la aplicacion
     *
     * @param me       instancia del cliente para connexiones p2p
     * @param name     Nombre de este cliente
     * @param password Clave de acceso
     * @return token autentificacion
     */
    IAuthToken login(IClient me, String name, String password) throws RemoteException;

    /**
     * Se piden los amigos connectados
     *
     * @param me token autentificacion
     * @return lista de amigos connectados
     */
    List<IProfile> getFriends(IAuthToken me) throws RemoteException;


    /**
     * Nota: el servidor debe comprobar que el nombre coincide con un amigo valido de este cliente
     *
     * @param me   token autentificacion
     * @param name nombre del otro cliente
     * @return objeto remoto de otro cliente
     */
    IClient getConnection(IAuthToken me, String name) throws RemoteException;

    /**
     * Enviar peticion de amistad
     *
     * @param me   token autentificacion
     * @param name nombre del usuario a enviar la peticion de amistad
     */
    void sendFriendshiptRequest(IAuthToken me, String name) throws RemoteException;

    /**
     * recibir las peticiones de amistad
     *
     * @param me token autentificacion
     */

    List<IProfile> getFriendShipRequest(IAuthToken me) throws RemoteException;

    /**
     * @param me   token autentificacion
     * @param name nombre del usuario a dejar de ser amigo
     */
    void sendUnFriendshiptRequest(IAuthToken me, String name) throws RemoteException;

    /**
     * @param me          token autentificacion
     * @param searchInput texto de busqueda
     * @return Lista de usuarios de servidor, incluye los amigos de este usuario
     */
    List<String> searchUsers(IAuthToken me, String searchInput) throws RemoteException;

    interface IAuthToken {
        // El servidor implementa esto como quiera
    }

    interface IProfile extends Serializable {

        /**
         * Obtiene el nombre del usurio
         *
         * @return nombre del usuario
         */
        String getName();

        /**
         * Comprueba si el usuario esta connectado o no
         *
         * @return true si el usuario esta connectado en el momento de creacion de IProfile
         */
        boolean isConnected();
    }
}


