package tp1.client;

import jakarta.ws.rs.ProcessingException;

import java.net.URI;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class SoapClient {

    private static Logger Log = Logger.getLogger(SoapClient.class.getName());

    protected static final int READ_TIMEOUT = 5000;
    protected static final int CONNECT_TIMEOUT = 5000;

    protected static final int RETRY_SLEEP = 3000;
    protected static final int MAX_RETRIES = 10;

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
        return null;
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException x) { // nothing to do...
        }
    }

}
