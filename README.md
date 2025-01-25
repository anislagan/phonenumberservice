# Phone Number Service API

A flexible and scalable Spring Boot application that implements a phone search management system.

## Features

- RESTful API endpoints
- Comprehensive error handling
- Environment-specific configurations
- Paginated response
- Global error handling
- Caching for faster access
- Integration and unit tests with Mockito
- Swagger API documentation

## Unsupported features
- Rate limiting - this is better handled by a more robust server infra e.g. AWS API Gateway
- JWT token - not added to focus on API functionality

## Technologies

- Java 17
- Spring Boot 3.4.1
- Gradle build tools
- JUnit 5 & Mockito for testing
- Testcontainers for integration testing

## Static Data fields
- row id, phone id, customer id, phone number, activation status, creation date, modified date - phone numbers
- customer_id,created_date,modified_date - customers

## Prerequisites
- JDK 17 or later
- Gradle 8.x

## Getting Started

### Clone the repository
```bash
git clone https://github.com/anislagan/phone.git
cd phonenumberservice
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
│   │   │       ├── mapper/
│   │   │       ├── dto/
│   │   │       └── exception/
│   │   └── resources/
│   │       │   └── swagger
│   │       └── application.yml
│   └── test/
│       └── java/
└── build.gradle
```


