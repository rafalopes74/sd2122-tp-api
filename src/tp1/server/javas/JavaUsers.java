package tp1.server.javas;


import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;
import jakarta.inject.Singleton;
import tp1.api.User;
import tp1.api.service.util.Result;
import tp1.client.ClientFactory;
import tp1.client.RestDirectoryClient;
import tp1.client.SoapDirectoryClient;
import tp1.server.util.Discovery;

@Singleton
public class JavaUsers {

    private final ConcurrentMap<String,User> users = new ConcurrentHashMap<>();

    private static Logger Log = Logger.getLogger(JavaUsers.class.getName());

    private ClientFactory factory = new ClientFactory();

    public JavaUsers() {
    }


    public Result<String> createUser(User user) {
        Log.info("createUser : " + user);

        // Check if user data is valid
        if(user.getUserId() == null || user.getPassword() == null || user.getFullName() == null ||
                user.getEmail() == null) {
            Log.info("User object invalid.");
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }

        // Check if userId already exists
        if( this.users.containsKey(user.getUserId())) {
            Log.info("User already exists.");
            return Result.error(Result.ErrorCode.CONFLICT);
        }

        //Add the user to the map of users
        this.users.put(user.getUserId(), user);
        return Result.ok(user.getUserId());
    }



    public Result<User> getUser(String userId, String password) {
        Log.info("getUser : user = " + userId + "; pwd = " + password);

        // Check if user is valid
        if(userId == null) {
            Log.info("UserId null.");
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }


        User user = users.get(userId);

        // Check if user exists
        if( user == null ) {
            Log.info("User does not exist.");
            return Result.error(Result.ErrorCode.NOT_FOUND);
        }

        //Check if the password is correct
        if( !user.getPassword().equals( password) || password == null) {
            Log.info("Password is incorrect or null.");
            return Result.error(Result.ErrorCode.FORBIDDEN);
        }

        return Result.ok(user);
    }



    public Result<User> updateUser(String userId, String password, User user) {
        Log.info("updateUser : user = " + userId + "; pwd = " + password + " ; user = " + user);

        // Check if user is valid
        if(userId == null ) {
            Log.info("UserId null");
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }
        User u = users.get(userId);

        // Check if user exists
        if( u == null ) {
            Log.info("User does not exist.");
            return Result.error(Result.ErrorCode.NOT_FOUND);
        }

        //Check if the password is correct
        if(!u.getPassword().equals( password) || password == null) {
            Log.info("Password is incorrect or null.");
            return Result.error(Result.ErrorCode.FORBIDDEN);
        }

        user.setUserId(u.getUserId());

        if(user.getEmail() == null)
            user.setEmail(u.getEmail());

        if(user.getPassword() == null)
            user.setPassword(u.getPassword());

        if(user.getFullName() == null)
            user.setFullName(u.getFullName());

        // encontrar outra maneira de dar update...
        users.remove(userId);
        users.put(user.getUserId(), user);

        return Result.ok(user);
    }


    public Result<User> deleteUser(String userId, String password) throws URISyntaxException, MalformedURLException {
        Log.info("deleteUser : user = " + userId + "; pwd = " + password);

        //check if values are correct
        if(userId == null ){
            Log.info("UserId nnull.");
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }

        User user = users.get(userId);

        if(user == null) {
            Log.info("User does not exist.");
            return Result.error(Result.ErrorCode.NOT_FOUND);
        }

        //Check if the password is correct
        if( !user.getPassword().equals( password) || password == null) {
            Log.info("Password is incorrect or null.");
            return Result.error(Result.ErrorCode.FORBIDDEN);
        }


        Log.info("VALOR DO CARLAHO " + Discovery.getInstance().knownUrisOf("directory").toArray().length );

        if(Discovery.getInstance().knownUrisOf("directory").toArray().length != 0)
            ClientFactory.getDirectory().deleteFilesUser(userId, password);



        users.remove(userId);

        return Result.ok(user);
    }


    public Result<List<User>> searchUsers(String pattern) {
        Log.info("searchUsers : pattern = " + pattern);
        ArrayList<User> au = new ArrayList<User>();
        Iterator<String> it = users.keySet().iterator();
        while(it.hasNext()){
            String id =it.next();
            String s = users.get(id).getFullName().toLowerCase();
            if(s.contains(pattern.toLowerCase()))
                au.add(users.get(id));
        }
        return Result.ok(au);
    }

}
