package tp1.client;

import jakarta.ws.rs.ProcessingException;
import tp1.api.service.util.Result;

import java.net.URI;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class SoapClient {

    private static Logger Log = Logger.getLogger(SoapClient.class.getName());

    protected static final int READ_TIMEOUT = 10000;
    protected static final int CONNECT_TIMEOUT = 10000;

    protected static final int RETRY_SLEEP = 1000;
    protected static final int MAX_RETRIES = 3;

    public SoapClient(URI serverURI) {
    }

    protected <T> T reTry(Supplier<T> func) {
        for (int i = 0; i < MAX_RETRIES; i++)
            try {
                return func.get();
            } catch (ProcessingException x) {
                Log.fine("ProcessingException: " + x.getMessage());
                sleep(RETRY_SLEEP);
            } catch (Exception x) {
                Log.fine("Exception: " + x.getMessage());
                x.printStackTrace();
                break;
            }
        return (T) Result.ErrorCode.BAD_REQUEST;
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException x) { // nothing to do...
        }
    }

}
