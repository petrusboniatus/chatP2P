package client.newView;

import api.Defaults;
import client.Client;
import client.ServerConnection;
import client.controller.Controller;
import client.controller.ViewState;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * Created by cout970 on 4/30/17.
 */
public enum ViewHandler {
    LOADING(new Loading()),
    LOGIN(new Login()),
    MENU(new Menu()),
    CHANGE_PASSWORD(new ChangePassword()),
    SEARCH_USERS(new SearchUsers()),
    CHAT(new Chat()),
    FRIENDSHIP_REQUESTS(new FriendshipPetitions());


    private static JFrame window = new JFrame();
    private static Controller controller = new Controller();
    private static ViewHandler currentView = null;
    private View view;

    ViewHandler(View view){
        this.view = view;
    }

    public static void init(){
        for (ViewHandler viewHandler : ViewHandler.values()) {
            viewHandler.view.setController(controller);
        }

        try {
            UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[1].getClassName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(800, 600);
        window.setVisible(true);
        connectToServer();
    }

    public void show(){
        currentView = this;
        window.setContentPane(view.getRoot());
        window.show();
        view.onLoad();
    }

    public static void connectToServer() {
        LOADING.show();
        Client client;

        try {
            client = new Client(controller);
        } catch (RemoteException e) {
            ((Loading)LOADING.view).showError("Error interno, no se pudo crear la clase Cliente");
            e.printStackTrace();
            return;
        }

        ServerConnection handler = new ServerConnection(client, Defaults.clientURL);

        if (handler.getServer() == null) {
            ((Loading)LOADING.view).showError("Error al connectar con el servidor");
            return;
        }
        controller.setHandler(handler);

        LOGIN.show();
    }

    public static View getCurrentView() {
        return currentView.view;
    }
}
