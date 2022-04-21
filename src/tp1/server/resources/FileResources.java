package tp1.server.resources;

import tp1.api.service.rest.RestFiles;
import tp1.server.javas.JavaFiles;

public class FileResources extends RestResources implements RestFiles {

    final JavaFiles impl = new JavaFiles();

    @Override
    public void writeFile(String fileId, byte[] data, String token) {
        super.reTry(impl.writeFile(fileId, data, token));
    }

    @Override
    public void deleteFile(String fileId, String token) {
        super.reTry(impl.deleteFile(fileId, token));
    }

    @Override
    public byte[] getFile(String fileId, String token) {
        return  super.reTry(impl.getFile(fileId, token));
    }
}
