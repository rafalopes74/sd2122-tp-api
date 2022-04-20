package tp1.api.service.soap;

import java.util.List;

import jakarta.jws.WebService;
import tp1.api.User;
import tp1.server.resources.JavaUsers;
import tp1.api.service.util.Users;

@WebService(serviceName= SoapUsers.NAME, targetNamespace=SoapUsers.NAMESPACE, endpointInterface=SoapUsers.INTERFACE)
public class UsersWebService extends SoapUsersWeb implements SoapUsers {


    final JavaUsers impl = new JavaUsers();

    public UsersWebService() {
    }

    @Override
    public String createUser(User user) throws UsersException {
        return super.reTry(impl.createUser(user));
    }

    @Override
    public User getUser(String userId, String password) throws UsersException {
        return super.reTry(impl.getUser(userId, password));
    }

    @Override
    public User updateUser(String userId, String password, User user) throws UsersException {
        return super.reTry(impl.updateUser(userId, password, user));
    }

    @Override
    public User deleteUser(String userId, String password) throws UsersException {
        return super.reTry(impl.deleteUser(userId, password));
    }

    @Override
    public List<User> searchUsers(String pattern) throws UsersException {
        return super.reTry(impl.searchUsers(pattern));
    }

    /*
    private boolean badUserData(User user) {
        //TODO check user data...
        return false;
    }
    */
}
