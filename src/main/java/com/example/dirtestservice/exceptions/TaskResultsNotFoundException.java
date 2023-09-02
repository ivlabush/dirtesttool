package com.example.dirtestservice.exceptions;

public class TaskResultsNotFoundException extends RuntimeException {
    public TaskResultsNotFoundException(String message) {
        super(message);
    }
}
