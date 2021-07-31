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
    
    protected GetRealException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
