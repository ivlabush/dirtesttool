package com.example.dirtestservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tasks")
public class TaskEntity {

    @Id
    private String id;
    private String name;
    private String baseUrl;
}
