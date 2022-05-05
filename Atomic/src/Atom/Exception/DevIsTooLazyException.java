package Atom.Exception;

public class DevIsTooLazyException extends EdgeCaseException{
    public DevIsTooLazyException() {
        super("Dev is too lazy to write a message");
    }
    
    public DevIsTooLazyException(String message) {
        super(message);
    }
    
    public DevIsTooLazyException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DevIsTooLazyException(Throwable cause) {
        super(cause);
    }
}
