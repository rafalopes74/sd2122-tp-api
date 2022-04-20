package tp1.client;

import util.Debug;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShareFileDirectoryClient {

    private static final Logger Log = Logger.getLogger(ShareFileDirectoryClient.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    public static void main(String[] args) throws IOException {
        Debug.setLogLevel(Level.FINE, Debug.SD2122);

        if (args.length != 5) {
            System.err.println("Use: java sd2122.aula3.clients.DeleteFileDirectoryClient url filename userId shareUserId password");
            return;
        }

        String serverUrl = args[0];
        String filename = args[1];
        String userId = args[2];
        String shareUserId = args[3];
        String password = args[4];

        Log.info("Sending request to server.");

        new RestDirectoryClient(URI.create(serverUrl)).shareFile(filename, userId, shareUserId, password);

    }
}
