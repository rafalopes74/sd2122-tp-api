package tp1.api.service.soap;

import jakarta.ws.rs.WebApplicationException;
import tp1.api.service.util.Result;

import java.util.logging.Logger;


import java.util.*;
import java.util.logging.Logger;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import tp1.api.service.util.Result;
import jakarta.ws.rs.core.Response.Status;

import java.util.function.Supplier;

public class SoapUsersWeb {


    private static Logger Log = Logger.getLogger(tp1.api.service.rest.Resources.class.getName());

    protected  <T> T reTry(Result<T> func) throws UsersException {
        var h =  func;
        if(h.isOK())
            return h.value();

        this.statusCalculation(h.error());

        return null;

    }
    private void statusCalculation(Result.ErrorCode h) throws UsersException {
        var t = h.toString();
        if(t.equals("CONFLICT"))
            throw new UsersException("conflict");

        if(t.equals("NOT_FOUND"))
            throw new UsersException("not_found");

        if(t.equals("BAD_REQUEST"))
            throw new UsersException("bad_request");

        if(t.equals("FORBIDDEN"))
            throw new UsersException("forbidden");


        throw new UsersException("not implemented");
    }

}
