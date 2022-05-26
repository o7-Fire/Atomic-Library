package Atom.API;

import Atom.Struct.PoolObject;

public class AbstractAPITracker extends CommonIPTrackerAbstract implements IPLookup, PoolObject.Object {
	String key = "";
	
	public AbstractAPITracker(String ip, String key) {
		super(ip);
		this.key = key;
		setIP(ip);
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
	
	public void setIP(String ip) {
		this.ip = ip;
		api = "https://ipgeolocation.abstractapi.com/v1/?api_key=" + key + "&ip_address=" + ip;
		reset();
	}
	
	@Override
	public void reset() {
		cache.clear();
	}
}
