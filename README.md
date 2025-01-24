# Phone Number Service API

A flexible and scalable Spring Boot application that implements a phone search management system.

## Features

- RESTful API endpoints
- Caching for faster repeated access
- Comprehensive error handling
- Environment-specific configurations
- Paginated response
- Global error handling
- Integration and unit tests with Mockito
- Swagger API documentation

## Limitation
- There is performance issue on the frequent loading of flat file during phone and customer search. 
However, since no production environment is configured to use flat file as persistence, no extra effort is made to optimise it.
Search performance in production can be vastly improved using production-grade search technologies e.g. database or elasticsearch

## Unsupported features
- Rate limiting - this is better handled by a more robust server infra e.g. AWS API Gateway
- JWT token - not added to focus on API functionality

## Technologies

- Java 17
- Spring Boot 3.4.1
- Gradle/Maven build tools
- JUnit 5 & Mockito for testing
- Testcontainers for integration testing

## Flat File Repo
### Syntax
The file is in comma-separated format for easy migration to DB and other fast search technologies e.g. elasticsearch
- row id, phone id, customer id, phone number, activation status, creation date, modified date - phone numbers
- customer_id,created_date,modified_date - customer table

## Prerequisites
- JDK 17 or later
- Gradle 8.x

## Getting Started

### Clone the repository
```bash
git clone https://github.com/anislagan/phone.git
cd product-service
```

### Build the application
```bash
./gradlew clean build
```

### Run the application
```bash
# Development profile
./gradlew bootRun --args='--spring.profiles.active=dev'

# Production profile
./gradlew bootRun --args='--spring.profiles.active=prod'
```

## API Documentation 
(src/main/resources/swagger/api.yml)

### Endpoints Summary
```bash
GET  /phone-numbers                           - Get all phone numbers
GET  /customers/{customerId}/phone-numbers:   - Get customer phone numbers
POST /phone-numbers/{phoneNumberId}/activate  - Activate phone number
```

## Testing

### Run unit tests
```bash
./gradlew test
```

### Run integration tests
```bash
./gradlew integrationTest
```

### Test coverage report
```bash
./gradlew jacocoTestReport
```

## Project Structure
```bash
product-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/product/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── model/
│   │   │       ├── dto/
│   │   │       └── exception/
│   │   └── resources/
│   │       │   └── swagger
│   │       └── application.yml
│   └── test/
│       └── java/
└── build.gradle
```

## License
This project is licensed under the MIT License
