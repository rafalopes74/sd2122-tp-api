package tp1.server.resources;

import tp1.api.FileInfo;
import tp1.api.service.soap.DirectoryException;
import tp1.api.service.soap.SoapDirectory;
import tp1.api.service.soap.UsersException;
import tp1.server.javas.JavaDirectory;

import java.net.URISyntaxException;
import java.util.List;

public class DirectoryWebService extends SoapResources implements SoapDirectory {

    final JavaDirectory impl = new JavaDirectory();

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) throws DirectoryException, URISyntaxException, UsersException {
        return super.reTry(impl.writeFile(filename, data, userId, password));

    }

    @Override
    public void deleteFile(String filename, String userId, String password) throws DirectoryException, URISyntaxException, UsersException {
        super.reTry(impl.deleteFile(filename, userId, password));
    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) throws DirectoryException, URISyntaxException, UsersException {
        super.reTry(impl.shareFile(filename, userId,userIdShare, password));
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) throws DirectoryException, URISyntaxException, UsersException {
        super.reTry(impl.unshareFile(filename, userId,userIdShare, password));
    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) throws DirectoryException, URISyntaxException, UsersException {
        return super.reTry(impl.getFile(filename, userId,accUserId, password));
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) throws DirectoryException, URISyntaxException, UsersException {
        return super.reTry(impl.lsFile(userId, password));
    }
}
