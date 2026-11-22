import Atom.API.IPLookup;
import Atom.Net.Request;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class NetTest {
    
    @Test
    public void ipLookup() {
        try {
            IPLookup.main(new String[]{});
        } catch (Exception e) {
            // TODO: IPLookup provider might be down, find better alternative.
            Assumptions.assumeTrue(false, "No IP lookup provider available: " + e.getMessage());
        }
    }
    
    @Test
    public void currentIP() {
        try {
            String ip = Request.getPublicIP();
            assert ip != null : "IP should not be null";
            assert !ip.isEmpty() : "IP should not be empty";
            System.out.println(ip);
        } catch (Exception e) {
            Assumptions.assumeTrue(false, "Public IP service unavailable: " + e.getMessage());
        }
    }
    
}
