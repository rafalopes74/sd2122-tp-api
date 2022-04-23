package tp1.client;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.sun.xml.ws.client.BindingProviderProperties;
import jakarta.inject.Singleton;


import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebServiceException;
import tp1.api.User;

import tp1.api.service.soap.SoapUsers;
import tp1.api.service.soap.UsersException;
import tp1.api.service.util.Result;
import tp1.api.service.util.Users;

import javax.xml.namespace.QName;

@Singleton
public class SoapUserClient extends SoapClient implements Users {

    private static Service service = null;
    public SoapUserClient(URI serverURI) throws MalformedURLException {
        super(serverURI);
        QName qname = new QName(SoapUsers.NAMESPACE, SoapUsers.NAME);
        this.service = Service.create( URI.create(serverURI + "/?wsdl").toURL(), qname);
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
        SoapUsers users = service.getPort(tp1.api.service.soap.SoapUsers.class);

        ((BindingProvider) users).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        ((BindingProvider) users).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, READ_TIMEOUT);

        try{
            var u = users.createUser(user);

            System.out.println("Result: Correu Bem");

            return Result.ok(u);
        }
        catch (WebServiceException we){
            new WebServiceException("Result: problema na parte do servidor");
        } catch (UsersException e) {
            new UsersException("Result: userException lançada");
        }

        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    private Result<List<User>> clt_searchUsers(String pattern) {
        SoapUsers users = service.getPort(tp1.api.service.soap.SoapUsers.class);

        ((BindingProvider) users).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        ((BindingProvider) users).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, READ_TIMEOUT);
        try{
            var u = users.searchUsers(pattern);

            System.out.println("Result: Correu Bem");

            return Result.ok(u);
        }
        catch (WebServiceException we){
            new WebServiceException("Result: problema na parte do servidor");
        } catch (UsersException e) {
            new UsersException("Result: userException lançada");
        }

        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    private Result<User> clt_getUser(String userId, String password) {
        SoapUsers users = service.getPort(tp1.api.service.soap.SoapUsers.class);

        ((BindingProvider) users).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        ((BindingProvider) users).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, READ_TIMEOUT);
        try{
            var u = users.getUser(userId, password);

            System.out.println("Result: Correu Bem");



            return Result.ok(u);
        }
        catch (WebServiceException we){
            new WebServiceException("Result: problema na parte do servidor");
        } catch (UsersException e) {
            new UsersException("Result: userException lançada");
        }

        return Result.error(Result.ErrorCode.BAD_REQUEST);

    }

    private Result<User> clt_updateUser(String userId, String oldpwd, User u){
        SoapUsers users = service.getPort(tp1.api.service.soap.SoapUsers.class);

        ((BindingProvider) users).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        ((BindingProvider) users).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, READ_TIMEOUT);
        try{
            var ur = users.updateUser(userId, oldpwd, u);

            System.out.println("Result: Correu Bem");

            return Result.ok(ur);
        }
        catch (WebServiceException we){
            new WebServiceException("Result: problema na parte do servidor");
        } catch (UsersException e) {
            new UsersException("Result: userException lançada");
        }

        return Result.error(Result.ErrorCode.BAD_REQUEST);

    }

    private Result<User> clt_deleteUser(String userId, String password){

        SoapUsers users = service.getPort(tp1.api.service.soap.SoapUsers.class);

        ((BindingProvider) users).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        ((BindingProvider) users).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, READ_TIMEOUT);
        try{
            var ur = users.deleteUser(userId, password);

            System.out.println("Result: Correu Bem");

            return Result.ok(ur);
        }
        catch (WebServiceException we){
            new WebServiceException("Result: problema na parte do servidor");
        } catch (UsersException e) {
            new UsersException("Result: userException lançada");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
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
            default:
                return Result.ErrorCode.INTERNAL_ERROR;

        }
    }
}
