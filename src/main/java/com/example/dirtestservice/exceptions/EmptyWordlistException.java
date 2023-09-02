package com.example.dirtestservice.exceptions;

public class EmptyWordlistException extends RuntimeException {
    public EmptyWordlistException(String message) {
        super(message);
    }
}
