package Atom.Exception;

public class GetRealException extends RuntimeException {
    public GetRealException() {
        this("Get Real");
    }
    
    public GetRealException(String message) {
        super(message);
    }
    
    public GetRealException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public GetRealException(Throwable cause) {
        super(cause);
    }
    
    
}
