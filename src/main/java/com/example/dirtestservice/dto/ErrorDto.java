package com.example.dirtestservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDto {

    private String id;
    private String url;
    private String taskId;
    private Integer statusCode;
    private String message;
    private String stacktrace;
}
