package tp1.client;

import jakarta.ws.rs.client.WebTarget;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.soap.SoapDirectory;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;
import tp1.client.RestClient;
import tp1.client.RestDirectoryClient;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

public class SoapDirectoryClient extends SoapClient implements SoapDirectory {
    private static final Logger Log = Logger.getLogger(RestDirectoryClient.class.getName());


    SoapDirectoryClient(URI serverURI) {
        super(serverURI);

    }

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) {
        return super.reTry( () -> clt_writeFile( filename,data,userId,password ));
    }

    @Override
    public void deleteFile(String filename, String userId, String password) {
        super.reTry( () -> clt_deleteFile(filename, userId, password));
    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) {
        //return Result.error(Result.ErrorCode.NOT_IMPLEMENTED);
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) {
        //return Result.error(Result.ErrorCode.NOT_IMPLEMENTED);
    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) {
        return null;

    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) {
        return null;
    }


    /*
    @Override
    public void deleteFilesUser(String userId, String password){
        return null; //super.reTry( () -> clt_deleteUser(userId, password));
    }
*/

    private void clt_deleteUser(String userId, String password){


    }
    private FileInfo clt_writeFile(String filename, byte[] data, String userId, String password){
        return null;
    }

    private void clt_deleteFile(String filename, String userId, String password){

    }

    private void clt_shareFile(String filename, String userId, String userIdShare, String password){

    }
    private void clt_unshareFile(String filename, String userId, String userIdShare, String password){

        return Result.error(Result.ErrorCode.NOT_IMPLEMENTED);
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
