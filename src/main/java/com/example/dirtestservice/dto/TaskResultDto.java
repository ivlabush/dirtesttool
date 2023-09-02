package com.example.dirtestservice.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class TaskResultDto {

    private String id;
    private String url;
    private HttpStatus status;
}
