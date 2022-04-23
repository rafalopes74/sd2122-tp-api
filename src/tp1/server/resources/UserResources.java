package tp1.server.resources;

import jakarta.inject.Singleton;
import tp1.api.User;
import tp1.api.service.rest.RestUsers;
import tp1.server.javas.JavaUsers;


import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

@Singleton
public class UserResources extends RestResources implements RestUsers {
    private static Logger Log = Logger.getLogger(UserResources.class.getName());
    final JavaUsers impl = new JavaUsers();


    @Override
    public String createUser(User user) {
        return super.reTry(impl.createUser(user));
    }

    @Override
    public User getUser(String userId, String password) {
        return super.reTry(impl.getUser(userId, password));


    }

    @Override
    public User updateUser(String userId, String password, User user) {
        return super.reTry(impl.updateUser(userId, password, user));
    }

    @Override
    public User deleteUser(String userId, String password) throws URISyntaxException, MalformedURLException {
        return super.reTry(impl.deleteUser(userId, password));
    }

    @Override
    public List<User> searchUsers(String pattern) {

        return super.reTry(impl.searchUsers(pattern));
    }

}
