package tp1.server.rest;

import tp1.server.resources.JavaFiles;

import java.io.IOException;

public class FileResources extends Resources implements RestFiles {

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
    public byte[] getFile(String fileId, String token) throws IOException {
        return super.reTry(impl.getFile(fileId, token));
    }
}
