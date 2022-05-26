package Atom.API;

import Atom.Struct.InstantFuture;
import Atom.Utility.Pool;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface IPLookup extends API {
	public static final HashSet<Class<? extends IPLookup>> trustedProvider = new HashSet<>(Arrays.asList(IPAPIDotCom.class));
	
	public static void loadProvider() {
		synchronized (trustedProvider) {
			trustedProvider.addAll(Atom.Reflect.Reflect.getExtendedClass("", IPLookup.class));
			for (Class<? extends IPLookup> i : new HashSet<>(trustedProvider)) {
				try {
					IPLookup ip = i.getDeclaredConstructor(String.class).newInstance("1.1.1.1");
					ip.getDescription();
				}catch(Exception n){
					trustedProvider.remove(i);
				}
			}
		}
		
	}
	
	public static Future<Map<String, String>>[] searchAll(String ip) {
		Future<Map<String, String>>[] futures = new Future[trustedProvider.size()];
		int index = 0;
		for (Class<? extends IPLookup> i : trustedProvider) {
			try {
				IPLookup tracker = i.getDeclaredConstructor(String.class).newInstance(ip);
				futures[index] = Pool.submit(tracker::getProperty);
			}catch(Exception throwable){
				futures[index] = (InstantFuture<Map<String, String>>) () -> {
					throw new RuntimeException(throwable);
				};
			}
			index++;
		}
		return futures;
	}
	
	public static Map<String, String> search(String ip) {
		HashMap<String, String> exception = new HashMap<>();
		for (Future<Map<String, String>> s : searchAll(ip)) {
			try {
				return s.get();
			}catch(InterruptedException | ExecutionException e){
				exception.put(s.hashCode() + "", e.getMessage());
			}
		}
		return exception;
	}
	
	public static void main(String[] args) throws ExecutionException, InterruptedException {
		IPLookup tracker = new IPAPIDotCom("1.1.1.1");
		System.out.println(IPLookup.search("one.one.one.one"));
		for (Map.Entry<String, String> s : tracker.getProperty().entrySet()) {
			System.out.println(s.getKey() + ": " + s.getValue());
		}
		IPLookup.loadProvider();
		System.out.println(trustedProvider);
		Future<Map<String, String>>[] futureOne = IPLookup.searchAll("one.one.one.one");
		Future<Map<String, String>>[] futureTwo = IPLookup.searchAll("1.1.1.1");
		for (Future<Map<String, String>> s : futureOne) {
			s.get();
		}
		for (Future<Map<String, String>> s : futureTwo) {
			s.get();
		}
	}
	
	// Function to validate the IPs address.
	public static boolean isValidIPAddress(String ip) {
		return true;//everything is an ip, if you resolve it
	}
	
	@Override
	default String getDescription() {
		return "Get IP Metadata";
	}
	
	public abstract String getCountryName();
	
	public abstract String getRegionName();
	
	public abstract String getISPName();
	
	public abstract boolean isVPN();
	
	public abstract Map<String, String> getProperty();
	
	public String getIP();
}
