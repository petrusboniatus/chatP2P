package server.remote;

import api.IClient;
import api.IServer;
import server.daos.DAOLogin;
import server.daos.DAOUsuarios;
import server.daos.Profile;
import server.utils.RandomString;
import server.utils.Security;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pedro on 4/04/17.
 */
public class Server implements IServer{
    private HashMap<AuthToken, TimedClient> clientesConectados;
    private DAOLogin daoLogin;
    private DAOUsuarios daoUsuarios;
    private RandomString random;


    @Override
    public void registerUser(String name, String password) throws RemoteException {

        if(daoUsuarios.existeUsuario(name))
            throw new IllegalArgumentException("Ese usuario ya existe");

        Profile nuevoUsuario = new Profile();
        nuevoUsuario.setName(name);
        daoUsuarios.insertarUsuario(nuevoUsuario);
        daoLogin.setCryptedPass(nuevoUsuario.getName(),Security.encrypt(password));

    }

    @Override
    public IAuthToken login(IClient me, String name, String password) throws RemoteException {

        String clave = daoLogin.getCryptedPass(name);

        if(clave==null || clave == "")
            throw new IllegalArgumentException("El usuario no está registrado");
        if(Security.checkPassword(password,clave))
            throw new IllegalArgumentException("La clave es erronea");


        Profile usuario = daoUsuarios.getUsuario(name);
        AuthToken nuevaAut = new AuthToken(usuario,random.getString());
        usuario.setOnline(true);
        clientesConectados.put(nuevaAut,new TimedClient(me,1));
        daoUsuarios.actualizarUsuario(usuario);

        return nuevaAut;

    }

    @Override
    public List<IProfile> getFriends(IAuthToken me) throws RemoteException {
        String usuarioCadena = ((AuthToken)me).getNombreUsuario();
        if(!this.clientesConectados.containsKey(me))
            throw new IllegalArgumentException("el usuario no existe");

        Profile usuario = daoUsuarios.getUsuario(usuarioCadena);
        return (List)daoUsuarios.getAmigos(usuario);
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


    public Server(){
        clientesConectados = new HashMap<>();
        daoLogin = new DAOLogin();
        daoLogin.inicializar();
        daoUsuarios = new DAOUsuarios();
        daoUsuarios.inicializar();
    }
}
