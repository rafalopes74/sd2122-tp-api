package tp1.server.rest;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.service.util.Result;

import java.util.logging.Logger;

public class Resources {
    private static final Logger Log = Logger.getLogger(Resources.class.getName());

    protected <T> T reTry(Result<T> func) {
        var h = func;
        if (h.isOK())
            return h.value();

        statusCalculation(h.error());

        return null;

    }

    private void statusCalculation(Result.ErrorCode h) {
        var t = h.toString();
        if (t.equals("CONFLICT"))
            throw new WebApplicationException(Status.CONFLICT);

        if (t.equals("NOT_FOUND"))
            throw new WebApplicationException(Status.NOT_FOUND);

        if (t.equals("BAD_REQUEST"))
            throw new WebApplicationException(Status.BAD_REQUEST);

        if (t.equals("FORBIDDEN"))
            throw new WebApplicationException(Status.FORBIDDEN);


        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

}
