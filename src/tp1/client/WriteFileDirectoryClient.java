package tp1.client;

import tp1.api.User;
import util.Debug;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriteFileDirectoryClient {

    private static Logger Log = Logger.getLogger(WriteFileDirectoryClient.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    public static void main(String[] args) throws IOException {

        Debug.setLogLevel( Level.FINE, Debug.SD2122 );

        if (args.length != 5) {
            System.err.println("Use: java sd2122.aula3.clients.WriteFileDirectoryClient url filename data userId password");
            return;
        }

        String serverUrl = args[0];
        String filename = args[1];
        byte[] data = args[2].getBytes();
        String userId = args[3];
        String password = args[4];

        Log.info("Sending request to server.");

        var result = new RestDirectoryClient(URI.create(serverUrl)).writeFile(filename,data,userId,password);
        System.out.println("Result: " + result);
    }
}
