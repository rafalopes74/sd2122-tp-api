package tp1.server.resources;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import tp1.api.service.soap.FilesException;
import tp1.api.service.soap.SoapFiles;
import tp1.api.service.soap.SoapUsers;
import tp1.api.service.soap.UsersException;
import tp1.server.javas.JavaFiles;

import java.util.logging.Logger;

@WebService(serviceName= SoapFiles.NAME, targetNamespace=SoapFiles.NAMESPACE, endpointInterface=SoapFiles.INTERFACE)
public class FileWebService extends SoapResources implements SoapFiles {
    private static Logger Log = Logger.getLogger(FileWebService.class.getName());
    final JavaFiles impl = new JavaFiles();

    @WebMethod
    public byte[] getFile(String fileId, String token) throws FilesException, UsersException {
        var x = impl.getFile(fileId, token);
        if( x.isOK() )
            return x.value();
        else{
            Log.info("COMO VEM A INFORMAÇÃO de erro: " + x.toString());
            throw new FilesException( "barraca a dar get no file") ;
        }
    }

    @WebMethod
    public void deleteFile(String fileId, String token) throws FilesException, UsersException {
        var x = impl.deleteFile(fileId, token);
        if( x.isOK() )
            x.value();
        else{
            Log.info("COMO VEM A INFORMAÇÃO de erro: " + x.toString());
            throw new FilesException( "barraca a dar delete no file") ;
        }
    }

    @WebMethod
    public void writeFile(String fileId, byte[] data, String token) throws UsersException, FilesException {
        var x = impl.writeFile(fileId, data,  token);
        if( x.isOK() )
            x.value();
        else{
            Log.info("COMO VEM A INFORMAÇÃO de erro: " + x.toString());
            throw new FilesException( "barraca a dar write do file") ;
        }
    }
}
