import api.IClient;
import api.IP2P;
import api.IServer;
import client.Client;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.junit.Assert;
import org.junit.Test;
import server.daos.DAOLogin;
import server.daos.DAOUsuarios;
import server.remote.ClientData;
import server.remote.LimpiadorClientesConectados;
import server.remote.Server;

import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.fail;

/**
 * Created by pedro on 20/04/17.
 */
public class ServerTest {


    Server server = null;
    DAOLogin daoLogin;
    DAOUsuarios daoUsuarios;

    ConcurrentHashMap<String, ClientData> clientesConectados;
    Thread hiloLimpiador;
    LimpiadorClientesConectados limpiador;

    class ClientTest implements IClient{

        String nomrbe;
        boolean notificado = false;
        ClientTest(String nombre){
            this.nomrbe = nombre;
        }

        @Override
        public void notifyFriendListUpdates() throws RemoteException {
            System.out.print(nomrbe + " ha sido notificado\n");
            notificado = true;
        }

        @Override
        public IP2P getP2P() throws RemoteException {
            return null;
        }

        @Override
        public void startConnections(IP2P connection, IServer.IAuthToken token) {

        }
    }

    public ServerTest(){
        clientesConectados = new ConcurrentHashMap<>();
        daoLogin = new DAOLogin();
        daoLogin.inicializar();
        daoUsuarios = new DAOUsuarios();
        daoUsuarios.inicializar();

        limpiador = new LimpiadorClientesConectados(clientesConectados, daoUsuarios);
        hiloLimpiador = new Thread(limpiador);

        try {
            server = new Server(daoLogin, daoUsuarios, clientesConectados);
            hiloLimpiador.start();
        }catch (Exception e){e.printStackTrace();}
    }

    private void  close(){
        limpiador.finalizarHilo();
        try {
            hiloLimpiador.join();
        }catch (Exception e){
            e.printStackTrace();
        };
        limpiador.desconectarClientes();

    }

    private void dropTables(){
        MongoClient mongoClient = new MongoClient();
        DB db = mongoClient.getDB("usersBD");

        db.dropDatabase();
    }

    private void insertUsers(){
        try
        {


            server.registerUser("usuario1", "usr");
            server.registerUser("usuario2","usr" );

            Client c1 = new Client(null);
            Client c2 = new Client(null);

            IServer.IAuthToken t1 = server.login(c1,"usuario1", "usr" );
            IServer.IAuthToken t2 = server.login(c2,"usuario2", "usr" );

            server.changePassword(t1, "usr", "usr2");

            server.login(c1,"usuario1","usr2");


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Test
    public void userFriendShips(){

        insertUsers();
        try {

            IClient c1 = new ClientTest("usuario1");
            IClient c2 = new ClientTest("usuario2");

            IServer.IAuthToken t1 = server.login(c1, "usuario1", "usr2");
            IServer.IAuthToken t2 = server.login(c2, "usuario2", "usr");

            List<String> usuarios  = server.searchUsers(t1,"usuario2");

            Assert.assertEquals("usuario2", usuarios.get(0));

            server.sendFriendshiptRequest(t1,usuarios.get(0));

            List<IServer.IProfile> peticiones = server.getFriendShipRequest(t2);

            Assert.assertEquals(peticiones.get(0).getName(), "usuario1");

            server.acceptFriendPetition(t2, peticiones.get(0));

            Assert.assertEquals(1, server.getFriends(t1).size());
            Assert.assertEquals(1, server.getFriends(t2).size());
            Assert.assertEquals(0, server.getFriendShipRequest(t1).size());
            Assert.assertEquals(0, server.getFriendShipRequest(t2).size());


            IClient c1Requested = server.getConnection(t2, "usuario1");

            Assert.assertEquals(c1, c1Requested);

            server.sendUnFriendshiptRequest(t1, "usuario2");

            Assert.assertEquals(0, server.getFriends(t1).size());
            Assert.assertEquals(0, server.getFriends(t2).size());
            Assert.assertEquals(0, server.getFriendShipRequest(t1).size());
            Assert.assertEquals(0, server.getFriendShipRequest(t2).size());

            limpiador.finalizarHilo();
            hiloLimpiador.join();
            limpiador.desconectarClientes();

        }catch (Exception e){
            e.printStackTrace();
        }

        close();
        dropTables();
    }


    @Test
    public void limipiador(){
        insertUsers();

        try {


            IClient c1 = new ClientTest("usuario1");
            IClient c2 = new ClientTest("usuario2");

            IServer.IAuthToken t1 = server.login(c1, "usuario1", "usr2");
            IServer.IAuthToken t2 = server.login(c2, "usuario2", "usr");

            server.sendFriendshiptRequest(t1,"usuario2");


            List<IServer.IProfile> peticiones = server.getFriendShipRequest(t2);
            server.acceptFriendPetition(t2,peticiones.get(0));
            ((ClientTest)c1).notificado = false;

            System.out.println(server.getFriends(t1));
            System.out.println(server.getFriends(t2));


            Thread.sleep(4000);
            server.imAlive(t1);
            IClient c = server.getConnection(t1,"usuario2");

            System.out.println(server.getFriends(t1));
            System.out.println(server.getFriends(t2));


            Thread.sleep(4000);

            System.out.println(server.getFriends(t1));


            try{
                IClient c_1 = server.getConnection(t1,"usuario2");
                fail("Debería dar una execpición");
            }catch (IllegalArgumentException e){/*España va bién*/}

            System.out.println(server.getFriends(t1));
            Assert.assertTrue( ((ClientTest)c1).notificado);
            List<IServer.IProfile> friends = server.getFriends(t1);
            Assert.assertFalse(friends.get(0).isConnected());

            close();

        }catch (Exception e){
            e.printStackTrace();
        }
        close();
        dropTables();
    }


    @Test
    public void notification(){
        insertUsers();

        try {
            IClient c1 = new ClientTest("usuario1");
            IClient c2 = new ClientTest("usuario2");

            IServer.IAuthToken t1 = server.login(c1, "usuario1", "usr2");
            IServer.IAuthToken t2 = server.login(c2, "usuario2", "usr");

            server.sendFriendshiptRequest(t1, "usuario2");


            List<IServer.IProfile> peticiones = server.getFriendShipRequest(t2);
            server.acceptFriendPetition(t2, peticiones.get(0));
            ((ClientTest) c1).notificado = false;

            Thread.sleep(4000);
            server.imAlive(t1);

            Thread.sleep(4000);
            server.imAlive(t1);
            t2 = server.login(c2, "usuario2", "usr");

            Assert.assertTrue(((ClientTest) c1).notificado);


        }catch (Exception e){
            e.printStackTrace();
        }

        close();
        dropTables();
    }


}
