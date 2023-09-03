# Dir Test Service

Automation service for testing directories of Web Applications  

Key features:
 - persistent run configuration
 - multiple word lists
 - request retries depends on returned response status code
 - persistent results and errors
 - async multithreading execution

## Prerequisites
1. Java 17
2. PostgreSQL 15
3. Maven 3.8.1
4. Docker 24.0.5

## Migration
1. Update conf/migration.conf file, change username and password for root user
2. Run ```mvn clean flyway:migrate -Dflyway.configFiles=migration.conf``` on running PostgreSQL DB with existing public schema

## Configuration
1. Place needed word lists in /src/main/resource folder
2. Update application.yaml and provide needed credentials for DB, desired word lists, success and error status codes,
backoff, retries, and desired number of parallel threads for task execution  
   
## Run Service
Run ```mvn spring-boot:run``` to run the service. Default service port is 8080

## REST APIs
Service expose REST APIs to manipulate configuration and runtime of the task, and obtain tasks results or errors  

### Tasks API
```GET /tasks/``` provide all existing tasks in the system  
```GET /task/{id}``` provide task by id  
```POST /tasks``` body ```{"baseUrl": "valid URL example"}``` create task for particular base URL  
```PUT /tasks/{id}``` body ```{"baseUrl": "valid URL example"}``` update existing task  
```DELETE /tasks``` delete all tasks in the system
```DELETE /tasks/{id}``` delete particular task with all related task results  
```DELETE /task/url/{url}``` delete tasks by URL  
```DELETE /task/url/contains/{url}``` delete tasks by URL containing particular symbols  
```POST /tasks/start/{id}``` starts execution of particular task

### Task Results API
```GET /taskresults``` provide all task results in the system  
```GET /taskresults/task/{id}``` provide all task results for a particular task  
```GET /taskresults/{id}``` provide particular task result  
```DELETE /taskresults``` delete all task results in the system
```DELETE /taskresults/url/{url}``` delete tasks results by URL  
```DELETE /taskresults/url/contains/{url}``` delete tasks results by URL containing particular symbols  
```DELETE /taskresults/task/{id}``` delete all task results for a particular task  
```DELETE /taskresults``` delete all task results in the system  

### Errors API
```GET /errors``` provide all errors in the system  
```GET /errors/task/{id}``` provide all errors by task id  
```GET /errors/statuscode/{code}``` provide all errors by status code. Note that 0 status code means internal system error  
```GET /errors/url/{url}``` provide all errors by URL  
```GET /errors/url/contains/{url}``` provide all errors by URL containing particular symbols  
```DELETE /errors``` delete all errors from the system  
```DELETE /errors/task/{id}``` delete all errors by task id  
```DELETE /errors/url/{url}``` delete all errors by URL  
```DELETE /errors/url/contains/{url}``` delete all errors by URL containing particular symbols  

