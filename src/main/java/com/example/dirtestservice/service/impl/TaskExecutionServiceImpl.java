package com.example.dirtestservice.service.impl;

import com.example.dirtestservice.configuration.RunConfig;
import com.example.dirtestservice.entity.TaskEntity;
import com.example.dirtestservice.exceptions.EmptyWordlistException;
import com.example.dirtestservice.exceptions.UnableToReadFileException;
import com.example.dirtestservice.exceptions.WordlistNotFoundException;
import com.example.dirtestservice.service.ErrorService;
import com.example.dirtestservice.service.TaskExecutionService;
import com.example.dirtestservice.service.TaskResultService;
import com.example.dirtestservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TaskExecutionServiceImpl implements TaskExecutionService {

    private final TaskService taskService;
    private final TaskResultService resultService;
    private final ErrorService errorService;
    private final RunConfig configuration;
    private final ResourceLoader loader;
    private final RetryTemplate retryTemplate;
    private final RestTemplate restTemplate;

    @Override
    public void startTask(String id) {
        TaskEntity task = taskService.getTaskById(id);

        List<InputStream> streams = readWordlists();

        CompletableFuture.runAsync(() -> runTestForUrl(task, formBaseUrl(task.getBaseUrl()), streams));
    }

    private String formBaseUrl(String baseUrl) {
        if (!baseUrl.endsWith("/")) baseUrl += "/";
        return baseUrl;
    }

    private List<InputStream> readWordlists() {
        List<InputStream> streams = new ArrayList<>(10);
        for (String fileName : configuration.getWordlists()) {
            try {
                InputStream stream = loader.getResource("classpath:" + fileName).getInputStream();
                if (stream.available() == 0) {
                    throw new EmptyWordlistException("File is empty");
                }
                streams.add(stream);
            } catch (IOException e) {
                throw new WordlistNotFoundException("Wordlist by name " + fileName + " wasn't found in resource folder");
            }
        }
        return streams;
    }

    private void runTestForUrl(final TaskEntity task, final String baseUrl, List<InputStream> streams) {
        ExecutorService executor = Executors.newFixedThreadPool(configuration.getThreads());

        for (InputStream s : streams) {
            try (BufferedReader r = new BufferedReader(new InputStreamReader(s))) {
                r.lines().forEach(line -> {
                    String url = baseUrl + line;
                    CompletableFuture
                            .supplyAsync(() -> submitRequest(url), executor)
                            .thenAcceptAsync(pair -> createTaskResult(task, pair), executor)
                            .exceptionally(err -> createErrorResult(task, url, err));
                });
            } catch (IOException e) {
                throw new UnableToReadFileException("Unable to read file. Exception message: " + e.getMessage());
            }
        }
    }

    private Pair<String, ResponseEntity<String>> submitRequest(String url) {
        ResponseEntity<String> response = retryTemplate.execute(args -> {
            ResponseEntity<String> r;
            try {
                log.debug("Submit request for url {}", url);
                r = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            } catch (RestClientException e) {
                log.debug("Caught exception for url {}. Exception ", url, e);
                if (e instanceof RestClientResponseException) {
                    List<Integer> retryCodes = configuration.getStop().getCodes();
                    Integer errorCode = ((RestClientResponseException) e).getStatusCode().value();
                    if (retryCodes.contains(errorCode)) {
                        throw e;
                    }
                }
                // it's just a hack
                r = new ResponseEntity<>(HttpStatusCode.valueOf(418));
            }
            return r;
        });
        log.debug("Got response with status code {} for url {}", response.getStatusCode().value(), url);
        return new Pair<>(url, response);
    }

    private void createTaskResult(TaskEntity task, Pair<String, ResponseEntity<String>> pair) {
        String url = pair.getValue0();
        int code = pair.getValue1().getStatusCode().value();
        if (configuration.getCodes().contains(code)) {
            log.debug("Create Task Result for url {}", url);
            resultService.createTaskResult(url, task, code);
            taskService.save(task);
        }
    }

    private Void createErrorResult(TaskEntity task, String url, Throwable e) {
        log.debug("Create Error for url {}", url);
        errorService.createError(task, url, e);
        taskService.save(task);
        return null;
    }
}
