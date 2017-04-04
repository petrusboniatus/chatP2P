package server.remote;

import server.daos.DAOUsuarios;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pedro on 4/04/17.
 */
public class LimpiadorClientesConectados implements Runnable {

    private static final int TIEMPO_ENTRE_LIMPIEZA = 30_000;

    private ConcurrentHashMap<AuthToken, TimedClient> mapaALimpiar;
    private DAOUsuarios daoUsuarios;

    private boolean cerrarAplicacion;

    public LimpiadorClientesConectados(ConcurrentHashMap<AuthToken, TimedClient> mapaALimpiar, DAOUsuarios daoUsuarios) {
        this.mapaALimpiar = mapaALimpiar;
        this.cerrarAplicacion = false;
        this.daoUsuarios = daoUsuarios;
    }

    public void cerraraplicacion() {
        this.cerrarAplicacion = true;
    }

    public void desconectarClientes() {

        for (TimedClient entrada : mapaALimpiar.values()) {
            entrada.getPefil().setOnline(false);
            daoUsuarios.actualizarUsuario(entrada.getPefil());
        }
    }

    @Override
    public void run() {
        while (!cerrarAplicacion) {
            try {
                Thread.sleep(TIEMPO_ENTRE_LIMPIEZA);

                for (Map.Entry<AuthToken, TimedClient> entrada : mapaALimpiar.entrySet()) {

                    entrada.getValue().setTimeLeft(entrada.getValue().getTimeLeft() - 1);

                    if (entrada.getValue().getTimeLeft() == 0) {

                        entrada.getValue().getPefil().setOnline(false);
                        daoUsuarios.actualizarUsuario(entrada.getValue().getPefil());


                        try {
                            entrada.getValue().getClient().notifyFriendListUpdates();
                        } catch (RemoteException e) {
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
