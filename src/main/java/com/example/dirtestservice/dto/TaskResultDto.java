package com.example.dirtestservice.dto;

import lombok.Data;

@Data
public class TaskResultDto {

    private String id;
    private String url;
    private String taskId;
    private int status;
}
