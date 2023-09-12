package com.example.dirtestservice.controller;

import com.example.dirtestservice.dto.TaskDto;
import com.example.dirtestservice.entity.TaskEntity;
import com.example.dirtestservice.repository.TaskRepository;
import com.example.dirtestservice.service.TaskService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class TaskControllerTest {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("postgres")
            .withUsername("root")
            .withPassword("toor");

    private final String url = "http://localhost:8000/";

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        taskRepository.deleteAll();
    }

    @Test
    public void testGetAllTasks() {
        createTask();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/tasks")
                .then()
                .statusCode(200)
                .body(".", hasSize(1));
    }

    @Test
    public void testGetTaskByTaskId() {
        TaskEntity entity = createTask();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/tasks/" + entity.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(entity.getId()))
                .body("name", equalTo(entity.getName()))
                .body("baseUrl", equalTo(entity.getBaseUrl()));
    }

    @Test
    public void testGetTaskByTaskIdEntityNotFound() {
        createTask();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/tasks/123")
                .then()
                .statusCode(404)
                .body("message", equalTo("Task with id=123 wasn't found"))
                .body("date", is(notNullValue()));
    }

    @Test
    public void testGetTasksByBaseUrl() {
        createTask();

        given()
                .contentType(ContentType.JSON)
                .param("url", url)
                .when()
                .get("/tasks/url")
                .then()
                .statusCode(200)
                .body(".", hasSize(1));
    }

    @Test
    public void testGetTasksByBaseUrlEntityNotFound() {
        createTask();

        given()
                .contentType(ContentType.JSON)
                .param("url", "aaa")
                .when()
                .get("/tasks/url")
                .then()
                .statusCode(404)
                .body("message", equalTo("Tasks by url=aaa weren't found"))
                .body("date", is(notNullValue()));
    }

    @Test
    public void testGetTasksByBaseUrlContains() {
        createTask();

        given()
                .contentType(ContentType.JSON)
                .param("url", url)
                .when()
                .get("/tasks/url/contains")
                .then()
                .statusCode(200)
                .body(".", hasSize(1));
    }

    @Test
    public void testGetTasksByBaseUrlContainsEntityNotFound() {
        createTask();

        given()
                .contentType(ContentType.JSON)
                .param("url", "aaa")
                .when()
                .get("/tasks/url")
                .then()
                .statusCode(404)
                .body("message", equalTo("Tasks by url=aaa weren't found"))
                .body("date", is(notNullValue()));
    }

    @Test
    public void testCreateTask() {
        TaskDto dto = createDto();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/tasks")
                .then()
                .statusCode(202)
                .body("id", is(notNullValue()))
                .body("name", startsWith("task-"))
                .body("baseUrl", equalTo(dto.getBaseUrl()));
    }

    @Test
    public void testCreateTaskFilledId() {
        TaskDto dto = createDto();
        dto.setId(UUID.randomUUID().toString());

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/tasks")
                .then()
                .statusCode(400)
                .body("[0].message", equalTo("Task id shouldn't be populated as it will be provided automatically"))
                .body("[0].date", is(notNullValue()));
    }

    @Test
    public void testCreateTaskFilledName() {
        TaskDto dto = createDto();
        dto.setName("Name");

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/tasks")
                .then()
                .statusCode(400)
                .body("[0].message", equalTo("Task name shouldn't be populated as it will be provided automatically"))
                .body("[0].date", is(notNullValue()));
    }

    @Test
    public void testCreateTaskMalformedBaseUrl() {
        TaskDto dto = createDto();
        dto.setBaseUrl("aaa");

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/tasks")
                .then()
                .statusCode(400)
                .body("[0].message", equalTo("Malformed URL provided as base URL"))
                .body("[0].date", is(notNullValue()));
    }

    @Test
    public void testUpdateTask() {
        TaskEntity entity = createTask();
        TaskDto dto = createDto();

        dto.setBaseUrl(dto.getBaseUrl() + "/1");

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .put("/tasks/" + entity.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(entity.getId()))
                .body("name", equalTo(entity.getName()))
                .body("baseUrl", equalTo(dto.getBaseUrl()));
    }

    @Test
    public void testUpdateTaskFilledId() {
        TaskEntity entity = createTask();

        TaskDto dto = createDto();
        dto.setId(UUID.randomUUID().toString());

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .put("/tasks/" + entity.getId())
                .then()
                .statusCode(400)
                .body("[0].message", equalTo("Task id shouldn't be populated as it will be provided automatically"))
                .body("[0].date", is(notNullValue()));
    }

    @Test
    public void testUpdateTaskFilledName() {
        TaskEntity entity = createTask();

        TaskDto dto = createDto();
        dto.setName("Name");

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .put("/tasks/" + entity.getId())
                .then()
                .statusCode(400)
                .body("[0].message", equalTo("Task name shouldn't be populated as it will be provided automatically"))
                .body("[0].date", is(notNullValue()));
    }

    @Test
    public void testUpdateTaskMalformedBaseUrl() {
        TaskEntity entity = createTask();

        TaskDto dto = createDto();
        dto.setBaseUrl("aaa");

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .put("/tasks/" + entity.getId())
                .then()
                .statusCode(400)
                .body("[0].message", equalTo("Malformed URL provided as base URL"))
                .body("[0].date", is(notNullValue()));
    }

    @Test
    public void testUpdateTaskEntityNotFound() {
        TaskDto dto = createDto();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .put("/tasks/123")
                .then()
                .statusCode(404)
                .body("message", equalTo("Task with id=123 wasn't found"))
                .body("date", is(notNullValue()));
    }

    private TaskEntity createTask() {
        TaskDto dto = new TaskDto();
        dto.setBaseUrl(url);

        return taskService.createTask(dto);
    }

    private TaskDto createDto() {
        TaskDto dto = new TaskDto();
        dto.setBaseUrl(url);
        return dto;
    }
}
