package server.remote;

import api.IClient;
import api.IServer;
import server.daos.DAOLogin;
import server.daos.DAOUsuarios;
import server.daos.Profile;
import server.utils.RandomString;
import server.utils.Security;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@SuppressWarnings("unchecked")
public class Server extends UnicastRemoteObject implements IServer {

    private static final int ACTUALIZACIONES = 5; //Lo que tarda en borrarse del servidor si no hay renovación

    private ConcurrentHashMap<String, ClientData> clientesConectados;
    private DAOLogin daoLogin;
    private DAOUsuarios daoUsuarios;
    private RandomString random = new RandomString();



    private boolean checkConectado(IAuthToken he) {

        AuthToken sub = (AuthToken) he;
        if (clientesConectados.containsKey(sub.getNombreUsuario())) {
            ClientData c = clientesConectados.get(sub.getNombreUsuario());
            return c.getToken().equals(sub);
        } else {
            return false;
        }

    }

    private void notificarAmigos(Profile perfil){

        for (Profile profile : daoUsuarios.getAmigos(perfil) ){

            if (clientesConectados.contains(profile.getName())) {

                ClientData c = clientesConectados.get(profile.getName());
                try {
                    if (c.getTimeLeft() != 0)
                        c.getClient().notifyFriendListUpdates();
                } catch (RemoteException e) {
                    c.setTimeLeft(0);
                }

            }

        }
    }



    @Override
    public void imAlive(IAuthToken me) throws RemoteException {

        if (!checkConectado(me))
            throw new IllegalArgumentException("ya no estas logueado");

        clientesConectados.get( ((AuthToken)me).getNombreUsuario() ).setTimeLeft(ACTUALIZACIONES);
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
        if (!Security.checkPassword(password, clave))
            throw new IllegalArgumentException("La clave es erronea");


        Profile usuario = daoUsuarios.getUsuario(name);
        AuthToken nuevaAut = new AuthToken(usuario, random.getString());

        usuario.setOnline(true);
        clientesConectados.put(name, new ClientData(me, ACTUALIZACIONES, usuario, nuevaAut));
        daoUsuarios.actualizarUsuario(usuario);

        notificarAmigos(usuario);
        
        return nuevaAut;

    }

    @Override
    public void changePassword(IAuthToken me, String oldPass, String newPass) throws RemoteException {

        String clave = daoLogin.getCryptedPass( ((AuthToken)me).getNombreUsuario());

        if (!checkConectado(me))
            throw new IllegalArgumentException("No es un cliente logueado");
        if (!Security.checkPassword(oldPass, clave))
            throw new IllegalArgumentException("La contraseña no coincide");

        daoLogin.setCryptedPass( ((AuthToken)me).getNombreUsuario() , Security.encrypt(newPass) );

    }

    @Override
    public List<IProfile> getFriends(IAuthToken me) throws RemoteException {

        if (!checkConectado(me))
            throw new IllegalArgumentException("No es un cliente logueado");

        Profile perfilConectado = clientesConectados.get( ((AuthToken)me).getNombreUsuario() ).getPefil();

        return (List) daoUsuarios.getAmigos(perfilConectado);
    }

    @Override
    public IClient getConnection(IAuthToken me, String name) throws RemoteException {

        List<IProfile> amigos = getFriends(me);

        IProfile amigo = null;
        for (IProfile a : amigos)
            if (a.getName().equals(name))
                amigo = a;

        if (amigo == null)
            throw new IllegalArgumentException("Esa persona no es tu amigo");
        if (!amigo.isConnected())
            throw new IllegalArgumentException("Tu amigo no está conectado");
        if(!clientesConectados.containsKey(name))
            throw new IllegalArgumentException("Tu amigo no esta conectado");

        return clientesConectados.get(name).getClient();
    }

    @Override
    public void sendFriendshiptRequest(IAuthToken me, String name) throws RemoteException {

        if (!checkConectado(me))
            throw new IllegalArgumentException("Usuario no conectado");
        if (getFriends(me).contains(new Profile(name)))
            throw new IllegalArgumentException("Este persone ya está en la lista o listo de amigues");

        Profile enviador = this.clientesConectados.get(((AuthToken)me).getNombreUsuario()).getPefil();
        Profile receptor = new Profile(name);
        this.daoUsuarios.anhadirPeticion(enviador, receptor);

    }

    @Override
    public List<IProfile> getFriendShipRequest(IAuthToken me) throws RemoteException {

        if (!checkConectado(me))
            throw new IllegalArgumentException("usuario no conectado");

        Profile perfilConectado = clientesConectados.get(((AuthToken)me).getNombreUsuario()).getPefil();
        return (List) daoUsuarios.getPeticionesPendientes(perfilConectado);
    }

    @Override
    public void sendUnFriendshiptRequest(IAuthToken me, String name) throws RemoteException {

        if (!checkConectado(me))
            throw new IllegalArgumentException("usuario no conectado");
        if (!getFriends(me).contains(new Profile(name)))
            throw new IllegalArgumentException("Esta perosona no está en tu lista de amigos");

        Profile autenticado = clientesConectados.get( ((AuthToken)me).getNombreUsuario()).getPefil();
        daoUsuarios.borrarAmigo(autenticado, new Profile(name));
    }

    @Override
    public void acceptFriendPetition(IAuthToken me, IProfile amigo) throws RemoteException {

        if (!checkConectado(me))
            throw new IllegalArgumentException("usuario no conectado");
        if (!getFriendShipRequest(me).contains(amigo))
            throw new IllegalArgumentException("ese usuario no esta en la lista de peticiones");

        Profile aceptador = clientesConectados.get( ((AuthToken)me).getNombreUsuario() ).getPefil();
        daoUsuarios.borrarPeticion((Profile) amigo, aceptador);
        daoUsuarios.borrarPeticion(aceptador, (Profile) amigo);
        daoUsuarios.anhadirAmigo((Profile) amigo, aceptador);
        if(clientesConectados.containsKey(amigo.getName()))
            clientesConectados.get(amigo.getName()).getClient().notifyFriendListUpdates();
    }

    @Override
    public List<String> searchUsers(IAuthToken me, String searchInput) throws RemoteException {

        if (!checkConectado(me))
            throw new IllegalArgumentException("usuario no conectado");

        return daoUsuarios.buscarUsuarios(searchInput)
                .stream()
                .map(i -> i.getName())
                .collect(Collectors.toList());
    }


    public Server(DAOLogin daoLogin, DAOUsuarios daoUsuarios, ConcurrentHashMap clientesConectados) throws
            RemoteException {
        super();
        this.clientesConectados = clientesConectados;
        this.daoLogin = daoLogin;
        this.daoUsuarios = daoUsuarios;
    }
}
