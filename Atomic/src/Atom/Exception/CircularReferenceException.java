package Atom.Exception;

public class CircularReferenceException extends WhatTheFuck {
    public CircularReferenceException() {
        super();
    }
    
    public CircularReferenceException(String message) {
        super(message);
    }
    
    public CircularReferenceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public CircularReferenceException(Throwable cause) {
        super(cause);
    }
    
    
}
