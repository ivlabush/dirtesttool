package com.example.dirtestservice.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public class ErrorDto {

    private String message;
    private Date date;
}
