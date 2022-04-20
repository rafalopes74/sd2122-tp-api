package tp1.client;

import util.Debug;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LsFileDirectoryClient {

    private static final Logger Log = Logger.getLogger(LsFileDirectoryClient.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    public static void main(String[] args) throws IOException {
        Debug.setLogLevel(Level.FINE, Debug.SD2122);

        if (args.length != 5) {
            System.err.println("Use: java sd2122.aula3.clients.LsFileDirectoryClient url userId password");
            return;
        }

        String serverUrl = args[0];
        String userId = args[1];
        String password = args[2];

        Log.info("Sending request to server.");

        var result = new RestDirectoryClient(URI.create(serverUrl)).lsFile(userId, password);
        System.out.println("Result: " + result);
    }
}
