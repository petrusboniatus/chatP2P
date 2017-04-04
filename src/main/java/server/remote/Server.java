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
import java.util.Optional;

/**
 * Created by pedro on 4/04/17.
 */
@SuppressWarnings("unchecked")
public class Server implements IServer {

    private HashMap<AuthToken, TimedClient> clientesConectados;
    private DAOLogin daoLogin;
    private DAOUsuarios daoUsuarios;
    private RandomString random;


    @Override
    public void imAlive(IAuthToken me) throws RemoteException {
        if(!clientesConectados.containsKey(me))
            throw new IllegalArgumentException("No es un cliente logueado");
    }

    @Override
    public void registerUser(String name, String password) throws RemoteException {

        if (daoUsuarios.existeUsuario(name))
            throw new IllegalArgumentException("Ese usuario ya existe");


        Profile nuevoUsuario = new Profile();

        nuevoUsuario.setName(name);
        daoUsuarios.insertarUsuario(nuevoUsuario);
        daoLogin.setCryptedPass(nuevoUsuario.getName(), Security.encrypt(password));

    }

    @Override
    public IAuthToken login(IClient me, String name, String password) throws RemoteException {


        String clave = daoLogin.getCryptedPass(name);

        if (clave == null)
            throw new IllegalArgumentException("El usuario no está registrado");
        if (Security.checkPassword(password, clave))
            throw new IllegalArgumentException("La clave es erronea");


        Profile usuario = daoUsuarios.getUsuario(name);
        AuthToken nuevaAut = new AuthToken(usuario, random.getString());

        usuario.setOnline(true);
        clientesConectados.put(nuevaAut, new TimedClient(me, 1,usuario));
        daoUsuarios.actualizarUsuario(usuario);

        return nuevaAut;

    }

    @Override
    public List<IProfile> getFriends(IAuthToken me) throws RemoteException {


        if (!this.clientesConectados.containsKey(me))
            throw new IllegalArgumentException("el usuario no existe");


        return (List) daoUsuarios.getAmigos(clientesConectados.get(me).getPefil());
    }

    @Override
    public IClient getConnection(IAuthToken me, String name) throws RemoteException {

        List<IProfile> amigos = getFriends(me);

        IProfile amigo = null;
        for(IProfile a: amigos)
            if(a.getName().equals(name))
                amigo = a;

        if(amigo==null)
            throw new IllegalArgumentException("Esa persona no es tu amigo");
        if(!amigo.isConnected())
            throw new IllegalArgumentException("Tu amigo no está conectado");

        final IProfile amigoFinal = amigo;
        Optional<AuthToken> token = clientesConectados.keySet()
                .parallelStream()
                .filter(authToken -> authToken.getNombreUsuario().equals(amigoFinal.getName()))
                .findFirst();

        return clientesConectados.get(token.get()).getClient();

    }

    @Override
    public void sendFriendshiptRequest(IAuthToken me, String name) throws RemoteException {

        if(!this.clientesConectados.containsKey(me))
            throw new IllegalArgumentException("usuario no conectado");
        if(getFriends(me).contains(new Profile(name)))
            throw new IllegalArgumentException("Este persone ya está en la lista o listo de amigues");


        Profile enviador = this.clientesConectados.get(me).getPefil();
        Profile receptor = new Profile(name);

        this.daoUsuarios.anhadirPeticion(enviador,enviador);

    }

    @Override
    public List<IProfile> getFriendShipRequest(IAuthToken me) throws RemoteException {

        if(!this.clientesConectados.containsKey(me))
            throw new IllegalArgumentException("usuario no conectado");

        return (List)daoUsuarios.getPeticionesPendientes(this.clientesConectados.get(me).getPefil());
    }

    @Override
    public void sendUnFriendshiptRequest(IAuthToken me, String name) throws RemoteException {

        if(!this.clientesConectados.containsKey(me))
            throw new IllegalArgumentException("usuario no conectado");
        if(getFriends(me).contains(new Profile(name)))
            throw new IllegalArgumentException("Esta perosona no está en tu lista de amigos");

        daoUsuarios.borrarAmigo(this.clientesConectados.get(me).getPefil(),new Profile(name));
    }

    @Override
    public void acceptFriendPetition(IAuthToken me, IProfile amigo) throws RemoteException {

        if(!this.clientesConectados.containsKey(me))
            throw new IllegalArgumentException("usuario no conectado");
        if(!getFriendShipRequest(me).contains(amigo))
            throw new IllegalArgumentException("ese usuario no esta en la lista de peticiones");

        Profile aceptador = this.clientesConectados.get(me).getPefil();


        daoUsuarios.borrarPeticion((Profile)amigo,aceptador);
        daoUsuarios.anhadirAmigo((Profile)amigo,aceptador);
    }

    @Override
    public List<String> searchUsers(IAuthToken me, String searchInput) throws RemoteException {

        if(!this.clientesConectados.containsKey(me))
            throw new IllegalArgumentException("usuario no conectado");

        return (List)daoUsuarios.buscarUsuarios(searchInput);
    }


    public Server() {
        clientesConectados = new HashMap<>();
        daoLogin = new DAOLogin();
        daoLogin.inicializar();
        daoUsuarios = new DAOUsuarios();
        daoUsuarios.inicializar();
    }
}
