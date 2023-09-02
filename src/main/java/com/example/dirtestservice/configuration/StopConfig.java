package com.example.dirtestservice.configuration;

import lombok.Data;

import java.util.List;

@Data
public class StopConfig {
    private List<Integer> codes;
    private Integer backoff;
    private Integer retries;
}
