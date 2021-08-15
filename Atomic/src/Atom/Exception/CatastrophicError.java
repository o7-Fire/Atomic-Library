package Atom.Exception;

public class CatastrophicError extends Error {
    public CatastrophicError() {
        this("You're a " + CatastrophicError.class.getSimpleName());
    }
    
    public CatastrophicError(String message) {
        super(message);
    }
    
    public CatastrophicError(String message, Throwable cause) {
        super(message, cause);
    }
    
    public CatastrophicError(Throwable cause) {
        super(cause);
    }
    
    
}
