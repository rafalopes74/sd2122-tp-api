package tp1.api.service.rest;

import java.util.*;
import java.util.logging.Logger;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import tp1.api.service.soap.UsersException;
import tp1.api.service.util.Result;
import jakarta.ws.rs.core.Response.Status;

import java.util.function.Supplier;

public class Resources {
    private static Logger Log = Logger.getLogger(Resources.class.getName());

    protected  <T> T reTry(Result<T> func) {
        var h =  func;
        if(h.isOK())
            return h.value();

        statusCalculation(h.error());

        return null;

    }
    private void statusCalculation(Result.ErrorCode h){
        var t = h.toString();
        if(t.equals("CONFLICT"))
            throw new WebApplicationException(Status.CONFLICT);

        if(t.equals("NOT_FOUND"))
            throw new WebApplicationException(Status.NOT_FOUND);

        if(t.equals("BAD_REQUEST"))
            throw new WebApplicationException(Status.BAD_REQUEST);

        if(t.equals("FORBIDDEN"))
            throw new WebApplicationException(Status.FORBIDDEN);


        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

}
