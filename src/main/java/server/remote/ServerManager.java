package server.remote;

import server.daos.DAOLogin;
import server.daos.DAOUsuarios;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pedro on 4/04/17.
 */
public class ServerManager {

    private int portNum;
    private String urlRegistro;

    private DAOLogin daoLogin;
    private DAOUsuarios daoUsuarios;

    private ConcurrentHashMap<AuthToken, ClientData> clientesConectados;
    private Thread hiloLimpiador;
    private LimpiadorClientesConectados limpiador;

    private Server server;


    public ServerManager(int portNum, String urlRegistro) {
        this.portNum = portNum;
        this.urlRegistro = urlRegistro;
    }

    public void inicializar() {


        clientesConectados = new ConcurrentHashMap<>();
        daoLogin = new DAOLogin();
        daoLogin.inicializar();
        daoUsuarios = new DAOUsuarios();
        daoUsuarios.inicializar();

        limpiador = new LimpiadorClientesConectados(clientesConectados, daoUsuarios);
        hiloLimpiador = new Thread(limpiador);

        try {

            server = new Server(daoLogin, daoUsuarios, clientesConectados);
            startRegistry(portNum);
            Naming.rebind("rmi://" + urlRegistro +":"+portNum+ "/Server", server);
            System.out.println("Callback Server ready.");

            hiloLimpiador.start();
        } catch (Exception re) {
            System.out.println("Exception in HelloServer.main: " + re);
        }

    }

    public void apagar() {


        try {

            Naming.unbind("rmi://" + urlRegistro +":"+portNum+ "/Server");
            System.out.println("La desconexion se ha relizado de manera correcta");

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            limpiador.finalizarHilo();
            hiloLimpiador.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        limpiador.desconectarClientes();

    }


    private static void startRegistry(int RMIPortNum) throws RemoteException {

        try {

            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list();

        } catch (RemoteException e) {
            Registry registry = LocateRegistry.createRegistry(RMIPortNum);
        }
    }

}
