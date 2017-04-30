package client.controller;

import javafx.application.Platform;

/**
 * Created by cout970 on 4/30/17.
 */
public class Utils {

    public static void runAsync(Runnable task) {
        System.out.println(Platform.isFxApplicationThread());
        Thread a = new Thread(() -> {
            System.out.println(Platform.isFxApplicationThread());
            task.run();
        });
        a.setDaemon(false);
        a.start();
    }
}
