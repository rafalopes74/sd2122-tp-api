package tp1.server;

import jakarta.xml.ws.Endpoint;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import tp1.server.resources.UserResources;
import tp1.server.resources.UserWebService;
import tp1.server.util.Discovery;


import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SoapUsersServer {

    private static Logger Log = Logger.getLogger(SoapUsersServer.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s\n");
    }

    public static final int PORT = 8080;
    public static final String SERVICE = "users";
    private static final String SERVER_URI_SOAP =  "http://%s:%s/soap";

    public static void main(String[] args) {
        try {

            System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
            System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
            System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
            System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");

            Log.setLevel(Level.INFO);

            String ip = InetAddress.getLocalHost().getHostAddress();
            String serverURI = String.format(SERVER_URI_SOAP, ip, PORT);


            Endpoint.publish(serverURI.replace(ip, "0.0.0.0"), new UserWebService());

            Discovery d = Discovery.getInstance();
            d.announce("users", serverURI);
            d.listener();

            //More code can be executed here...
        } catch( Exception e) {
            Log.severe(e.getMessage());
        }
    }
}
