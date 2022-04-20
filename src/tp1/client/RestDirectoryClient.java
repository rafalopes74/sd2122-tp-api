package tp1.client;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.rest.RestUsers;
import tp1.server.resources.JavaDirectory;


import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

public class RestDirectoryClient extends RestClient implements RestDirectory {
    private static final Logger Log = Logger.getLogger(RestDirectoryClient.class.getName());

    final WebTarget target;

    RestDirectoryClient( URI serverURI ) {
        super( serverURI );
        target = client.target( serverURI ).path( RestDirectory.PATH );
    }
    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) {
        return super.reTry( () -> {
            return clt_writeFile( filename,data,userId,password );
        });
    }

    @Override
    public void deleteFile(String filename, String userId, String password) {
        super.reTry( () -> {
            clt_deleteFile( filename, userId, password );
            return null;
        });
    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) {
         super.reTry( () -> {
             clt_shareFile( filename, userId,userIdShare,password);
             return null;
        });
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) {
        super.reTry( () -> {
            clt_unshareFile( filename, userId, userIdShare, password);
            return null;
        });
    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) {
        return super.reTry( () -> {
            return clt_getFile( filename, userId, accUserId, password);
        });
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) {
        return super.reTry( () -> {
            return clt_lsFile( userId,  password);
        });
    }

    private FileInfo clt_writeFile(String filename, byte[] data, String userId, String password){
        Log.info("entrou no write file do directoruy");
        Response fi = target.path(userId).path(filename)
                .queryParam("password", password).request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));

        Log.info("entrou no write file do directoruy2");
        if( fi.getStatus() == Response.Status.OK.getStatusCode() && fi.hasEntity() ){
            Log.info("entrou no write file do directoruy3");
            return fi.readEntity(new GenericType<FileInfo>() {});}
        else
            System.out.println("Error, HTTP error status: " + fi.getStatus() );

        return null;
    }

    private void clt_deleteFile(String filename, String userId, String password){

        Response fi = target.path(userId).path(filename)
                .queryParam("password", password).request()
                .delete();

        if( fi.getStatus() == Response.Status.OK.getStatusCode() && fi.hasEntity() )
            System.out.println("Deu certo: " + fi.getStatus() );
        else
            System.out.println("Error, HTTP error status: " + fi.getStatus() );

    }

    private void clt_shareFile(String filename, String userId, String userIdShare, String password){

        Response fi = target.path(userId).path(filename).path(userIdShare)
                .queryParam("password", password).request()
                .post(Entity.json(null));

        if( fi.getStatus() == Response.Status.OK.getStatusCode() && fi.hasEntity() )
            System.out.println("Deu certo: " + fi.getStatus() );
        else
            System.out.println("Error, HTTP error status: " + fi.getStatus() );

    }
    private void clt_unshareFile(String filename, String userId, String userIdShare, String password){

        Response fi = target.path(userId).path(filename).path(userIdShare)
                .queryParam("password", password).request()
                .delete();

        if( fi.getStatus() == Response.Status.OK.getStatusCode() && fi.hasEntity() )
            System.out.println("Deu certo: " + fi.getStatus() );
        else
            System.out.println("Error, HTTP error status: " + fi.getStatus() );

    }

    private byte[] clt_getFile(String filename, String userId, String accUserId, String password){

        Response fi = target.path(userId).path(filename)
                .queryParam("password", password).request()
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .get();

        if( fi.getStatus() == Response.Status.OK.getStatusCode() && fi.hasEntity() ) {
            System.out.println("Deu certo: " + fi.getStatus());
            return fi.readEntity(new GenericType<byte[]>() {});
        }
        else
            System.out.println("Error, HTTP error status: " + fi.getStatus() );

        return null;

    }
    private List<FileInfo> clt_lsFile(String userId, String password){
        String s = "/" + userId;
        Response fi = target.path(s)
                .queryParam("password", password).request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        if( fi.getStatus() == Response.Status.OK.getStatusCode() && fi.hasEntity() ) {
            System.out.println("Deu certo: " + fi.getStatus());
            return fi.readEntity(new GenericType<List<FileInfo>>() {});
        }
        else
            System.out.println("Error, HTTP error status: " + fi.getStatus() );

        return null;

    }



}
