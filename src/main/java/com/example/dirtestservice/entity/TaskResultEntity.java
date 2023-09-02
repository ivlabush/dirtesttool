package com.example.dirtestservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "task_results")
public class TaskResultEntity {

    @Id
    private String id;
    private String url;
    private String taskId;
    private Integer statusCode;
}
