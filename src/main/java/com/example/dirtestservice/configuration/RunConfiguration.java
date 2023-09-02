package com.example.dirtestservice.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "run.config")
@Data
public class RunConfiguration {

    private List<String> wordlists;
    private List<Integer> codes;
    private StopConfig stop;
    private Integer threads;
}
