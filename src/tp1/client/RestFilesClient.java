package tp1.client;


import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tp1.api.service.rest.RestFiles;

import tp1.api.service.util.Files;
import tp1.api.service.util.Result;
import tp1.client.RestClient;

import java.io.InputStream;
import java.net.URI;
import java.util.logging.Logger;

public class RestFilesClient extends RestClient implements Files {
    final WebTarget target;
    private static final Logger Log = Logger.getLogger(RestFilesClient.class.getName());

    public RestFilesClient(URI serverURI) {
        super( serverURI );
        target = client.target( serverURI ).path( RestFiles.PATH );


    }

    @Override
    public Result<Void> writeFile(String fileId, byte[] data, String token) {

        return super.reTry( () -> clt_writeFile( fileId, data,token ) );
    }

    @Override
    public Result<Void> deleteFile(String fileId, String token) {
        return super.reTry( () -> clt_deleteFile( fileId, token ));
    }

    @Override
    public Result<byte[]> getFile(String fileId, String token) {
        return super.reTry( () -> clt_getFile( fileId, token));
    }


    private Result<Void> clt_writeFile(String fileId, byte[] data, String token){
        Response r = target.path(fileId).queryParam(token)
                .request().accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));

        if(r.getStatus() == Response.Status.NO_CONTENT.getStatusCode() || r.getStatus() == Response.Status.OK.getStatusCode() ){
            return Result.ok(r.readEntity(Void.class));
        }else{
            return Result.error(errorCode(r.getStatus()));
        }
    }


    private Result<Void> clt_deleteFile(String fileId, String token) {
        Response r = target.path(fileId).request().delete();

        if(r.getStatus() == Response.Status.NO_CONTENT.getStatusCode() || r.getStatus() == Response.Status.OK.getStatusCode() ){
            return Result.ok(r.readEntity(Void.class));
        }else{
            return Result.error(errorCode(r.getStatus()));
        }
    }

    private Result<byte[]> clt_getFile(String fileId, String token) {
        Response r = target.path(fileId).request().accept(MediaType.APPLICATION_OCTET_STREAM_TYPE).get();

        if(r.getStatus() == Response.Status.NO_CONTENT.getStatusCode() || r.getStatus() == Response.Status.OK.getStatusCode() ){
            return Result.ok(r.readEntity(byte[].class));
        }else{
            return Result.error(errorCode(r.getStatus()));
        }

    }

    private Result.ErrorCode errorCode(int status){
        switch(status) {
            case 403:
                return Result.ErrorCode.FORBIDDEN;
            case 404:
                return Result.ErrorCode.NOT_FOUND;
            case 409:
                return Result.ErrorCode.CONFLICT;
            case 405:
                Log.info("REBENTOU");
                return Result.ErrorCode.INTERNAL_ERROR;
            default:
                return Result.ErrorCode.INTERNAL_ERROR;

        }
    }


}
