package tp1.server.resources;

import jakarta.inject.Singleton;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestDirectory;
import tp1.server.javas.JavaDirectory;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

@Singleton
public class DirectoryResources extends RestResources implements RestDirectory {

    final JavaDirectory impl = new JavaDirectory();

    public DirectoryResources(){
    }

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) throws URISyntaxException, MalformedURLException {
        return super.reTry(impl.writeFile(filename, data, userId, password));

    }

    @Override
    public void deleteFile(String filename, String userId, String password) throws URISyntaxException, MalformedURLException {
        super.reTry(impl.deleteFile(filename, userId, password));
    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) throws URISyntaxException, MalformedURLException {
        super.reTry(impl.shareFile(filename, userId,userIdShare, password));
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) throws URISyntaxException, MalformedURLException {
        super.reTry(impl.unshareFile(filename, userId,userIdShare, password));
    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) throws URISyntaxException, MalformedURLException {
        return super.reTry(impl.getFile(filename, userId,accUserId, password));

    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) throws URISyntaxException, MalformedURLException {
        return super.reTry(impl.lsFile(userId, password));
    }

    @Override
    public void deleteFilesUser(String userId, String password) throws URISyntaxException, MalformedURLException {
        super.reTry(impl.deleteFilesUser(userId, password));
    }
}
