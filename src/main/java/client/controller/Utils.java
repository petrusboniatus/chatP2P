package client.controller;

import javafx.application.Platform;

/**
 * Created by cout970 on 4/30/17.
 */
public class Utils {

    public static void runAsync(Runnable task) {
        Thread a = new Thread(task);
        a.setDaemon(false);
        a.start();
    }
}
