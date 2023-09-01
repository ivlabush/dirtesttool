package com.example.dirtestservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TaskEntity {

    @Id
    private String id;
    private String name;
    private String baseUrl;
}
