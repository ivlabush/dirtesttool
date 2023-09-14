package com.example.dirtestservice.controller;

import com.example.dirtestservice.dto.TaskDto;
import com.example.dirtestservice.entity.TaskEntity;
import com.example.dirtestservice.repository.TaskRepository;
import com.example.dirtestservice.service.ErrorService;
import com.example.dirtestservice.service.TaskService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ErrorControllerTest {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("postgres")
            .withUsername("root")
            .withPassword("toor");

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ErrorService errorService;

    private final String url = "http://localhost:8000/";

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        taskRepository.deleteAll();
    }

    private final String STACK_TRACE_MESSAGE = "Message1";
    private final int ERROR_STATUS_CODE = 400;
    private final String STATUS_TEXT = "Status Text1";

    @Test
    public void testGetAllErrors() {
        TaskEntity entity = createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/errors")
                .then()
                .statusCode(200)
                .body(".", hasSize(1))
                .body("[0].id", is(notNullValue()))
                .body("[0].url", equalTo(url + "1"))
                .body("[0].taskId", equalTo(entity.getId()))
                .body("[0].statusCode", equalTo(ERROR_STATUS_CODE))
                .body("[0].message", equalTo(STACK_TRACE_MESSAGE));
    }

    @Test
    public void testGetErrorsByTaskId() {
        TaskEntity entity = createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/errors/task/" + entity.getId())
                .then()
                .statusCode(200)
                .body(".", hasSize(1))
                .body("[0].id", is(notNullValue()))
                .body("[0].url", equalTo(url + "1"))
                .body("[0].taskId", equalTo(entity.getId()))
                .body("[0].statusCode", equalTo(ERROR_STATUS_CODE))
                .body("[0].message", equalTo(STACK_TRACE_MESSAGE));
    }

    @Test
    public void testGetErrorsByTaskIdEntityNotFound() {
        createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/errors/task/123")
                .then()
                .statusCode(404)
                .body("message", equalTo("Errors for task id=123 weren't found"))
                .body("date", is(notNullValue()));
    }

    @Test
    public void testGetErrorsByStatusCode() {
        TaskEntity entity = createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/errors/statuscode/400")
                .then()
                .statusCode(200)
                .body(".", hasSize(1))
                .body("[0].id", is(notNullValue()))
                .body("[0].url", equalTo(url + "1"))
                .body("[0].taskId", equalTo(entity.getId()))
                .body("[0].statusCode", equalTo(ERROR_STATUS_CODE))
                .body("[0].message", equalTo(STACK_TRACE_MESSAGE));
    }

    @Test
    public void testGetErrorsByStatusCodeEntityNotFound() {
        createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/errors/statuscode/100")
                .then()
                .statusCode(404)
                .body("message", equalTo("Errors by status code 100 weren't found"))
                .body("date", is(notNullValue()));
    }

    @Test
    public void testGetErrorsByUrl() {
        TaskEntity entity = createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .param("url", url + "1")
                .when()
                .get("/errors/url")
                .then()
                .statusCode(200)
                .body(".", hasSize(1))
                .body("[0].id", is(notNullValue()))
                .body("[0].url", equalTo(url + "1"))
                .body("[0].taskId", equalTo(entity.getId()))
                .body("[0].statusCode", equalTo(ERROR_STATUS_CODE))
                .body("[0].message", equalTo(STACK_TRACE_MESSAGE));
    }

    @Test
    public void testGetErrorsByUrlEntityNotFound() {
        createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .param("url", "aaa")
                .when()
                .get("/errors/url")
                .then()
                .statusCode(404)
                .body("message", equalTo("Errors by url aaa weren't found"))
                .body("date", is(notNullValue()));
    }

    @Test
    public void testGetErrorsByUrlContains() {
        TaskEntity entity = createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .param("url", url)
                .when()
                .get("/errors/url/contains")
                .then()
                .statusCode(200)
                .body(".", hasSize(1))
                .body("[0].id", is(notNullValue()))
                .body("[0].url", equalTo(url + "1"))
                .body("[0].taskId", equalTo(entity.getId()))
                .body("[0].statusCode", equalTo(ERROR_STATUS_CODE))
                .body("[0].message", equalTo(STACK_TRACE_MESSAGE));
    }

    @Test
    public void testGetErrorsByUrlContainsEntityNotFound() {
        createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .param("url", "aaa")
                .when()
                .get("/errors/url/contains")
                .then()
                .statusCode(404)
                .body("message", equalTo("Errors by url contains aaa weren't found"))
                .body("date", is(notNullValue()));
    }

    @Test
    public void testDeleteAllErrorsByTaskIdTaskNotFound() {
        createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/errors/task/123")
                .then()
                .statusCode(404)
                .body("message", equalTo("Task with id=123 wasn't found"))
                .body("date", is(notNullValue()));
    }

    private TaskEntity createTaskWithError() {
        TaskDto dto = new TaskDto();
        dto.setBaseUrl(url);

        TaskEntity task = taskService.createTask(dto);

        HttpClientErrorException cause = HttpClientErrorException
                .create(STACK_TRACE_MESSAGE, HttpStatusCode.valueOf(ERROR_STATUS_CODE), STATUS_TEXT, new HttpHeaders(),
                        new byte[]{}, null);
        StackTraceElement[] stackTrace = new StackTraceElement[]{new StackTraceElement("Class Loader Name", "Module Name", "1",
                "Declaring Class", "Method Name", "File name", 1)};
        cause.setStackTrace(stackTrace);

        errorService.createError(task, url + "1", new RuntimeException(cause));

        return task;
    }
}
