package tp1.server.resources;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import tp1.api.service.soap.FilesException;
import tp1.api.service.soap.SoapFiles;
import tp1.api.service.soap.SoapUsers;
import tp1.api.service.soap.UsersException;
import tp1.server.javas.JavaFiles;

@WebService(serviceName= SoapFiles.NAME, targetNamespace=SoapFiles.NAMESPACE, endpointInterface=SoapFiles.INTERFACE)
public class FileWebService extends SoapResources implements SoapFiles {

    final JavaFiles impl = new JavaFiles();

    @WebMethod
    public byte[] getFile(String fileId, String token) throws FilesException, UsersException {
        return  super.reTry(impl.getFile(fileId, token));
    }

    @WebMethod
    public void deleteFile(String fileId, String token) throws FilesException, UsersException {
        super.reTry(impl.deleteFile(fileId, token));
    }

    @WebMethod
    public void writeFile(String fileId, byte[] data, String token) throws FilesException, UsersException {
        super.reTry(impl.writeFile(fileId, data, token));
    }
}
