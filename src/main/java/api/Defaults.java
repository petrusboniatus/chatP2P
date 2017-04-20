package api;

/**
 * Created by cout970 on 2017/04/08.
 */
public class Defaults {

    public static final String serverHost = "localhost";
    public static final int serverPort = 1099;
    public static final String serverName = "Server";
    public static final String clientURL = "rmi://" + serverHost + ":" + serverPort + "/" + serverName;

    public static final String databaseHost = getIP();
    public static final int databasePort = 27017;

    private static String getIP() {
        String username = System.getProperty("user.name");
        return username.contains("cout") ? "192.168.1.37" : "localhost";
    }
}
