package Atom.Exception;

public class ShouldNotHappenedException extends WhatTheFuck {
    public ShouldNotHappenedException() {
        super();
    }
    
    public ShouldNotHappenedException(String message) {
        super(message);
    }
    
    public ShouldNotHappenedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ShouldNotHappenedException(Throwable cause) {
        super(cause);
    }
    
    
}
