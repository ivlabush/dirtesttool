package com.example.dirtestservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class ErrorDto {

    private String message;
    private Date date;
}
