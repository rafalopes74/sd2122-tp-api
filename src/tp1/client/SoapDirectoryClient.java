package tp1.client;

import com.sun.xml.ws.client.BindingProviderProperties;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebServiceException;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.soap.*;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;


import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

public class SoapDirectoryClient extends SoapClient implements Directory {
    private static final Logger Log = Logger.getLogger(SoapDirectoryClient.class.getName());
    private static Service service = null;

    private static SoapDirectory dir;
    public SoapDirectoryClient(URI serverURI) throws MalformedURLException {
        super( serverURI );
        QName qname = new QName(SoapDirectory.NAMESPACE, SoapDirectory.NAME);
        this.service = Service.create( URI.create(serverURI + "/?wsdl").toURL(), qname);


        dir = service.getPort(tp1.api.service.soap.SoapDirectory.class);
    }
    @Override
    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
        Result<FileInfo> res =  super.reTry( () -> clt_writeFile( filename,data,userId,password ));


        if(res == null)
            return Result.error(Result.ErrorCode.INTERNAL_ERROR);
        else return res;
    }

    @Override
    public Result<Void> deleteFile(String filename, String userId, String password) {
        Result<Void> res =  super.reTry( () -> clt_deleteFile(filename, userId, password));

        if(res == null)
            return Result.error(Result.ErrorCode.INTERNAL_ERROR);
        else return res;
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
    public Result<List<FileInfo>> lsFile(String userId, String password) {
        return Result.error(Result.ErrorCode.NOT_IMPLEMENTED);
    }

    @Override
    public Result<Void> deleteFilesUser(String userId, String password) {
        Result<Void> res = super.reTry( () -> clt_deleteUserFile(userId, password));

        if(res == null)
            return Result.error(Result.ErrorCode.INTERNAL_ERROR);
        else return res;
    }

    private Result<Void> clt_deleteUserFile(String userId, String password){

           try{
            dir.deleteFilesUser(userId, password);

            System.out.println("Result: Correu Bem");
            return Result.ok();

        } catch (WebServiceException we){
            new WebServiceException("Result: problema na parte do servidor");

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (DirectoryException e) {
            throw new RuntimeException(e);
        }

        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    private Result<FileInfo> clt_writeFile(String filename, byte[] data, String userId, String password){
        return Result.error(Result.ErrorCode.NOT_IMPLEMENTED);
    }

    private Result<Void> clt_deleteFile(String filename, String userId, String password){
        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    private Result<Void> clt_shareFile(String filename, String userId, String userIdShare, String password){
        return Result.error(Result.ErrorCode.NOT_IMPLEMENTED);

    }
    private Result<Void> clt_unshareFile(String filename, String userId, String userIdShare, String password){
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
