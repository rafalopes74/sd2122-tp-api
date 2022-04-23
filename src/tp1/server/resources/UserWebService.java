package tp1.server.resources;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import tp1.api.User;
import tp1.api.service.soap.SoapUsers;
import tp1.api.service.soap.UsersException;
import tp1.server.javas.JavaUsers;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

@WebService(serviceName=SoapUsers.NAME, targetNamespace=SoapUsers.NAMESPACE, endpointInterface=SoapUsers.INTERFACE)
public class UserWebService extends SoapResources implements SoapUsers {
    private static Logger Log = Logger.getLogger(UserResources.class.getName());
    final JavaUsers impl = new JavaUsers();

    @WebMethod
    public String createUser(User user) throws UsersException {
        var x = impl.createUser(user);
        if( x.isOK() )
            return x.value();
        else
            throw new UsersException( "barraca a criar o user") ;
    }

    @WebMethod
    public User getUser(String userId, String password) throws UsersException {

        var x = impl.getUser(userId, password);
        if( x.isOK() )
            return x.value();
        else{
            Log.info("COMO VEM A INFORMAÇÃO de erro: " + x.toString());
            throw new UsersException( "barraca a dar get no user") ;
        }
    }

    @WebMethod
    public User updateUser(String userId, String password, User user) throws UsersException {
        var x = impl.updateUser(userId, password, user);
        if( x.isOK() )
            return x.value();
        else{
            Log.info("COMO VEM A INFORMAÇÃO de erro: " + x.toString());
            throw new UsersException( "barraca a dar update") ;
        }
    }

    @WebMethod
    public User deleteUser(String userId, String password) throws UsersException, URISyntaxException, MalformedURLException {
        var x = impl.deleteUser(userId, password);
        if( x.isOK() )
            return x.value();
        else{
            Log.info("COMO VEM A INFORMAÇÃO de erro: " + x.toString());
            throw new UsersException( "barraca a dar get no user") ;
        }
    }

    @WebMethod
    public List<User> searchUsers(String pattern) throws UsersException {

        var x = impl.searchUsers(pattern);
        if( x.isOK() )
            return x.value();
        else{
            Log.info("COMO VEM A INFORMAÇÃO de erro: " + x.toString());
            throw new UsersException( "barraca a dar get no user") ;
        }
    }
}
