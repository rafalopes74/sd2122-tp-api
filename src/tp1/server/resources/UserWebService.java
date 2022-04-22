package tp1.server.resources;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import tp1.api.User;
import tp1.api.service.soap.SoapUsers;
import tp1.api.service.soap.UsersException;
import tp1.server.javas.JavaUsers;

import java.net.URISyntaxException;
import java.util.List;
@WebService(serviceName=SoapUsers.NAME, targetNamespace=SoapUsers.NAMESPACE, endpointInterface=SoapUsers.INTERFACE)
public class UserWebService extends SoapResources implements SoapUsers {

    final JavaUsers impl = new JavaUsers();

    @WebMethod
    public String createUser(User user) throws UsersException {
        return super.reTry(impl.createUser(user));
    }

    @WebMethod
    public User getUser(String userId, String password) throws UsersException {
        return super.reTry(impl.getUser(userId, password));
    }

    @WebMethod
    public User updateUser(String userId, String password, User user) throws UsersException {
        return super.reTry(impl.updateUser(userId, password, user));
    }

    @WebMethod
    public User deleteUser(String userId, String password) throws UsersException, URISyntaxException {
        return super.reTry(impl.deleteUser(userId, password));
    }

    @WebMethod
    public List<User> searchUsers(String pattern) throws UsersException {

        return super.reTry(impl.searchUsers(pattern));
    }
}
