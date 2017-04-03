package client;

import java.rmi.RemoteException;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
public class Main {

    public static void main(String[] args) {
        Client client = null;

        try {
            client = new Client();
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(1);
        }

        ServerConnection handler = new ServerConnection(client, "rmi://localhost:1099/server");

        try {
            handler.getServer();
        } catch (Exception e) {
            System.out.println("Error al connectar con el servidor");
            System.exit(1);
        }

        boolean success = handler.tryLogin("", "");

        if (!success){
            System.out.println("Error de login");
            System.exit(1);
        }

        FriendManager manager = new FriendManager(handler);
        client.setManager(manager);

        System.out.println(manager.getProfiles());
    }
}
