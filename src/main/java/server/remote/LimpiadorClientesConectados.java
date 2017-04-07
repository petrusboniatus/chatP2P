package server.remote;

import server.daos.DAOUsuarios;
import server.daos.Profile;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pedro on 4/04/17.
 */
public class LimpiadorClientesConectados implements Runnable {

    private static final int TIEMPO_ENTRE_LIMPIEZA = 5_000;

    private ConcurrentHashMap<String, ClientData> mapaALimpiar;
    private DAOUsuarios daoUsuarios;

    private boolean finalizarHilo;

    public LimpiadorClientesConectados(ConcurrentHashMap<String, ClientData> mapaALimpiar, DAOUsuarios daoUsuarios) {
        this.mapaALimpiar = mapaALimpiar;
        this.finalizarHilo = false;
        this.daoUsuarios = daoUsuarios;
    }

    public void finalizarHilo() {
        this.finalizarHilo = true;
    }

    public void desconectarClientes() {
        for (ClientData entrada : mapaALimpiar.values()) {
            entrada.getPefil().setOnline(false);
            daoUsuarios.actualizarUsuario(entrada.getPefil());
        }
    }

    @Override
    public void run() {
        while (!finalizarHilo) {
            try {
                Thread.sleep(TIEMPO_ENTRE_LIMPIEZA);

                for (Map.Entry<String, ClientData> entrada : mapaALimpiar.entrySet()) {

                    entrada.getValue().setTimeLeft(entrada.getValue().getTimeLeft() - 1);

                    if (entrada.getValue().getTimeLeft() == 0) {

                        entrada.getValue().getPefil().setOnline(false);
                        daoUsuarios.actualizarUsuario(entrada.getValue().getPefil());


                        try {
                            for (Profile profile : daoUsuarios.getAmigos(entrada.getValue().getPefil())) {

                                mapaALimpiar.get(profile.getName()).getClient().notifyFriendListUpdates();
                            }
                        } catch (RemoteException e) {
                            //Ingonre this
                        }

                        mapaALimpiar.remove(entrada.getKey());
                    }

                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
