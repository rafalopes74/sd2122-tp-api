package tp1.client;

import util.Debug;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeleteUserClient {
    private static Logger Log = Logger.getLogger(DeleteUserClient.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    public static void main(String[] args) throws IOException {

        Debug.setLogLevel( Level.FINE, Debug.SD2122 );

        if (args.length != 3) {
            System.err.println("Use: java sd2122.aula3.clients.DeleteUserClient url userId password");
            return;
        }

        String serverUrl = args[0];
        String userId = args[1];
        String password = args[2];


        Log.info("Sending request to server.");

        var result = new RestUsersClient(URI.create(serverUrl)).deleteUser(userId,password);
        System.out.println("Result: " + result);
    }
}
