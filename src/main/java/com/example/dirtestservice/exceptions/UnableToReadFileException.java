package com.example.dirtestservice.exceptions;

public class UnableToReadFileException extends RuntimeException {
    public UnableToReadFileException(String message) {
        super(message);
    }
}
