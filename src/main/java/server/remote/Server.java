package server.remote;

import api.IClient;
import api.IServer;
import server.daos.DAOLogin;
import server.daos.DAOUsuarios;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by pedro on 4/04/17.
 */
public class Server implements IServer{
    private List<TimedClient> clientesConectados;
    private DAOLogin daoLogin;
    private DAOUsuarios daoUsuarios;

    @Override
    public void registerUser(String name, String password) throws RemoteException {

    }

    @Override
    public IAuthToken login(IClient me, String name, String password) throws RemoteException {
        return null;
    }

    @Override
    public List<IProfile> getFriends(IAuthToken me) throws RemoteException {
        return null;
    }

    @Override
    public IClient getConnection(IAuthToken me, String name) throws RemoteException {
        return null;
    }

    @Override
    public void sendFriendshiptRequest(IAuthToken me, String name) throws RemoteException {

    }

    @Override
    public void sendUnFriendshiptRequest(IAuthToken me, String name) throws RemoteException {

    }

    @Override
    public List<String> searchUsers(IAuthToken me, String searchInput) throws RemoteException {
        return null;
    }
}
