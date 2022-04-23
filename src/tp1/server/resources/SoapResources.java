package tp1.server.resources;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.service.soap.UsersException;
import tp1.api.service.util.Result;

import java.util.logging.Logger;

public class SoapResources {
    private static Logger Log = Logger.getLogger(SoapResources.class.getName());

    protected  <T> T reTry(Result<T> func) throws UsersException {
        var h =  func;
        if(h.isOK())
            return h.value();

        statusCalculation(h.error());

        return null;

    }
    private void statusCalculation(Result.ErrorCode h) throws UsersException {
        var t = h.toString();

        Log.info("COMO VEM OS ERROS DO SOAP: "+ t);
        if(t.equals("CONFLICT"))
            throw new UsersException();


        throw new WebApplicationException(Status.BAD_REQUEST);
    }

}
