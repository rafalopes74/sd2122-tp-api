package tp1.client;

import jakarta.inject.Singleton;
import org.glassfish.jersey.server.Uri;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Files;
import tp1.api.service.util.Users;
import tp1.server.util.Discovery;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
@Singleton
public class ClientFactory {



    public static Users getClient() throws URISyntaxException, MalformedURLException {
        var serverUri = Discovery.getInstance().knownUrisOf("users").toArray()[0];

        var s = serverUri.toString().split("/");

        if (s[s.length-1].equals("rest")) {
            return new RestUsersClient((URI) serverUri);
        }else return new SoapUserClient((URI) serverUri);
    }

    public static Files getFiles(URI url, URI uri) throws URISyntaxException, MalformedURLException {

        if (url.toString().contains("rest")) {
            return new RestFilesClient(uri);
        }else return new SoapFilesClient(uri);
    }

    public static Directory getDirectory() throws URISyntaxException, MalformedURLException {
        var serverUri = Discovery.getInstance().knownUrisOf("directory").toArray()[0];

        //throw new RuntimeException(serverUri.toString());
        var s = serverUri.toString().split("/");

        if (s[s.length-1].equals("rest")) {
            return new RestDirectoryClient((URI) serverUri);
        }else return new SoapDirectoryClient((URI) serverUri);


    }

}
