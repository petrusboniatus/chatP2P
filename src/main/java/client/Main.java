package client;

import client.controller.GUI;

import java.rmi.RemoteException;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
public class Main {

    public static void main(String[] args) {
        GUI gui = new GUI();

        gui.init();

        Client client;

        try {
            client = new Client();
        } catch (RemoteException e) {
            gui.getController().showError("Error interno, no se pudo crear la clase Cliente");
            return;
        }

        ServerHandler handler = new ServerHandler(client, "rmi://localhost:1099/Server");

        if(handler.getServer() == null){
            gui.getController().showError("Error al connectar con el servidor");
            return;
        }

        client.setManager(gui.getController());
        gui.getController().setServerHandler(handler);
    }
}
