package tp1.client;


import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tp1.api.service.rest.RestFiles;

import tp1.api.service.soap.SoapFiles;
import tp1.api.service.util.Files;
import tp1.api.service.util.Result;

import java.net.URI;
import java.util.logging.Logger;

public class SoapFilesClient extends SoapClient implements SoapFiles {

    private static final Logger Log = Logger.getLogger(SoapFilesClient.class.getName());

    public SoapFilesClient(URI serverURI) {
        super( serverURI );


    }

    @Override
    public void writeFile(String fileId, byte[] data, String token) {

        super.reTry( () -> clt_writeFile( fileId, data,token ) );
    }

    @Override
    public void deleteFile(String fileId, String token) {
        super.reTry( () -> clt_deleteFile( fileId, token ));
    }

    @Override
    public byte[] getFile(String fileId, String token) {
        return super.reTry( () -> clt_getFile( fileId, token));
    }


    private void clt_writeFile(String fileId, byte[] data, String token){
        Result.error(Result.ErrorCode.NOT_IMPLEMENTED);
    }


    private void clt_deleteFile(String fileId, String token) {
        Result.error(Result.ErrorCode.NOT_IMPLEMENTED);
    }

    private byte[] clt_getFile(String fileId, String token) {
        return null;

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
