package com.example.dirtestservice.exceptions;

public class WordlistNotFoundException extends RuntimeException {
    public WordlistNotFoundException(String message) {
        super(message);
    }
}
