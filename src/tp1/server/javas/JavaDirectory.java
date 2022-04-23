package tp1.server.javas;

import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.api.FileInfo;

import tp1.api.User;
import tp1.api.service.util.Result;
import tp1.client.*;
import tp1.server.util.Discovery;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

@Singleton
public class JavaDirectory {

    private static final Logger Log = Logger.getLogger(JavaDirectory.class.getName());
    private final String USERS_SERVICE_NAME = "users";
    private final String FILES_SERVICE_NAME = "files";
    private final String FILE_ID_FORMAT = "%s_%s";
    private final String FILE_PATH = "/files/";
    private final String FILE_URL_FORMAT = "%s" + FILE_PATH + "%s";


    private final ConcurrentMap<String, ConcurrentMap<String, FileInfo>> files;
    private final ConcurrentMap<URI, Integer> uriToNumberOfFiles;

    private ClientFactory factory = new ClientFactory();

    public JavaDirectory() {
        this.files = new ConcurrentHashMap<>();
        this.uriToNumberOfFiles = new ConcurrentHashMap<>();
    }

    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) throws URISyntaxException, MalformedURLException {
        Log.info("CHECKPOINT 1");

        var u = this.getUserAux(userId, password);
        if (!u.isOK())
            return Result.error(u.error());

        Log.info("CHECKPOINT 2");


        ConcurrentMap<String, FileInfo> filesMap = files.get(userId);
        String fileId = this.getFileId(userId, filename);
        URI uri = null;
        String fileUrl = null;


        if (filesMap != null && filesMap.get(filename) != null) {
            FileInfo fi = filesMap.get(filename);
            fileUrl = fi.getFileURL();
            uri = this.getUriFromUrl(fileUrl, fileId);

            var f = ClientFactory.getFiles(URI.create(fileUrl), uri).writeFile(fileId, data, "");
            if (!f.isOK())
                return Result.error(f.error());

            Log.info("CHECKPOINT 3");

            files.get(userId).put(filename, new FileInfo(userId, filename, fileUrl, fi.getSharedWith()));
        } else {

            ConcurrentLinkedQueue<URI> aux = new ConcurrentLinkedQueue<>();
            aux.addAll((Discovery.knownUrisOf(FILES_SERVICE_NAME)));
            uri = this.getLeastUsedServer(aux);
            fileUrl = String.format(FILE_URL_FORMAT, uri, fileId);


            var f = ClientFactory.getFiles(URI.create(fileUrl),uri).writeFile(fileId, data, "");

            if (!f.isOK())
                return Result.error(f.error());

            uriToNumberOfFiles.put(uri, uriToNumberOfFiles.get(uri) + 1);

            if (!files.containsKey(userId))
                files.put(userId, new ConcurrentHashMap<>());

            files.get(userId).put(filename, new FileInfo(userId, filename, fileUrl, new HashSet<>()));
        }

        return Result.ok(files.get(userId).get(filename));
    }

    public Result<Void> deleteFile(String filename, String userId, String password) throws URISyntaxException, MalformedURLException {
        var u = this.getUserAux(userId, password);
        if (!u.isOK())
            return Result.error(u.error());

        return this.deleteFileAux(userId, filename);
    }


    public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) throws URISyntaxException, MalformedURLException {
        var u = this.getUserAux(userId, password);
        if (!u.isOK())
            return Result.error(u.error());

        var acU = this.getUserAux(userIdShare, null);

        if (!files.containsKey(userId) || !files.get(userId).containsKey(filename) || acU.error().equals(Result.ErrorCode.NOT_FOUND))
            return Result.error(Result.ErrorCode.NOT_FOUND);

        Set<String> sharedWith = files.get(userId).get(filename).getSharedWith();
        sharedWith.add(userIdShare);
        files.get(userId).get(filename).setSharedWith(sharedWith);

        return Result.ok();
    }


    public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) throws URISyntaxException, MalformedURLException {
        var u = this.getUserAux(userId, password);
        if (!u.isOK())
            return Result.error(u.error());

        var acU = getUserAux(userIdShare, null);

        if (!files.containsKey(userId) || !files.get(userId).containsKey(filename) || acU.error().equals(Result.ErrorCode.NOT_FOUND))
            return Result.error(Result.ErrorCode.NOT_FOUND);

        Set<String> sharedWith = files.get(userId).get(filename).getSharedWith();
        sharedWith.remove(userIdShare);
        files.get(userId).get(filename).setSharedWith(sharedWith);

        return Result.ok();

    }

    public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) throws URISyntaxException, MalformedURLException {


        var u = this.getUserAux(accUserId, password);
        if (!u.isOK())
            return Result.error(u.error());

        if (!files.containsKey(userId) || !files.get(userId).containsKey(filename))
            return Result.error(Result.ErrorCode.NOT_FOUND);

        if (!files.get(userId).get(filename).getSharedWith().contains(accUserId) && !accUserId.equals(userId))
            return Result.error(Result.ErrorCode.BAD_REQUEST);

        String url = files.get(userId).get(filename).getFileURL();
        String fileId = this.getFileId(userId, filename);

        if(this.getUriFromUrl(url, fileId ).toString().contains("rest") && Discovery.getInstance().knownUrisOf("directory").toArray()[0].toString().contains("rest"))
          {

              Response res = Response.temporaryRedirect(URI.create(url)).build();

              throw new WebApplicationException(res);
           }



        URI uri = this.getUriFromUrl(url, fileId);

        var res = ClientFactory.getFiles(URI.create(url), uri).getFile(fileId, "");
        if(res.isOK()) return Result.ok(res.value());

        return Result.error(res.error());

    }

    public Result<List<FileInfo>> lsFile(String userId, String password) throws URISyntaxException, MalformedURLException {
        var u = this.getUserAux(userId, password);
        if (!u.isOK())
            return Result.error(u.error());

        List<FileInfo> lfi = new ArrayList<>();
        if (files.containsKey(userId))
            lfi.addAll(files.get(userId).values());

        for (Map<String, FileInfo> stringFileInfoMap : files.values())
            for (FileInfo fi : stringFileInfoMap.values())
                if (fi.getSharedWith().contains(userId))
                    lfi.add(fi);

        return Result.ok(lfi);

    }

    private Result<User> getUserAux(String userId, String password) throws URISyntaxException, MalformedURLException {
        //return new RestUsersClient(Discovery.getInstance().knownUrisOf(USERS_SERVICE_NAME)[0]).getUser(userId, password);

        return ClientFactory.getClient().getUser(userId, password);
    }
    public Result<Void> deleteFilesUser(String userId, String password) throws URISyntaxException, MalformedURLException {
        var u = this.getUserAux(userId, password);
        if (!u.isOK())
            return Result.error(u.error());


        Iterator<String> it = files.get(userId).keySet().iterator();
        while (it.hasNext())
            this.deleteFileAux(userId, it.next());

        files.remove(userId);

        return Result.ok();
    }

    private Result<Void> deleteFileAux(String userId, String filename) throws MalformedURLException, URISyntaxException {
        String fileId = this.getFileId(userId, filename);

        Log.info("CHECKY 1");
        if (files.get(userId) == null || files.get(userId).get(filename) == null)
            return Result.error(Result.ErrorCode.NOT_FOUND);

        Log.info("CHECKY 2");
        String url = files.get(userId).get(filename).getFileURL();
        URI uri = getUriFromUrl(url, fileId);

        Log.info("CHECKY 3");
        //var f = (new RestFilesClient(uri)).deleteFile(fileId, "");
        var f = ClientFactory.getFiles(URI.create(url),uri).deleteFile(fileId, "");

        if (!f.isOK())
            return Result.error(f.error());

        Log.info("CHECKY 4");
        uriToNumberOfFiles.put(uri, uriToNumberOfFiles.get(uri) - 1);
        files.get(userId).remove(filename);
        Log.info("CHECKY 5");
        return Result.ok();
    }

    private URI getUriFromUrl(String url, String fileId) {
        return URI.create(url.substring(0, url.length() - (fileId.length() + FILE_PATH.length())));
    }

    private String getFileId(String userId, String filename) {
        return String.format(FILE_ID_FORMAT, userId, filename);
    }

    private URI getLeastUsedServer(ConcurrentLinkedQueue<URI> fileServersURI) {
        URI leastUsedServer = null;
        Iterator<URI> iUri = fileServersURI.iterator();
        while(iUri.hasNext()){
            URI uri = iUri.next();
            if (!uriToNumberOfFiles.containsKey(uri))
                uriToNumberOfFiles.put(uri, 0);
            if (leastUsedServer == null || uriToNumberOfFiles.get(uri) < uriToNumberOfFiles.get(leastUsedServer))
                leastUsedServer = uri;
        }
        return leastUsedServer;
    }

    private void errorCode(int status){
        switch(status) {

            case 404:
                Log.info("print ya é aqui");


        }
    }

}
