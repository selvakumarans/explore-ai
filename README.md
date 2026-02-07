# Explore AI - GraphQL API

A Spring Boot GraphQL API for searching and managing people data from MongoDB with pagination support.

## Features

- **GraphQL Endpoint** for searching people with filters
- **MongoDB Integration** for data persistence
- **Pagination Support** for large datasets
- **GraphiQL UI** for testing queries
- **Spring Data MongoDB** for easy data access

## Prerequisites

- Java 17+
- Maven 3.6+
- MongoDB 4.0+
- Spring Boot 3.2.0+

## Project Structure

```
src/
├── main/
│   ├── java/com/example/ai/
│   │   ├── ExploreAiApplication.java
│   │   ├── model/
│   │   │   └── People.java
│   │   ├── repository/
│   │   │   └── PeopleRepository.java
│   │   ├── service/
│   │   │   └── PeopleService.java
│   │   └── graphql/
│   │       ├── controller/
│   │       │   └── PeopleGraphQLController.java
│   │       ├── input/
│   │       │   └── PeopleSearch.java
│   │       └── dto/
│   │           └── PeoplePage.java
│   └── resources/
│       ├── graphql/
│       │   └── schema.graphqls
│       └── application.yml
```

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/selvakumarans/explore-ai.git
cd explore-ai
```

### 2. Configure MongoDB
Update `src/main/resources/application.yml` with your MongoDB connection details:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/explore-ai
```

### 3. Build the Project
```bash
mvn clean install
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

## GraphQL Queries

### Access GraphiQL UI
Navigate to: `http://localhost:8080/api/graphiql`

### Search People Query
```graphql
query {
  searchPeople(
    searchParam: {
      peopleName: "John"
      project: "AI"
    }
    page: 0
    size: 10
  ) {
    content {
      id
      name
      project
      email
      department
    }
    totalElements
    totalPages
    currentPage
    pageSize
  }
}
```

### Get People by ID
```graphql
query {
  getPeopleById(id: "507f1f77bcf86cd799439011") {
    id
    name
    project
    email
    department
  }
}
```

### Create People Mutation
```graphql
mutation {
  createPeople(people: {
    name: "John Doe"
    project: "AI-Project"
    email: "john@example.com"
    department: "Engineering"
  }) {
    id
    name
    project
    email
    department
  }
}
}
```

## API Endpoints

- **GraphQL Query**: `POST /api/graphql`
- **GraphiQL UI**: `GET /api/graphiql`
- **Schema Printer**: `GET /api/graphql/schema`

## Data Model

### People Collection
```json
{
  "_id": "ObjectId",
  "name": "String",
  "project": "String",
  "email": "String",
  "department": "String"
}
```

### PeopleSearch Input
```json
{
  "peopleName": "String (optional)",
  "project": "String (optional)"
}
```

## Dependencies

- **spring-boot-starter-graphql**: GraphQL support
- **spring-boot-starter-data-mongodb**: MongoDB integration
- **spring-boot-starter-web**: Web framework
- **spring-boot-devtools**: Development tools

## Testing

```bash
mvn test
```

## License

MIT License
