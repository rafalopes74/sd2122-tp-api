package tp1.client;

import util.Debug;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriteFileFileClient {

    private static Logger Log = Logger.getLogger(WriteFileFileClient.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    public static void main(String[] args) throws IOException {

        Debug.setLogLevel(Level.FINE, Debug.SD2122);

        if (args.length != 4) {
            System.err.println("Use: java sd2122.aula3.clients.WriteFileFileClient url fileId data token ");
            return;
        }

        String serverUrl = args[0];
        String filename = args[1];
        byte[] data = args[2].getBytes();
        String token = args[3];

        Log.info("Sending request to server.");
        new RestFileClient(URI.create(serverUrl)).writeFile(filename, data, token);

    }
}
