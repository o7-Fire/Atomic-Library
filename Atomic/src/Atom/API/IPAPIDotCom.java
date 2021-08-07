package Atom.API;

import Atom.Struct.PoolObject;

public class IPAPIDotCom extends CommonIPTrackerAbstract implements IPTracker, PoolObject.Object {
    
    public IPAPIDotCom(String ip) {
        super(ip);
    }
    
    @Override
    public void setIP(String ip) {
        this.ip = ip;
        api = "http://ip-api.com/json/" + ip;
        reset();
    }
    
    
    @Override
    public String getCountryName() {
        return null;
    }
    
    @Override
    public String getRegionName() {
        return null;
    }
    
    @Override
    public String getISPName() {
        return null;
    }
    
    @Override
    public boolean isVPN() {
        return false;
    }
    
    @Override
    public void reset() {
        cache.clear();
    }
}
