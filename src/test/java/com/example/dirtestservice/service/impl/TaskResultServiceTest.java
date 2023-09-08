package com.example.dirtestservice.service.impl;

import com.example.dirtestservice.entity.TaskEntity;
import com.example.dirtestservice.entity.TaskResultEntity;
import com.example.dirtestservice.exceptions.TaskNotFoundException;
import com.example.dirtestservice.exceptions.TaskResultsNotFoundException;
import com.example.dirtestservice.repository.TaskRepository;
import com.example.dirtestservice.repository.TaskResultRepository;
import com.example.dirtestservice.service.TaskResultService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class TaskResultServiceTest {

    private final TaskResultRepository repository = mock(TaskResultRepository.class);
    private final TaskRepository taskRepository = mock(TaskRepository.class);
    private final TaskResultService service = new TaskResultServiceImpl(repository, taskRepository);

    private final String url = "http://localhost:8000";

    @Test
    public void testGetAllTaskResults() {
        service.getAllTaskResults();

        verify(repository, times(1)).findAll();
    }

    @Test
    public void testGetAllTaskResultsByTaskId() {
        String id = UUID.randomUUID().toString();

        when(repository.findAllByTaskId(id))
                .thenReturn(Optional.of(Collections.singletonList(new TaskResultEntity())));

        service.getAllTaskResultsByTaskId(id);

        verify(repository, times(1)).findAllByTaskId(id);
    }

    @Test
    public void testGetAllTaskResultsByTaskIdEntityNotFound() {
        String id = UUID.randomUUID().toString();
        when(repository.findAllByTaskId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getAllTaskResultsByTaskId(id))
                .isInstanceOf(TaskResultsNotFoundException.class)
                .hasMessageContaining(id);
    }

    @Test
    public void testGetTaskResultById() {
        String id = UUID.randomUUID().toString();

        when(repository.findById(id)).thenReturn(Optional.of(new TaskResultEntity()));

        service.getTaskResultById(id);

        verify(repository, times(1)).findById(id);
    }

    @Test
    public void testTaskResultsByIdEntityNotFound() {
        String id = UUID.randomUUID().toString();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTaskResultById(id))
                .isInstanceOf(TaskResultsNotFoundException.class)
                .hasMessageContaining(id);
    }

    @Test
    public void testGetTaskResultByUrl() {
        when(repository.findAllByUrl(url))
                .thenReturn(Optional.of(Collections.singletonList(new TaskResultEntity())));

        service.getTaskResultsByUrl(url);

        verify(repository, times(1)).findAllByUrl(url);
    }

    @Test
    public void testTaskResultsByUrlEntityNotFound() {
        when(repository.findAllByUrl(url)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTaskResultsByUrl(url))
                .isInstanceOf(TaskResultsNotFoundException.class)
                .hasMessageContaining(url);
    }

    @Test
    public void testGetTaskResultByUrlContains() {
        when(repository.findAllByUrlContains(url))
                .thenReturn(Optional.of(Collections.singletonList(new TaskResultEntity())));

        service.getTaskResultsByUrlContains(url);

        verify(repository, times(1)).findAllByUrlContains(url);
    }

    @Test
    public void testTaskResultsByUrlContainsEntityNotFound() {
        when(repository.findAllByUrlContains(url)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTaskResultsByUrlContains(url))
                .isInstanceOf(TaskResultsNotFoundException.class)
                .hasMessageContaining(url);
    }

    @Test
    public void testCreateTaskResult() {
        TaskEntity entity = new TaskEntity();
        int code = 200;

        when(repository.save(any())).thenAnswer(a -> a.getArguments()[0]);

        TaskResultEntity result = service.createTaskResult(url, entity, code);

        verify(repository, times(1)).save(any());

        assertThat(result).extracting(TaskResultEntity::getId)
                .isNotNull();
        assertThat(result).extracting(TaskResultEntity::getUrl)
                .isEqualTo(url);
        assertThat(result).extracting(TaskResultEntity::getTask)
                .isEqualTo(entity);
        assertThat(result).extracting(TaskResultEntity::getStatusCode)
                .isEqualTo(code);
    }

    @Test
    public void testDeleteTaskResultsByTaskId() {
        String id = UUID.randomUUID().toString();

        TaskEntity entity = new TaskEntity();
        entity.setId(id);

        when(taskRepository.findById(id)).thenReturn(Optional.of(entity));

        service.deleteTaskResultsByTaskId(id);

        verify(taskRepository, times(1)).findById(id);
        verify(repository, times(1)).deleteAllByTask(entity);
    }

    @Test
    public void testDeleteTaskResultsByTaskIdTaskNotFound() {
        String id = UUID.randomUUID().toString();

        when(taskRepository.findById(id)).thenReturn(Optional.empty());


        assertThatThrownBy(() -> service.deleteTaskResultsByTaskId(id))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(id);

        verify(taskRepository, times(1)).findById(id);
        verify(repository, never()).deleteAllByTask(any());
    }

    @Test
    public void testDeleteAllTaskResults() {
        service.deleteAllTaskResults();

        verify(repository, times(1)).deleteAll();
    }

    @Test
    public void testDeleteAllByUrl() {
        service.deleteAllTaskResultsByUrl(url);

        verify(repository, times(1)).deleteAllByUrl(url);
    }

    @Test
    public void testDeleteAllByUrlContains() {
        service.deleteAllTaskResultsByUrlContains(url);

        verify(repository, times(1)).deleteAllByUrlContains(url);
    }
}
