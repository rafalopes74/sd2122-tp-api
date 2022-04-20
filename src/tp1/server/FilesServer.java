package tp1.server;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import tp1.api.service.rest.FileResources;
import tp1.server.resources.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.logging.Logger;

public class FilesServer {

        private static Logger Log = Logger.getLogger(tp1.server.FilesServer.class.getName());

        static {
            System.setProperty("java.net.preferIPv4Stack", "true");
            System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s\n");
        }

        public static final int PORT = 8080;
        public static final String SERVICE = "files";
        private static final String SERVER_URI_REST = "http://%s:%s/rest";

        public static void main(String[] args) {
            try {
                ResourceConfig config = new ResourceConfig();
                config.register(FileResources.class);
                config.register(CustomLoggingFilter.class);
                config.register(GenericExceptionMapper.class);

                String ip = InetAddress.getLocalHost().getHostAddress();
                String serverURI = String.format(SERVER_URI_REST, ip, PORT);
                JdkHttpServerFactory.createHttpServer( URI.create(serverURI), config);

                Log.info(String.format("%s Server ready @ %s\n",  SERVICE, serverURI));

                Discovery d = Discovery.getInstance();
                d.announce("files", serverURI);
                d.listener();

                //More code can be executed here...
            } catch( Exception e) {
                Log.severe(e.getMessage());
            }
        }
}

