package tp1.server;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import tp1.server.resources.DirectoryResources;

import tp1.server.util.CustomLoggingFilter;
import tp1.server.util.Discovery;
import tp1.server.util.GenericExceptionMapper;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.logging.Logger;

public class DirectoryServer {

    private static Logger Log = Logger.getLogger(DirectoryServer.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s\n");
    }

    public static final int PORT = 8080;
    public static final String SERVICE = "directory";
    public static final String SERVER_URI_REST = "http://%s:%s/rest";


    public static void main(String[] args) {
        try {

            ResourceConfig config = new ResourceConfig();
            config.register(DirectoryResources.class);
            //config.register(CustomLoggingFilter.class);
            //config.register(GenericExceptionMapper.class);

            String ip = InetAddress.getLocalHost().getHostAddress();
            String serverURI = String.format(SERVER_URI_REST, ip, PORT);
            JdkHttpServerFactory.createHttpServer( URI.create(serverURI), config);

            Log.info(String.format("%s Server ready @ %s\n",  SERVICE, serverURI));

            Discovery d = Discovery.getInstance();
            d.announce("directory", serverURI);
            d.listener();
            //More code can be executed here...
        } catch( Exception e) {
            Log.severe(e.getMessage());
        }
    }
}

