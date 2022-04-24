package tp1.server.resources;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import tp1.api.FileInfo;
import tp1.api.service.soap.*;
import tp1.api.service.util.Result;
import tp1.server.javas.JavaDirectory;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

@WebService(serviceName= SoapDirectory.NAME, targetNamespace=SoapDirectory.NAMESPACE, endpointInterface=SoapDirectory.INTERFACE)
public class DirectoryWebService extends SoapResources implements SoapDirectory {
    private static Logger Log = Logger.getLogger(DirectoryWebService.class.getName());
    final JavaDirectory impl = new JavaDirectory();

    public DirectoryWebService() throws MalformedURLException, URISyntaxException {
    }

    @WebMethod
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) throws DirectoryException, URISyntaxException, UsersException, MalformedURLException {
        var x = impl.writeFile(filename, data, userId, password);
        if( x.isOK() )
            return  x.value();
        else{
            Log.info("COMO VEM A INFORMAÇÃO de erro: " + x.toString());
            throw new DirectoryException( "barraca a dar writefile no directory") ;
        }

    }

    @WebMethod
    public void deleteFile(String filename, String userId, String password) throws DirectoryException, URISyntaxException, UsersException, MalformedURLException {
        var x = impl.deleteFile(filename, userId, password);
        if( x.isOK() )
            x.value();
        else{
            Log.info("COMO VEM A INFORMAÇÃO de erro: " + x.toString());
            throw new DirectoryException( "barraca a dar delete no directory") ;
        }
    }

    @WebMethod
    public void shareFile(String filename, String userId, String userIdShare, String password) throws DirectoryException, URISyntaxException, UsersException, MalformedURLException {
        var x = impl.shareFile(filename, userId, userIdShare, password);
        if( x.isOK() )
            x.value();
        else{
            Log.info("COMO VEM A INFORMAÇÃO de erro: " + x.toString());
            throw new DirectoryException( "barraca a dar share no directory") ;
        }
    }

    @WebMethod
    public void unshareFile(String filename, String userId, String userIdShare, String password) throws DirectoryException, URISyntaxException, UsersException, MalformedURLException {
        var x = impl.unshareFile(filename, userId, userIdShare, password);
        if( x.isOK() )
            x.value();
        else{
            Log.info("COMO VEM A INFORMAÇÃO de erro: " + x.toString());
            throw new DirectoryException( "barraca a dar unshare no directory") ;
        }
    }

    @WebMethod
    public byte[] getFile(String filename, String userId, String accUserId, String password) throws DirectoryException, URISyntaxException, UsersException, MalformedURLException {
        var x = impl.getFile(filename, userId, accUserId, password);
        if( x.isOK() )
            return x.value();
        else{
            Log.info("COMO VEM A INFORMAÇÃO de erro: " + x.toString());
            throw new DirectoryException( "barraca a dar getfile no directory") ;
        }
    }

    @Override
    public void deleteFilesUser(String userId, String password) throws URISyntaxException, MalformedURLException, DirectoryException {
       var x  = impl.deleteFilesUser(userId, password);
        if( x.isOK() )
            x.value();
        else{
           throw new DirectoryException( "barraca a dar getfile no directory") ;
        }
    }
    @Override
    public List<FileInfo> lsFile(String userId, String password) throws DirectoryException, URISyntaxException, UsersException, MalformedURLException {
        var x = impl.lsFile(userId, password);
        if( x.isOK() )
            return x.value();
        else{
            Log.info("COMO VEM A INFORMAÇÃO de erro: " + x.toString());
            throw new DirectoryException( "barraca a dar delete no directory") ;
        }
    }

}
