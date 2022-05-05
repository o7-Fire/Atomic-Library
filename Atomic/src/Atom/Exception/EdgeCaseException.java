package Atom.Exception;

public class EdgeCaseException extends ShouldNotHappenedException{
    public EdgeCaseException() {
        super();
    }
    
    public EdgeCaseException(String message) {
        super(message);
    }
    
    public EdgeCaseException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public EdgeCaseException(Throwable cause) {
        super(cause);
    }
    
}
