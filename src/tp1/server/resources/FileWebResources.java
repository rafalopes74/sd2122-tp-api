package tp1.server.resources;

import tp1.api.service.soap.FilesException;
import tp1.api.service.soap.SoapDirectory;
import tp1.api.service.soap.SoapFiles;

public class FileWebResources extends SoapResources implements SoapFiles {
    @Override
    public byte[] getFile(String fileId, String token) throws FilesException {
        return new byte[0];
    }

    @Override
    public void deleteFile(String fileId, String token) throws FilesException {

    }

    @Override
    public void writeFile(String fileId, byte[] data, String token) throws FilesException {

    }
}
