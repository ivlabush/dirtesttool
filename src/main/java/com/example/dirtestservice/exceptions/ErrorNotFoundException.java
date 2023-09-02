package com.example.dirtestservice.exceptions;

public class ErrorNotFoundException extends RuntimeException {
    public ErrorNotFoundException(String message) {
        super(message);
    }
}
