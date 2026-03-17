# Job Portal Backend

This is the backend service for the Job Portal web application built using Java and Spring Boot.  
It provides REST APIs for managing jobs, job applications, and employer dashboard functionality.

## Features

- Create job postings
- View all available jobs
- Apply for jobs
- View job applicants
- Employer dashboard
- REST API integration with Angular frontend

## Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- REST APIs
- MySQL
- Maven

## Frontend Repository

The Angular frontend communicates with this backend through REST APIs.


## API Endpoints (Examples)

GET /jobs  
GET /jobs/{id}  
POST /jobs  
POST /applications  
GET /applications/{jobId}

## Installation


Navigate to the project folder:

cd job-portal-backend

Run the application:

mvn spring-boot:run

Server will start at:

http://localhost:8080

## Project Structure

src/main/java
controller
service
repository
model

## Author

Mohd Rashid
