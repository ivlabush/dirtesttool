package com.example.dirtestservice.controller;

import com.example.dirtestservice.dto.ErrorDto;
import com.example.dirtestservice.service.ErrorService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("/errors")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ErrorController {

    private final ModelMapper mapper;
    private final ErrorService service;

    private static final Type type = new TypeToken<List<ErrorDto>>() {
    }.getType();

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ErrorDto> getAllErrors() {
        return mapper.map(service.getAllErrors(), type);
    }

    @GetMapping("/task/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ErrorDto> getAllErrorsByTaskId(@PathVariable @NotBlank String id) {
        return mapper.map(service.getErrorsByTaskId(id), type);
    }

    @GetMapping("/statuscode/{code}")
    @ResponseStatus(HttpStatus.OK)
    public List<ErrorDto> getAllErrorsByStatusCode(@PathVariable @NotNull Integer code) {
        return mapper.map(service.getErrorsByStatusCode(code), type);
    }

    @GetMapping("/url")
    @ResponseStatus(HttpStatus.OK)
    public List<ErrorDto> getAllErrorsByUrl(@RequestParam @NotBlank String url) {
        return mapper.map(service.getErrorsByUrl(url), type);
    }

    @GetMapping("/url/contains")
    @ResponseStatus(HttpStatus.OK)
    public List<ErrorDto> getAllErrorsByUrlContains(@RequestParam @NotBlank String url) {
        return mapper.map(service.getErrorsByUrlContains(url), type);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllErrors() {
        service.deleteAllErrors();
    }

    @DeleteMapping("/task/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllErrorsByTaskId(@PathVariable @NotBlank String id) {
        service.deleteErrorsByTaskId(id);
    }

    @DeleteMapping("/url")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllErrorsByUrl(@RequestParam @NotBlank String url) {
        service.deleteErrorsByUrl(url);
    }

    @DeleteMapping("/url/contains")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllErrorsByUrlContains(@RequestParam @NotBlank String url) {
        service.deleteErrorsByUrlContains(url);
    }
}
