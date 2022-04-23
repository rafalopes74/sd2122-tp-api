package tp1.client;


import com.sun.xml.ws.client.BindingProviderProperties;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebServiceException;
import tp1.api.service.rest.RestFiles;

import tp1.api.service.soap.FilesException;
import tp1.api.service.soap.SoapFiles;
import tp1.api.service.soap.SoapUsers;
import tp1.api.service.soap.UsersException;
import tp1.api.service.util.Files;
import tp1.api.service.util.Result;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.logging.Logger;

public class SoapFilesClient extends SoapClient implements Files {

    private static final Logger Log = Logger.getLogger(SoapFilesClient.class.getName());
    private static Service service = null;

    public SoapFilesClient(URI serverURI) throws MalformedURLException {
        super(serverURI);
        QName qname = new QName(SoapFiles.NAMESPACE, SoapFiles.NAME);
        this.service = Service.create( URI.create(serverURI + "/?wsdl").toURL(), qname);

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


    private Result<Void> clt_writeFile(String fileId, byte[] data, String token) {
        SoapFiles files = service.getPort(tp1.api.service.soap.SoapFiles.class);

        ((BindingProvider) files).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        ((BindingProvider) files).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, READ_TIMEOUT);
        try{
            files.writeFile(fileId, data, token);

            System.out.println("Result: Correu Bem");
            return Result.ok();
        } catch (FilesException x) {
            new FilesException("Result: problema na parte dos files");
        } catch (WebServiceException we){
            new WebServiceException("Result: problema na parte do servidor");
        } catch (UsersException e) {
            new UsersException("Result: userException lançada");
        }

        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }


    private Result<Void> clt_deleteFile(String fileId, String token) {
        SoapFiles files = service.getPort(tp1.api.service.soap.SoapFiles.class);

        ((BindingProvider) files).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        ((BindingProvider) files).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, READ_TIMEOUT);
        try{
            files.deleteFile(fileId,token);

            System.out.println("Result: Correu Bem");

            return Result.ok();
        } catch (FilesException x) {
            new FilesException("Result: problema na parte dos files");
        } catch (WebServiceException we){
            new WebServiceException("Result: problema na parte do servidor");
        } catch (UsersException e) {
            new UsersException("Result: userException lançada");
        }

        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    private Result<byte[]> clt_getFile(String fileId, String token) {
        SoapFiles files = service.getPort(tp1.api.service.soap.SoapFiles.class);

        ((BindingProvider) files).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        ((BindingProvider) files).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, READ_TIMEOUT);
        try{
            var r = files.getFile(fileId,token);

            System.out.println("Result: Correu Bem");

            return Result.ok(r);
        } catch (FilesException x) {
            new FilesException("Result: problema na parte dos files");
        } catch (WebServiceException we){
            new WebServiceException("Result: problema na parte do servidor");
        } catch (UsersException e) {
            new UsersException("Result: userException lançada");
        }

        return Result.error(Result.ErrorCode.BAD_REQUEST);
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
