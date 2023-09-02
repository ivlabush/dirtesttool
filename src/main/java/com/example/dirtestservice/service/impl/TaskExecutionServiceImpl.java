package com.example.dirtestservice.service.impl;

import com.example.dirtestservice.configuration.RunConfiguration;
import com.example.dirtestservice.entity.TaskEntity;
import com.example.dirtestservice.entity.TaskResultEntity;
import com.example.dirtestservice.exceptions.TaskNotFoundException;
import com.example.dirtestservice.exceptions.UnableToReadFileException;
import com.example.dirtestservice.exceptions.WordlistNotFoundException;
import com.example.dirtestservice.repository.TaskRepository;
import com.example.dirtestservice.repository.TaskResultRepository;
import com.example.dirtestservice.service.TaskExecutionService;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskExecutionServiceImpl implements TaskExecutionService {

    private final TaskRepository repository;
    private final TaskResultRepository resultRepository;
    private final RunConfiguration configuration;
    private final ResourceLoader loader;
    private final RestTemplate template;

    @Override
    public void startTask(String id) {
        TaskEntity task = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id=" + id + " wasn't found"));

        List<InputStream> streams = readWordlists();

        String baseUrl = task.getBaseUrl();
        if (!baseUrl.endsWith("/")) baseUrl += "/";

        runTestForUrl(task.getId(), baseUrl, streams);
    }

    private List<InputStream> readWordlists() {
        List<InputStream> streams = new ArrayList<>(10);
        for (String fileName : configuration.getWordlists()) {
            try {
                streams.add(loader.getResource("classpath:" + fileName).getInputStream());
            } catch (IOException e) {
                throw new WordlistNotFoundException("Wordlist by name " + fileName + " wasn't found in resource folder");
            }
        }
        return streams;
    }

    private void runTestForUrl(final String taskId, final String baseUrl, List<InputStream> streams) {
        ExecutorService executor = Executors.newFixedThreadPool(configuration.getThreads());

        for (InputStream s : streams) {
            try (BufferedReader r = new BufferedReader(new InputStreamReader(s))) {
                String path = r.readLine();
                CompletableFuture
                        .supplyAsync(() -> submitRequest(baseUrl + path), executor)
                        .thenAcceptAsync(pair -> createTaskResult(taskId, pair), executor);
            } catch (IOException e) {
                throw new UnableToReadFileException("Unable to read file");
            }
        }
    }

    private Pair<String, ResponseEntity<Object>> submitRequest(String url) {
        return new Pair<>(url, template.exchange(url, HttpMethod.GET, null, Object.class));
    }

    private void createTaskResult(String taskId, Pair<String, ResponseEntity<Object>> pair) {
        String url = pair.getValue0();
        int code = pair.getValue1().getStatusCode().value();
        if (configuration.getCodes().contains(code)) {
            TaskResultEntity entity = new TaskResultEntity();
            entity.setId(UUID.randomUUID().toString());
            entity.setUrl(url);
            entity.setTaskId(taskId);
            entity.setStatusCode(code);
            resultRepository.save(entity);
        }
    }
}
