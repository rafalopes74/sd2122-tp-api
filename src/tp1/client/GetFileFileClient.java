package tp1.client;

import util.Debug;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetFileFileClient {

    private static Logger Log = Logger.getLogger(GetFileFileClient.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    public static void main(String[] args) throws IOException {

        Debug.setLogLevel(Level.FINE, Debug.SD2122);

        if (args.length != 3) {
            System.err.println("Use: java sd2122.aula3.clients.GetFileFileClient url fileId token ");
            return;
        }

        String serverUrl = args[0];
        String fileId = args[1];
        String token = args[2];

        Log.info("Sending request to server.");
        var result = new RestFileClient(URI.create(serverUrl)).getFile(fileId, token);
        System.out.println("result:" + result);
    }
}
