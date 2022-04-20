package tp1.client;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tp1.api.FileInfo;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;
import tp1.server.rest.RestDirectory;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

public class RestDirectoryClient extends RestClient implements Directory {
    private static final Logger Log = Logger.getLogger(RestDirectoryClient.class.getName());

    final WebTarget target;

    RestDirectoryClient(URI serverURI) {
        super(serverURI);
        target = client.target(serverURI).path(RestDirectory.PATH);
    }

    @Override
    public Result<Void> deleteFile(String filename, String userId, String password) {
        return super.reTry(() -> {
            return clt_deleteFile(filename, userId, password);
        });
    }

    @Override
    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
        return super.reTry(() -> {
            return clt_writeFile(filename, data, userId, password);
        });
    }

    @Override
    public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) {
        return super.reTry(() -> {
            return clt_shareFile(filename, userId, userIdShare, password);
        });
    }

    @Override
    public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) {
        return super.reTry(() -> {
            return clt_unshareFile(filename, userId, userIdShare, password);
        });
    }

    @Override
    public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) {
        return super.reTry(() -> {
            return clt_getFile(filename, userId, accUserId, password);
        });
    }

    @Override
    public Result<List<FileInfo>> lsFile(String userId, String password) {
        return super.reTry(() -> {
            return clt_lsFile(userId, password);
        });
    }

    private Result<FileInfo> clt_writeFile(String filename, byte[] data, String userId, String password) {
        Log.info("entrou no write file do directoruy");
        Response fi = target.path(userId).path(filename)
                .queryParam("password", password).request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));

        Log.info("entrou no write file do directoruy2");
        if (fi.getStatus() == Response.Status.OK.getStatusCode() && fi.hasEntity()) {
            Log.info("entrou no write file do directoruy3");
            return Result.ok(fi.readEntity(new GenericType<FileInfo>() {
            }));
        } else {
            System.out.println("Error, HTTP error status: " + fi.getStatus());
            return Result.error(errorCode(fi.getStatus()));
        }
    }

    private Result<Void> clt_deleteFile(String filename, String userId, String password) {
        Response fi = target.path(userId).path(filename)
                .queryParam("password", password).request()
                .delete();

        if (fi.getStatus() == Response.Status.OK.getStatusCode() && fi.hasEntity()) {
            System.out.println("Deu certo: " + fi.getStatus());
            return Result.ok(fi.readEntity(void.class));
        } else {
            System.out.println("Error, HTTP error status: " + fi.getStatus());
            return Result.error(errorCode(fi.getStatus()));
        }

    }

    private Result<Void> clt_shareFile(String filename, String userId, String userIdShare, String password) {

        Response fi = target.path(userId).path(filename).path(userIdShare)
                .queryParam("password", password).request()
                .post(Entity.json(null));

        if (fi.getStatus() == Response.Status.OK.getStatusCode() && fi.hasEntity()) {
            System.out.println("Deu certo: " + fi.getStatus());
            return Result.ok(fi.readEntity(void.class));
        } else {
            System.out.println("Error, HTTP error status: " + fi.getStatus());
            return Result.error(errorCode(fi.getStatus()));
        }


    }

    private Result<Void> clt_unshareFile(String filename, String userId, String userIdShare, String password) {

        Response fi = target.path(userId).path(filename).path(userIdShare)
                .queryParam("password", password).request()
                .delete();

        if (fi.getStatus() == Response.Status.OK.getStatusCode() && fi.hasEntity()) {
            System.out.println("Deu certo: " + fi.getStatus());
            return Result.ok(fi.readEntity(void.class));
        } else {
            System.out.println("Error, HTTP error status: " + fi.getStatus());
            return Result.error(errorCode(fi.getStatus()));
        }

    }

    private Result<byte[]> clt_getFile(String filename, String userId, String accUserId, String password) {
        /*
        Response fi = target.path(userId).path(filename)
                .queryParam("password", password).request()
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .get();

        if (fi.getStatus() == Response.Status.OK.getStatusCode() && fi.hasEntity()) {
            System.out.println("Deu certo: " + fi.getStatus());
            return Result.ok(fi.readEntity(byte[].class));
        } else {
            System.out.println("Error, HTTP error status: " + fi.getStatus());
            return Result.error(errorCode(fi.getStatus()));
        }*/
        return null;
    }

    private Result<List<FileInfo>> clt_lsFile(String userId, String password) {
        String s = "/" + userId;
        Response fi = target.path(s)
                .queryParam("password", password).request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        if (fi.getStatus() == Response.Status.OK.getStatusCode() && fi.hasEntity()) {
            System.out.println("Deu certo: " + fi.getStatus());
            return Result.ok(fi.readEntity(new GenericType<List<FileInfo>>() {
            }));
        } else {
            System.out.println("Error, HTTP error status: " + fi.getStatus());
            return Result.error(errorCode(fi.getStatus()));
        }
    }

    private Result.ErrorCode errorCode(int status) {
        switch (status) {
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
