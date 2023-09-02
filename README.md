# Dir Test Service

Automation service for testing directories of Web Applications.

## Prerequisites
1. Java 17+
2. PostgreSQL 15
3. Maven 3.8.1+

## Migration
1. Update conf/migration.conf file, add username and password for root user
2. Run ```mvn clean flyway:migrate -Dflyway.configFiles=migration.conf``` on running PostgreSQL DB with existing public schema

## REST APIs
Service expose REST APIs to manipulate configuration and runtime of the task and obtain tasks results

### Tasks API
```GET /tasks/``` provide all existing tasks in the system  
```GET /task/{id}``` provide task by id  
```POST /tasks``` body ```{"baseUrl": "valid URL example"}``` create task for particular base URL  
```PUT /tasks/{id}``` body ```{"baseUrl": "valid URL example"}``` update existing task  
```DELETE /tasks/{id}``` delete particular task with all related task results  
```POST /tasks/start/{id}``` starts execution of particular task

### Task Results API
```GET /taskresults``` provide all task results in the system  
```GET /taskresults/task/{id}``` provide all task results for a particular task  
```GET /taskresults/{id}``` provide particular task result  
```DELETE /taskresults/task/{id}``` delete all task results for a particular task  
```DELETE /taskresults``` delete all task results in the system  