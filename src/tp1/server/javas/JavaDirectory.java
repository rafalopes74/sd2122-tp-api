package tp1.server.javas;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.api.FileInfo;

import tp1.api.service.util.Result;
import tp1.client.RestFilesClient;
import tp1.client.RestUsersClient;
import tp1.server.util.Discovery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;

public class JavaDirectory {

    private static final Logger Log = Logger.getLogger(JavaDirectory.class.getName());
    private final Map<String, Map<String,FileInfo>> files = new HashMap<>();

    public JavaDirectory() {
    }

    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) throws URISyntaxException {
        var u = (new RestUsersClient(Discovery.getInstance().knownUrisOf("users")[0])).getUser(userId, password);

        if(!u.isOK()) return Result.error(u.error());


        String fid = userId + "_" + filename;
        String link =  Discovery.getInstance().knownUrisOf("files")[0] + "/files/" + fid;




        var f = (new RestFilesClient(Discovery.getInstance().knownUrisOf("files")[0])).writeFile(fid, data, "");

        if(!f.isOK()) return Result.error(f.error());

        if(!files.containsKey(userId)) {
            files.put(userId, new HashMap<>());
            files.get(userId).put(filename, new FileInfo(userId, filename, link, new HashSet<>()));
        }else{
            if(!files.get(userId).containsKey(filename))
                files.get(userId).put(filename, new FileInfo(userId, filename, link, new HashSet<>()));
        }

        return Result.ok(files.get(userId).get(filename));
    }

    public Result<Void> deleteFile(String filename, String userId, String password) throws URISyntaxException {
        var u = (new RestUsersClient(Discovery.getInstance().knownUrisOf("users")[0])).getUser(userId, password);

        if(!u.isOK()) return Result.error(u.error());

        String fid = userId + "_" + filename;

        var f = (new RestFilesClient(Discovery.getInstance().knownUrisOf("files")[0])).deleteFile(fid, "");

        if(!f.isOK()) return Result.error(f.error());

        files.get(userId).remove(filename);

        return Result.ok();
    }


    public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) throws URISyntaxException {
        var u = (new RestUsersClient(Discovery.getInstance().knownUrisOf("users")[0])).getUser(userId, password);

        if(!u.isOK()) return Result.error(u.error());

        var acU = (new RestUsersClient(Discovery.getInstance().knownUrisOf("users")[0])).getUser(userIdShare, null);

        if(!files.containsKey(userId)  || acU.error().equals(Result.ErrorCode.NOT_FOUND)|| !files.get(userId).containsKey(filename)) return Result.error(Result.ErrorCode.NOT_FOUND);

        Set<String> sharedWith = files.get(userId).get(filename).getSharedWith();
        sharedWith.add(userIdShare);
        files.get(userId).get(filename).setSharedWith(sharedWith);

        return Result.ok();
    }


    public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) throws URISyntaxException {
        var u = (new RestUsersClient(Discovery.getInstance().knownUrisOf("users")[0])).getUser(userId, password);

        if(!u.isOK()) return Result.error(u.error());

        var acU = (new RestUsersClient(Discovery.getInstance().knownUrisOf("users")[0])).getUser(userIdShare, null);

        if(!files.containsKey(userId)  || acU.error().equals(Result.ErrorCode.NOT_FOUND)|| !files.get(userId).containsKey(filename))
            return Result.error(Result.ErrorCode.NOT_FOUND);

        Set<String> sharedWith = files.get(userId).get(filename).getSharedWith();

        sharedWith.remove(userIdShare);
        files.get(userId).get(filename).setSharedWith(sharedWith);

        return Result.ok();

    }

    public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) throws URISyntaxException {


        var u = (new RestUsersClient(Discovery.getInstance().knownUrisOf("users")[0])).getUser(accUserId, password);

        if(!u.isOK()) return Result.error(u.error());

        if(!files.containsKey(userId)) return Result.error(Result.ErrorCode.NOT_FOUND);

        if(!files.get(userId).containsKey(filename)) return Result.error(Result.ErrorCode.NOT_FOUND);

        if(!files.get(userId).get(filename).getSharedWith().contains(accUserId) && !accUserId.equals(userId)) return Result.error(Result.ErrorCode.BAD_REQUEST);

        URI file = URI.create(files.get(userId).get(filename).getFileURL()) ;

        Response res = Response.temporaryRedirect(file).build();

        throw new WebApplicationException(res);

        //if(!f.isOK())return Result.error(f.error());
        //return Result.ok(f.value());
    }

    public Result<List<FileInfo>> lsFile(String userId, String password) throws URISyntaxException {
        var u = (new RestUsersClient(Discovery.getInstance().knownUrisOf("users")[0])).getUser(userId, password);

        if(!u.isOK()) return Result.error(u.error());


        List<FileInfo> lfi = new ArrayList<>();
        if(files.containsKey(userId)) {
            Iterator<FileInfo> itF = files.get(userId).values().iterator();
            while(itF.hasNext()){
                lfi.add(itF.next());
            }
        }

        Iterator<Map<String, FileInfo>> itU = files.values().iterator();
        while(itU.hasNext()){
            Iterator<FileInfo> itF = itU.next().values().iterator();
            while(itF.hasNext()){
                FileInfo fi = itF.next();
                if(fi.getSharedWith().contains(userId)) lfi.add(fi);
            }
        }

        return Result.ok(lfi);

    }

    public Result<Void> deleteUser(String userId, String password) throws URISyntaxException {
        if(files.containsKey(userId)){
            Log.info("numero de ficheiros a eliminar" + files.get(userId).keySet().size());
            Iterator<Map.Entry<String, FileInfo>> f = files.get(userId).entrySet().iterator();   //files.get(userId).keySet().iterator();
            while(f.hasNext()){
                var h = f.next().getKey();
                Log.info("NOME DO FICHEIRO A ELIMINAR" + h);
                String fid = userId + "_" + h;
                var p = (new RestFilesClient(Discovery.getInstance().knownUrisOf("files")[0])).deleteFile(fid, "");

                //var del = this.deleteFile(fid, userId, password);
            }
            files.remove(userId);
        }


        return Result.ok();
    }

    private void errorCode(int status){
        switch(status) {

            case 404:
                Log.info("print ya é aqui");


        }
    }

}
