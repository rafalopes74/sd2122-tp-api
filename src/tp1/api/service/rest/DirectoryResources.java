package tp1.api.service.rest;

import jakarta.inject.Singleton;
import tp1.api.FileInfo;
import tp1.server.resources.JavaDirectory;
import tp1.api.service.util.Directory;

import java.net.URISyntaxException;
import java.util.List;

@Singleton
public class DirectoryResources extends Resources implements RestDirectory{

    final JavaDirectory impl = new JavaDirectory();

    public DirectoryResources() throws URISyntaxException {
    }

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) throws URISyntaxException{
        return super.reTry(impl.writeFile(filename, data, userId, password));
    }

    @Override
    public void deleteFile(String filename, String userId, String password) throws URISyntaxException{
        super.reTry(impl.deleteFile(filename, userId, password));
    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) throws URISyntaxException{
        super.reTry(impl.shareFile(filename, userId,userIdShare, password));
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) throws URISyntaxException{
        super.reTry(impl.shareFile(filename, userId,userIdShare, password));
    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) throws URISyntaxException{
        return super.reTry(impl.getFile(filename, userId,accUserId, password));
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password)throws URISyntaxException {
        return super.reTry(impl.lsFile(userId, password));
    }
}
