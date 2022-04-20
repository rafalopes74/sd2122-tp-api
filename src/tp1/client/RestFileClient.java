package tp1.client;


import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tp1.api.service.rest.RestFiles;
import tp1.api.service.rest.RestUsers;
import tp1.server.resources.JavaDirectory;

import java.io.InputStream;
import java.net.URI;
import java.util.logging.Logger;

public class RestFileClient extends RestClient implements RestFiles {
    final WebTarget target;
    private static final Logger Log = Logger.getLogger(JavaDirectory.class.getName());

    public RestFileClient(URI serverURI) {
        super( serverURI );
        target = client.target( serverURI ).path( RestFiles.PATH );


    }

    @Override
    public void writeFile(String fileId, byte[] data, String token) {

        super.reTry( () -> {
            clt_writeFile( fileId, data,token );
            return null;
        });
        Log.info("write file res file client");

    }

    @Override
    public void deleteFile(String fileId, String token) {
        super.reTry( () -> {
            clt_deleteFile( fileId, token );
            return null;
        });
    }

    @Override
    public byte[] getFile(String fileId, String token) {
        return super.reTry( () -> clt_getFile( fileId, token));
    }


    private void clt_writeFile(String fileId, byte[] data, String token){
        Log.info("clt write file res file client");
        Response f = target.path(fileId)
                .queryParam("token", token).request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));

        Log.info("clt write file res file client 2");
        if( f.getStatus() == Response.Status.NO_CONTENT.getStatusCode() ) {
            System.out.println("Deu certo: " + f.getStatus());
        }
        else {
            System.out.println("Error, HTTP error status: " + f.getStatus());
        }
    }


    private void clt_deleteFile(String fileId, String token) {
        Response f = target.path(fileId)
                .queryParam("token", token).request()
                .delete();

        if( f.getStatus() == Response.Status.OK.getStatusCode() && f.hasEntity() ) {
            System.out.println("Deu certo: " + f.getStatus());

        }
        else {
            System.out.println("Error, HTTP error status: " + f.getStatus());
        }
    }

    private byte[] clt_getFile(String fileId, String token) {

        Log.info("entrou dentro do getFile");

        Response f = target.path(fileId)
                .queryParam("token", token).request()
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .get();

        if( f.getStatus() == Response.Status.OK.getStatusCode() && f.hasEntity() ) {
            System.out.println("Deu certo: " + f.getStatus());

            return f.readEntity(byte[].class);
        }
        else {
            Log.info("vai rebentar");
            System.out.println("Error, HTTP error status: " + f.getStatus());
            return null;
        }

    }


}
