package com.example.dirtestservice.service.impl;

import com.example.dirtestservice.entity.ErrorEntity;
import com.example.dirtestservice.entity.TaskEntity;
import com.example.dirtestservice.exceptions.ErrorNotFoundException;
import com.example.dirtestservice.repository.ErrorRepository;
import com.example.dirtestservice.repository.TaskRepository;
import com.example.dirtestservice.service.ErrorService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class ErrorServiceTest {

    private final ErrorRepository errorRepository = mock(ErrorRepository.class);
    private final TaskRepository taskRepository = mock(TaskRepository.class);

    private final ErrorService service = new ErrorServiceImpl(errorRepository, taskRepository);

    private final Random random = new Random();
    private final String url = "http://localhost:8080";

    @Test
    public void testGetAllErrors() {
        service.getAllErrors();
        verify(errorRepository, times(1)).findAll();
    }

    @Test
    public void testGetErrorsByTaskId() {
        String id = UUID.randomUUID().toString();

        when(errorRepository.findAllByTaskId(id))
                .thenReturn(Collections.singletonList(new ErrorEntity()));

        service.getErrorsByTaskId(id);
        verify(errorRepository, times(1)).findAllByTaskId(id);
    }

    @Test
    public void testGetErrorsByTaskIdEntityNotFound() {
        String id = UUID.randomUUID().toString();
        when(errorRepository.findAllByTaskId(id)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> service.getErrorsByTaskId(id))
                .isInstanceOf(ErrorNotFoundException.class)
                .hasMessageContaining(id);
    }

    @Test
    public void testGetErrorsByStatusCode() {
        Integer id = random.nextInt();

        when(errorRepository.findAllByStatusCode(id))
                .thenReturn(Collections.singletonList(new ErrorEntity()));

        service.getErrorsByStatusCode(id);
        verify(errorRepository, times(1)).findAllByStatusCode(id);
    }

    @Test
    public void testGetErrorsByStatusCodeEntityNotFound() {
        Integer id = random.nextInt();

        when(errorRepository.findAllByStatusCode(id)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> service.getErrorsByStatusCode(id))
                .isInstanceOf(ErrorNotFoundException.class)
                .hasMessageContaining(String.valueOf(id));
    }

    @Test
    public void testGetErrorsByUrl() {
        when(errorRepository.findAllByUrl(url))
                .thenReturn(Collections.singletonList(new ErrorEntity()));

        service.getErrorsByUrl(url);
        verify(errorRepository, times(1)).findAllByUrl(url);
    }

    @Test
    public void testGetErrorsByUrlEntityNotFound() {
        when(errorRepository.findAllByUrl(url)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> service.getErrorsByUrl(url))
                .isInstanceOf(ErrorNotFoundException.class)
                .hasMessageContaining(url);
    }

    @Test
    public void testGetErrorsByUrlContains() {
        when(errorRepository.findAllByUrlContains(url))
                .thenReturn(Collections.singletonList(new ErrorEntity()));

        service.getErrorsByUrlContains(url);
        verify(errorRepository, times(1)).findAllByUrlContains(url);
    }

    @Test
    public void testGetErrorsByUrlContainsEntityNotFound() {
        when(errorRepository.findAllByUrlContains(url)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> service.getErrorsByUrlContains(url))
                .isInstanceOf(ErrorNotFoundException.class)
                .hasMessageContaining(url);
    }

    @Test
    public void testCreateErrorFromRestClientException() {
        HttpClientErrorException cause = HttpClientErrorException
                .create("A very long message more than 255 symbols !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!",
                        HttpStatusCode.valueOf(400), "Status Text", new HttpHeaders(), new byte[]{}, null);
        StackTraceElement[] stackTrace = new StackTraceElement[300];
        for (int i = 0; i < 300; i++) {
            stackTrace[i] = new StackTraceElement("Class Loader Name", "Module Name", "1",
                    "Declaring Class", "Method Name", "File name", i);
        }

        cause.setStackTrace(stackTrace);

        RuntimeException exception = new RuntimeException(cause);

        TaskEntity entity = new TaskEntity();
        entity.setBaseUrl(url);

        when(errorRepository.save(any())).thenAnswer(a -> a.getArguments()[0]);

        ErrorEntity error = service.createError(entity, url, exception);
        verify(errorRepository, times(1)).save(any());

        assertThat(error).extracting(ErrorEntity::getUrl)
                .isEqualTo(entity.getBaseUrl());
        assertThat(error).extracting(ErrorEntity::getStatusCode)
                .isEqualTo(((RestClientResponseException) exception.getCause()).getStatusCode().value());
        assertThat(error).extracting(ErrorEntity::getMessage)
                .extracting(String::length)
                .isEqualTo(255);
        assertThat(error).extracting(ErrorEntity::getStacktrace)
                .extracting(String::length)
                .isEqualTo(255);
        assertThat(error).extracting(ErrorEntity::getTask)
                .isEqualTo(entity);
    }

    @Test
    public void testCreateErrorFromCommonException() {
        String causeMessage = "Message";
        StackTraceElement[] stackTrace = new StackTraceElement[]{new StackTraceElement("Class Loader Name", "Module Name", "1",
                "Declaring Class", "Method Name", "File name", 1)};

        RuntimeException cause = new RuntimeException(causeMessage);
        cause.setStackTrace(stackTrace);

        RuntimeException exception = new RuntimeException(cause);

        TaskEntity entity = new TaskEntity();
        entity.setBaseUrl(url);

        when(errorRepository.save(any())).thenAnswer(a -> a.getArguments()[0]);

        ErrorEntity error = service.createError(entity, url, exception);
        verify(errorRepository, times(1)).save(any());

        assertThat(error).extracting(ErrorEntity::getUrl)
                .isEqualTo(entity.getBaseUrl());
        assertThat(error).extracting(ErrorEntity::getStatusCode)
                .isEqualTo(0);
        assertThat(error).extracting(ErrorEntity::getMessage)
                .isEqualTo(causeMessage);
        assertThat(error).extracting(ErrorEntity::getStacktrace)
                .isEqualTo(ExceptionUtils.getStackTrace(cause));
        assertThat(error).extracting(ErrorEntity::getTask)
                .isEqualTo(entity);
    }

    @Test
    public void testDeleteAllErrors() {
        service.deleteAllErrors();
        verify(errorRepository, times(1)).deleteAll();
    }

    @Test
    public void testDeleteErrorsByTaskId() {
        String id = UUID.randomUUID().toString();
        TaskEntity entity = new TaskEntity();

        when(taskRepository.findById(id)).thenReturn(Optional.of(entity));

        service.deleteErrorsByTaskId(id);

        verify(taskRepository, times(1)).findById(id);
        verify(errorRepository, times(1)).deleteAllByTask(entity);
    }

    @Test
    public void testDeleteErrorsByUrl() {
        service.deleteErrorsByUrl(url);
        verify(errorRepository, times(1)).deleteAllByUrl(url);
    }

    @Test
    public void testDeleteErrorsByUrlContains() {
        service.deleteErrorsByUrlContains(url);
        verify(errorRepository, times(1)).deleteAllByUrlContains(url);
    }
}
