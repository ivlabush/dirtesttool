package com.example.dirtestservice.service.impl;

import com.example.dirtestservice.configuration.CommonConfig;
import com.example.dirtestservice.dto.TaskDto;
import com.example.dirtestservice.entity.TaskEntity;
import com.example.dirtestservice.exceptions.TaskNotFoundException;
import com.example.dirtestservice.repository.TaskRepository;
import com.example.dirtestservice.service.TaskService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private final TaskRepository repository = mock(TaskRepository.class);
    private final ModelMapper mapper = new CommonConfig(null).mapper();
    private final TaskService service = new TaskServiceImpl(repository, mapper);

    private final String url = "http://localhost:8000";


    @Test
    public void testGetAllTasks() {
        service.getAllTasks();
        verify(repository, times(1)).findAll();
    }


    @Test
    public void testTaskByTaskId() {
        String id = UUID.randomUUID().toString();

        when(repository.findById(id)).thenReturn(Optional.of(new TaskEntity()));

        service.getTaskById(id);
        verify(repository, times(1)).findById(id);
    }

    @Test
    public void testTaskByTaskIdEntityNotFound() {
        String id = UUID.randomUUID().toString();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTaskById(id))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(id);
    }

    @Test
    public void testTaskByBaseUrl() {
        when(repository.findAllByBaseUrl(url))
                .thenReturn(Collections.singletonList(new TaskEntity()));

        service.getTaskByUrl(url);
        verify(repository, times(1)).findAllByBaseUrl(url);
    }

    @Test
    public void testTaskByBaseUrlEntityNotFound() {
        when(repository.findAllByBaseUrl(url)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> service.getTaskByUrl(url))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(url);
    }

    @Test
    public void testTaskByBaseUrlContains() {
        when(repository.findTasksByUrlContains(url))
                .thenReturn(Collections.singletonList(new TaskEntity()));

        service.getTasksByUrlContains(url);
        verify(repository, times(1)).findTasksByUrlContains(url);
    }

    @Test
    public void testTaskByBaseUrlContainsEntityNotFound() {
        when(repository.findTasksByUrlContains(url)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> service.getTasksByUrlContains(url))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(url);
    }

    @Test
    public void testCreateTask() {
        TaskDto dto = new TaskDto();
        dto.setBaseUrl(url);

        when(repository.save(any())).thenAnswer(a -> a.getArguments()[0]);

        TaskEntity entity = service.createTask(dto);
        verify(repository, times(1)).save(any());

        assertThat(entity).extracting(TaskEntity::getId)
                .isNotNull();
        assertThat(entity).extracting(TaskEntity::getName)
                .isEqualTo("task-" + entity.getId());
        assertThat(entity).extracting(TaskEntity::getBaseUrl)
                .isEqualTo(url);
    }

    @Test
    public void testUpdateTask() {
        String id = UUID.randomUUID().toString();
        TaskDto dto = new TaskDto();
        dto.setBaseUrl(url);

        TaskEntity entity = new TaskEntity();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenAnswer(a -> a.getArguments()[0]);

        entity = service.updateTask(id, dto);

        verify(repository, times(1)).findById(id);

        assertThat(entity).extracting(TaskEntity::getBaseUrl)
                .isEqualTo(url);
    }

    @Test
    public void testSave() {
        TaskEntity entity = new TaskEntity();

        service.save(entity);

        verify(repository, times(1)).save(entity);
    }

    @Test
    public void testDeleteAll() {
        service.deleteAllTasks();

        verify(repository, times(1)).deleteAll();
    }

    @Test
    public void testDeleteById() {
        String id = UUID.randomUUID().toString();
        service.deleteTask(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteByUrl() {
        service.deleteTaskByBaseUrl(url);

        verify(repository, times(1)).deleteAllByBaseUrl(url);
    }

    @Test
    public void testDeleteByUrlContains() {
        service.deleteTaskByBaseUrlContains(url);

        verify(repository, times(1)).deleteAllByBaseUrlContains(url);
    }
}
