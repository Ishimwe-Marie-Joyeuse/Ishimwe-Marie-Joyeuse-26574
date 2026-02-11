# Assignment 2: Spring Boot REST APIs

This repository contains the implementation of Assignment 2, consisting of five Spring Boot REST API projects and a bonus project.

## Projects Overview

### 1. Library Management API (Question 1)
- **Description**: A basic API for managing books in a library.
- **Port**: 8080
- **Base URL**: `http://localhost:8080/api/books`

### 2. Student Registration API (Question 2)
- **Description**: An API for student registration and management.
- **Port**: 8081 (Changed to avoid conflict)
- **Base URL**: `http://localhost:8081/api/students`

### 3. Restaurant Menu API (Question 3)
- **Description**: An API for managing a restaurant menu with categories and availability.
- **Port**: 8080
- **Base URL**: `http://localhost:8080/api/menu`

### 4. E-Commerce Product API (Question 4)
- **Description**: An API for an e-commerce platform to manage products, categories, and stock.
- **Port**: 8080
- **Base URL**: `http://localhost:8080/api/products`

### 5. Task Management API (Question 5)
- **Description**: An API for managing personal tasks with completion status and priorities.
- **Port**: 8083 (Changed to avoid conflict)
- **Base URL**: `http://localhost:8083/api/tasks`

### 6. User Profile API (Bonus Question)
- **Description**: A comprehensive user profile management API with custom response handling.
- **Port**: 8084 (Changed to avoid conflict)
- **Base URL**: `http://localhost:8084/api/users`

## How to Run

Each project is a standalone Spring Boot application. To run any of them:
1. Navigate to the project directory.
2. Run `./mvnw spring-boot:run`.

## Postman Collections
Postman collections for each API are included in their respective directories:
- `question1-library-api/library_api.postman_collection.json`
- `question2-student-api/student_api.postman_collection.json`
- `question3-restaurant-api/restaurant_api.postman_collection.json`
- `question4-E-Commerce-api/ecommerce_api.postman_collection.json`
- `question5-task-api/task_api.postman_collection.json`
- `questionBonus-userprofile-api/userprofile_api.postman_collection.json`

## Screenshots
Screenshots of the API tests are located in the `postman screenshoot` folder.
