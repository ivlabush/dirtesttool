package com.example.dirtestservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "errors")
@Data
public class ErrorEntity {

    @Id
    private String id;
    private String url;
    private String taskId;
    private Integer statusCode;
    private String message;
    private String stacktrace;
}
