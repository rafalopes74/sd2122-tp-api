package tp1.server.resources;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;
import tp1.client.RestFileClient;
import tp1.client.RestUsersClient;
import tp1.server.rest.Resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;

public class JavaDirectory extends Resources implements Directory {

    private static final Logger Log = Logger.getLogger(JavaDirectory.class.getName());
    private final Map<String, List<FileInfo>> files = new HashMap<>();


    //private final UserResources userResources = new RestUsersClient(discovery.knownUrisOf("users")[0]);
    //private final FileResources fileResources = new FileResources();

    public JavaDirectory() throws URISyntaxException {
    }

    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) throws URISyntaxException {

        User u = (new RestUsersClient(Discovery.getInstance().knownUrisOf("users")[0])).getUser(userId, password);

        if (u != null) {
            Log.info("entrou no file, o user é " + u.getUserId());

            if (filename == null || data == null) return Result.error(Result.ErrorCode.BAD_REQUEST);

            Log.info("passou a puta " + u.getUserId());
            List<FileInfo> fi = files.get(userId);

            if (fi != null) {
                Log.info("tem files " + u.getUserId());

                boolean found = false;
                for (int i = 0; i < fi.size(); i++) {
                    if (fi.get(i).getFilename().equals(filename)) {
                        Log.info("o nome do file ja existe " + u.getUserId());

                        String fn = "/" + userId + "_" + filename;
                        var disc = Discovery.getInstance().knownUrisOf("files");

                        new RestFileClient(disc[0]).writeFile(fn, data, "");

                        found = true;
                    }
                }

                if (!found) {
                    Log.info("o nome do file nao existe " + u.getUserId());

                    //escreve no ficherio
                    var positionServer = -1;
                    String fn = "/" + userId + "_" + filename;
                    String link = null;
                    var disc = Discovery.getInstance().knownUrisOf("files");

                    Log.info("o nome do file nao existe 2 " + u.getUserId());
                    new RestFileClient(disc[0]).writeFile(fn, data, "");
                    link = disc[0].toString() + fn;

                    Log.info("o nome do file nao existe 3 " + u.getUserId());

                    List<FileInfo> l = files.get(userId);
                    l.add(new FileInfo(userId, filename, link, new HashSet<>()));
                    files.put(userId, l);

                    return Result.ok(l.get(l.size() - 1));
                }

            } else {

                Log.info("user nao tem ficheiros " + u.getUserId());
                String fn = "/" + userId + "_" + filename;

                var disc = Discovery.getInstance().knownUrisOf("files");

                Log.info("valor do disc cona" + disc.toString());

                Log.info("link" + disc[0].toString());
                //esta a qui a merda do problema
                new RestFileClient(disc[0]).writeFile(fn, data, "");

                Log.info("valor do disc cona" + disc.toString());

                Log.info("ja escreveu supsotamente " + u.getUserId());

                String link = disc[0].toString() + fn;


                files.put(userId, new ArrayList<>());
                files.get(userId).add(new FileInfo(userId, filename, link, new HashSet<>()));

                Log.info("adicionou as merdas" + u.getUserId());

                return Result.ok(files.get(userId).get(files.get(userId).size() - 1));
            }
        } else {
            return Result.error(Result.ErrorCode.CONFLICT);
        }
        return null;
    }


    public Result<Void> deleteFile(String filename, String userId, String password) throws URISyntaxException {
        //User u = userResources.getUser(userId, password);
        User u = new RestUsersClient(Discovery.getInstance().knownUrisOf("users")[0]).getUser(userId, password);

        if (u != null) {

            if (filename == null) return Result.error(Result.ErrorCode.BAD_REQUEST);

            List<FileInfo> fi = files.get(userId);

            if (fi != null) {
                boolean found = false;
                for (FileInfo fileInfo : fi) {
                    if (fileInfo.getFilename().equals(filename)) {
                        //apagar o ficheiro
                        String fn = "/" + userId + "_" + filename;
                        String link = null;
                        var disc = Discovery.getInstance().knownUrisOf("files");

                        new RestFileClient(Discovery.getInstance().knownUrisOf("files")[0]).deleteFile(fn, "");


                        //if (res) files.get(userId).remove(i);
                        if (files.get(userId).size() == 0) files.remove(userId);
                        break;
                    }
                }
                if (!found) {
                    //nao existia esse ficheiro
                    return Result.error(Result.ErrorCode.NOT_FOUND);
                }
            } else {
                //nao existia esse ficheiro
                return Result.error(Result.ErrorCode.NOT_FOUND);
            }
        }

        return null;
    }


    public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) throws URISyntaxException {
        // User u = userResources.getUser(userId, password);
        User u = new RestUsersClient(Discovery.getInstance().knownUrisOf("users")[0]).getUser(userId, password);

        if (u != null) {

            if (filename == null || userIdShare == null) return Result.error(Result.ErrorCode.BAD_REQUEST);

            List<FileInfo> fi = files.get(userId);

            if (fi != null) {
                boolean found = false;
                for (int i = 0; i < fi.size(); i++) {
                    if (fi.get(i).getFilename().equals(filename)) {
                        files.get(userId).get(i).getSharedWith().add(userIdShare);
                        break;
                    }
                }
                if (!found) {
                    //nao existia esse ficheiro
                    return Result.error(Result.ErrorCode.NOT_FOUND);
                }
            } else {
                //nao existia esse ficheiro
                return Result.error(Result.ErrorCode.NOT_FOUND);
            }
        }

        return null;
    }


    public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) throws URISyntaxException {
        //User u = userResources.getUser(userId, password);
        User u = new RestUsersClient(Discovery.getInstance().knownUrisOf("users")[0]).getUser(userId, password);


        if (u != null) {

            if (filename == null || userIdShare == null) return Result.error(Result.ErrorCode.BAD_REQUEST);

            List<FileInfo> fi = files.get(userId);

            if (fi != null) {
                boolean found = false;
                for (int i = 0; i < fi.size(); i++) {
                    if (fi.get(i).getFilename().equals(filename)) {
                        files.get(userId).get(i).getSharedWith().remove(userIdShare);
                        break;
                    }
                }
                if (!found) {
                    //nao era partilhado
                    return Result.error(Result.ErrorCode.NOT_FOUND);
                }
            } else {
                //nao era partilhado
                return Result.error(Result.ErrorCode.NOT_FOUND);
            }
        }

        return null;
    }


    public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) throws URISyntaxException {
        //User u = userResources.getUser(userId, password);
        /*User u = new RestUsersClient(Discovery.getInstance().knownUrisOf("users")[0]).getUser(userId, password);


        if (u != null) {

            if (filename == null || accUserId == null) return Result.error(Result.ErrorCode.BAD_REQUEST);

            List<FileInfo> fi = files.get(userId);

            if (fi != null) {
                boolean found = false;
                for (int i = 0; i < fi.size(); i++) {
                    if (fi.get(i).getFilename().equals(filename)) {
                        if (fi.get(i).getOwner().equals(accUserId) || fi.get(i).getSharedWith().contains(accUserId)) {
                            //devolve os bytes do ficheiro
                            //String fn ="files/" + userId + "_" + filename;

                            Log.info("pilinha minima");
                            URI file = URI.create(fi.get(i).getFileURL());


                            Log.info("pilinha minima 2");
                            Response res = Response.temporaryRedirect(file).build();

                            Log.info("pilinha minima 3");
                            throw new WebApplicationException(res);

                            //var f = new RestFileClient(Discovery.getInstance().knownUrisOf("files")[0]).getFile(fi.get(i).getFileURL(), "");
                            //return Result.ok(f);
                        }
                    }
                }
                if (!found) {
                    //o ficherio nao exite
                    return Result.error(Result.ErrorCode.CONFLICT);
                }
            } else {
                //oficherio nao existe
                return Result.error(Result.ErrorCode.CONFLICT);
            }
        } else return Result.error(Result.ErrorCode.CONFLICT);
    */
        return null;
    }


    public Result<List<FileInfo>> lsFile(String userId, String password) throws URISyntaxException {
//        User u = userResources.getUser(userId, password);
        User u = new RestUsersClient(Discovery.getInstance().knownUrisOf("users")[0]).getUser(userId, password);


        if (u != null) {
            List<FileInfo> fi = files.get(userId);

            if (fi != null) {
                return Result.ok(fi);
            } else {
                //oficherio nao existe
                return Result.error(Result.ErrorCode.NOT_FOUND);
            }
        }

        return null;
    }
}
