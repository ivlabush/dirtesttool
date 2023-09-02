package com.example.dirtestservice.controller;

import com.example.dirtestservice.dto.ErrorResponseDto;
import com.example.dirtestservice.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = TaskNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleTaskNotFoundException(TaskNotFoundException e) {
        return ErrorResponseDto.builder()
                .message(e.getMessage())
                .date(new Date())
                .build();
    }

    @ExceptionHandler(value = TaskResultsNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleTaskResultsNotFoundException(TaskResultsNotFoundException e) {
        return ErrorResponseDto.builder()
                .message(e.getMessage())
                .date(new Date())
                .build();
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ErrorResponseDto> errorsDto = new ArrayList<>();
        Date date = new Date();
        e.getAllErrors().forEach(er -> errorsDto.add(ErrorResponseDto.builder()
                .message(er.getDefaultMessage())
                .date(date)
                .build()));
        return errorsDto;
    }

    @ExceptionHandler(value = UnableToReadFileException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleUnableReadFileException(UnableToReadFileException e) {
        return ErrorResponseDto.builder()
                .message(e.getMessage())
                .date(new Date())
                .build();
    }

    @ExceptionHandler(value = WordlistNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleWordlistNotFoundException(WordlistNotFoundException e) {
        return ErrorResponseDto.builder()
                .message(e.getMessage())
                .date(new Date())
                .build();
    }

    @ExceptionHandler(value = EmptyWordlistException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleEmptyFileException(EmptyWordlistException e) {
        return ErrorResponseDto.builder()
                .message(e.getMessage())
                .date(new Date())
                .build();
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleUnknownExceptions(Exception e) {
        return ErrorResponseDto.builder()
                .message("Unknown error happened on request processing")
                .date(new Date())
                .build();
    }
}
