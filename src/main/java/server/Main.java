package server;

import api.Defaults;
import server.remote.ServerManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
public class Main {

    public static void main(String[] args) {


//        String portNum;
//        String registryURL = "localhost";
//        int RMIPortNum = 0;
//
//        System.out.println("Introduce el numero de puerto");
//
//        portNum = (readLine()).trim();
//        RMIPortNum = Integer.parseInt(portNum);

        ServerManager servidor = new ServerManager(Defaults.serverPort, Defaults.serverHost);

        servidor.inicializar();

        System.out.println("Escribe <salir> para salir");
        String salir = "";

        while (!salir.equals("salir"))
            salir = readLine();

        servidor.apagar();
    }

    private static String readLine() {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        try {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
