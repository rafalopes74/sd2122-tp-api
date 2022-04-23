package tp1.client;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;


import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

public class RestDirectoryClient extends RestClient implements Directory {
    private static final Logger Log = Logger.getLogger(RestDirectoryClient.class.getName());

    final WebTarget target;

    public RestDirectoryClient(URI serverURI) {
        super( serverURI );
        target = client.target( serverURI ).path( RestDirectory.PATH );
    }
    @Override
    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
        return super.reTry( () -> clt_writeFile( filename,data,userId,password ));
    }

    @Override
    public Result<Void> deleteFile(String filename, String userId, String password) {
        return super.reTry( () -> clt_deleteFile(filename, userId, password));
    }

    @Override
    public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) {
        return Result.error(Result.ErrorCode.NOT_IMPLEMENTED);
    }

    @Override
    public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) {
        return Result.error(Result.ErrorCode.NOT_IMPLEMENTED);
    }

    @Override
    public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) {
        return Result.error(Result.ErrorCode.NOT_IMPLEMENTED);

    }


    @Override
    public Result<Void> deleteFilesUser(String userId, String password){
        return super.reTry( () -> clt_deleteUser(userId, password));
    }

    private Result<Void> clt_deleteUser(String userId, String password){
        Response r = target.path(userId).queryParam("password", password)
                .request().delete();

        if(r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity()){
            return Result.ok(r.readEntity(Void.class));
        }else{
            return Result.error(errorCode(r.getStatus()));
        }

    }

    @Override
    public Result<List<FileInfo>> lsFile(String userId, String password) {
        return Result.error(Result.ErrorCode.NOT_IMPLEMENTED);
    }

    private Result<FileInfo> clt_writeFile(String filename, byte[] data, String userId, String password){
            Response r = target.path(userId).path(filename)
                    .queryParam("password", password).request()
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));

            if(r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity()){
                return Result.ok(r.readEntity(new GenericType<FileInfo>() {}));
            }else{
                return Result.error(errorCode(r.getStatus()));
            }
    }

    private Result<Void> clt_deleteFile(String filename, String userId, String password){
            Response r = target.path(userId).path(filename).queryParam("password", password).request().delete();

            if(r.getStatus() == Response.Status.NO_CONTENT.getStatusCode() || r.getStatus() == Response.Status.OK.getStatusCode() ){
                return Result.ok(r.readEntity(Void.class));
            }else{
                return Result.error(errorCode(r.getStatus()));
            }
    }

    private Result<Void> clt_shareFile(String filename, String userId, String userIdShare, String password){
            Response r = target.path(userId).path(filename).path(userIdShare).queryParam("password", password).request().post(Entity.json(null));

            if(r.getStatus() == Response.Status.NO_CONTENT.getStatusCode() || r.getStatus() == Response.Status.OK.getStatusCode() ){
                return Result.ok(r.readEntity(Void.class));
            }else{
                return Result.error(errorCode(r.getStatus()));
            }

    }
    private Result<Void> clt_unshareFile(String filename, String userId, String userIdShare, String password){

            Response r = target.path(userId).path(filename).path(userIdShare).queryParam("password", password).request().delete();

            if(r.getStatus() == Response.Status.NO_CONTENT.getStatusCode() || r.getStatus() == Response.Status.OK.getStatusCode() ){
                return Result.ok(r.readEntity(Void.class));
            }else{
                return Result.error(errorCode(r.getStatus()));
            }
    }

    private Result<byte[]> clt_getFile(String filename, String userId, String accUserId, String password){
        return Result.error(Result.ErrorCode.NOT_IMPLEMENTED);
    }

    private Result<List<FileInfo>> clt_lsFile(String userId, String password){
        return Result.error(Result.ErrorCode.NOT_IMPLEMENTED);
    }

    private Result.ErrorCode errorCode(int status){
        switch(status) {
            case 403:
                return Result.ErrorCode.FORBIDDEN;
            case 404:
                return Result.ErrorCode.NOT_FOUND;
            case 409:
                return Result.ErrorCode.CONFLICT;
            default:
                return Result.ErrorCode.INTERNAL_ERROR;

        }
    }
}
