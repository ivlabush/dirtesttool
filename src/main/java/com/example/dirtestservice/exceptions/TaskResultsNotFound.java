package com.example.dirtestservice.exceptions;

public class TaskResultsNotFound extends RuntimeException {
    public TaskResultsNotFound(String message) {
        super(message);
    }
}
