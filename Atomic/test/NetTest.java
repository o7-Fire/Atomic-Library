import Atom.API.IPLookup;
import Atom.Net.Request;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class NetTest {
    
    @Test
    public void ipLookup() throws ExecutionException, InterruptedException {
        IPLookup.main(new String[]{});
    }
    
    @Test
    public void currentIP() throws ExecutionException, InterruptedException {
        String ip = Request.getPublicIP();
        assert ip != null : "IP should not be null";
        assert !ip.isEmpty() : "IP should not be empty";
        System.out.println(ip);
    }
    
}
