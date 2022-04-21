package tp1.server.resources;

import tp1.api.User;
import tp1.api.service.soap.SoapUsers;
import tp1.api.service.soap.UsersException;
import tp1.server.javas.JavaUsers;

import java.net.URISyntaxException;
import java.util.List;

public class UserWebService extends SoapResources implements SoapUsers {

    final JavaUsers impl = new JavaUsers();

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
    public User deleteUser(String userId, String password) throws UsersException, URISyntaxException {
        return super.reTry(impl.deleteUser(userId, password));
    }

    @Override
    public List<User> searchUsers(String pattern) throws UsersException {

        return super.reTry(impl.searchUsers(pattern));
    }
}
