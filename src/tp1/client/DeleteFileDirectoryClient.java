package tp1.client;

import util.Debug;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeleteFileDirectoryClient {

    private static Logger Log = Logger.getLogger(DeleteFileDirectoryClient.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    public static void main(String[] args) throws IOException {
        Debug.setLogLevel( Level.FINE, Debug.SD2122 );

        if (args.length != 4) {
        System.err.println("Use: java sd2122.aula3.clients.DeleteFileDirectoryClient url filename data userId password");
        return;
    }

    String serverUrl = args[0];
    String filename = args[1];
    String userId = args[2];
    String password = args[3];

    Log.info("Sending request to server.");

    new RestDirectoryClient(URI.create(serverUrl)).deleteFile(filename, userId,password);

}
}
