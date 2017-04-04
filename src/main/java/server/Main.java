package server;

import server.remote.Server;
import server.remote.ServerManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
public class Main {

    public static void main(String[] args) {


        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        String portNum;
        String registryURL = "localhost";
        int RMIPortNum = 0;

        System.out.println("Introduce el numero de puerto");
        try {
            portNum = (br.readLine()).trim();
            RMIPortNum = Integer.parseInt(portNum);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ServerManager servidor = new ServerManager(RMIPortNum, registryURL);

        servidor.inicializar();

        String salir = "";
        try {
            System.out.println("Escribe <salir> para salir");
            while (!salir.equals("salir"))
                salir = br.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            servidor.apagar();
        }

    }
}
