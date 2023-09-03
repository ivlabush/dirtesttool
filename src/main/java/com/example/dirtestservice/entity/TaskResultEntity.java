package com.example.dirtestservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "task_results")
public class TaskResultEntity {

    @Id
    private String id;
    private String url;
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private TaskEntity task;
    private Integer statusCode;
}
