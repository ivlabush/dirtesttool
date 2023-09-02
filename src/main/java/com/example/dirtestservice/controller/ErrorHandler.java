package com.example.dirtestservice.controller;

import com.example.dirtestservice.dto.ErrorDto;
import com.example.dirtestservice.exceptions.TaskNotFoundException;
import com.example.dirtestservice.exceptions.TaskResultsNotFoundException;
import com.example.dirtestservice.exceptions.UnableToReadFileException;
import com.example.dirtestservice.exceptions.WordlistNotFoundException;
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
    public ErrorDto handleTaskNotFoundException(TaskNotFoundException e) {
        return ErrorDto.builder()
                .message(e.getMessage())
                .date(new Date())
                .build();
    }

    @ExceptionHandler(value = TaskResultsNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleTaskResultsNotFoundException(TaskResultsNotFoundException e) {
        return ErrorDto.builder()
                .message(e.getMessage())
                .date(new Date())
                .build();
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ErrorDto> errorsDto = new ArrayList<>();
        Date date = new Date();
        e.getAllErrors().forEach(er -> errorsDto.add(ErrorDto.builder()
                .message(er.getDefaultMessage())
                .date(date)
                .build()));
        return errorsDto;
    }

    @ExceptionHandler(value = UnableToReadFileException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleUnableReadFileException(UnableToReadFileException e) {
        return ErrorDto.builder()
                .message(e.getMessage())
                .date(new Date())
                .build();
    }

    @ExceptionHandler(value = WordlistNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleWordlistNotFoundException(WordlistNotFoundException e) {
        return ErrorDto.builder()
                .message(e.getMessage())
                .date(new Date())
                .build();
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleUnknownExceptions(Exception e) {
        return ErrorDto.builder()
                .message("Unknown error happened on request processing")
                .date(new Date())
                .build();
    }
}
