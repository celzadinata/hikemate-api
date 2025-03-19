package com.enigmacamp.utils.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
