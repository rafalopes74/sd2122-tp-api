package tp1.client;

import java.net.URI;
import java.util.List;

import jakarta.inject.Singleton;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import tp1.api.User;
import tp1.api.service.rest.RestUsers;
import tp1.api.service.util.Result;
import tp1.api.service.util.Users;

@Singleton
public class RestUsersClient extends RestClient implements Users {

    final WebTarget target;

    public RestUsersClient(URI serverURI) {
        super( serverURI );
        target = client.target( serverURI ).path( RestUsers.PATH );
    }

    @Override
    public Result<String> createUser(User user) {
        return super.reTry( () -> {
            return clt_createUser( user );
        });
    }

    @Override
    public Result<User> getUser(String userId, String password) {
        return super.reTry( () ->{
            return clt_getUser(userId, password);
        });
    }

    @Override
    public Result<User> updateUser(String userId, String password, User user) {
        return super.reTry( () ->{
            return clt_updateUser(userId, password, user);
        });
    }

    @Override
    public Result<User> deleteUser(String userId, String password) {
        return super.reTry( () ->{
            return clt_deleteUser(userId, password);
        });
    }

    @Override
    public Result<List<User>> searchUsers(String pattern) {
        return super.reTry( () -> clt_searchUsers( pattern ));
    }



    private Result<String> clt_createUser( User user) {

        Response r = target.request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(user, MediaType.APPLICATION_JSON));

        if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() )
            return Result.ok(r.readEntity(String.class));
        else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
            return Result.error(errorCode(r.getStatus()));
        }
    }

    private Result<List<User>> clt_searchUsers(String pattern) {
        Response r = target
                .queryParam("query", pattern)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() )
            return Result.ok(r.readEntity(new GenericType<List<User>>() {}));
        else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
            return Result.error(errorCode(r.getStatus()));
        }

    }

    private Result<User> clt_getUser(String userId, String password) {
        Response r = target.path(userId)
                .queryParam("password", password).request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        if (r.getStatus() == Status.OK.getStatusCode() && r.hasEntity()) {
            System.out.println("Success:");
            System.out.println("User : " + User.class);
            return Result.ok(r.readEntity(new GenericType<User>() {}));

        } else{
            System.out.println("Error, HTTP error status: " + r.getStatus());
            return Result.error(errorCode(r.getStatus()));
        }


    }

    private Result<User> clt_updateUser(String userId, String oldpwd, User u){
        Response r = target.path( userId )
                .queryParam("password", oldpwd).request()
                .accept(MediaType.APPLICATION_JSON)
                .put(Entity.entity(u,MediaType.APPLICATION_JSON));

        if( r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity() ) {
            System.out.println("Success, update user with id: " + r.readEntity(String.class) );
            return Result.ok(u);
        } else{
            System.out.println("Error, HTTP error status: " + r.getStatus() );
            return Result.error(errorCode(r.getStatus()));
        }
    }

    private Result<User> clt_deleteUser(String userId, String password){

        User u = clt_getUser(userId, password).value();
        Response r = target.path( userId )
                .queryParam("password", password).request()
                .accept(MediaType.APPLICATION_JSON)
                .delete();

        if( r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity() ) {
            System.out.println("Success, deleted user with id: " + r.readEntity(String.class) );
            return Result.ok(u);
        } else
            System.out.println("Error, HTTP error status: " + r.getStatus() );
            return Result.error(errorCode(r.getStatus()));
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
