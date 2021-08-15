package Atom.Exception;

public class WhatTheFuck extends RuntimeException {
    public WhatTheFuck() {
        super();
    }
    
    public WhatTheFuck(String message) {
        super(message);
    }
    
    public WhatTheFuck(String message, Throwable cause) {
        super(message, cause);
    }
    
    public WhatTheFuck(Throwable cause) {
        super(cause);
    }
    
}
