# Marketplace API

This is a REST API built with **Java 17**, **Spring Boot**, and **PostgreSQL** to simulate a backend for an e-commerce marketplace. The main goal of this project is to practice clean code practices, layered architecture, and proper error handling in a production-like environment.

---

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 4.0.2
- **Database:** PostgreSQL
- **Build Tool:** Maven

---

## Project Structure

The project uses a standard layered architecture to keep the code organized and easy to maintain:

* **Controllers**: Handles HTTP requests and responses.
* **Controllers/Exceptions**: Dedicated package for centralized error records and handling.
* **Services**: Contains the business logic of the application.
* **Repositories**: Interacts with the PostgreSQL database using Spring Data JPA.
* **Entities**: Database table mappings.
* **DTOs (Data Transfer Objects)**: Inputs and outputs tailored using Java Records to ensure immutability.

---

## Key Features Implemented So Far

### 1. Category Management
- Full setup for creating categories.
- Uses `CategoryRequestDTO` to validate inputs (e.g., preventing empty names) and `CategoryResponseDTO` to return clean data to the client.

### 2. Global Error Handling
Instead of letting the application crash or return raw internal stacktraces to the user, a centralized `ResourceExceptionHandler` was implemented, which is responsible for properly handling and returning error messages to the user in case of:
- **Validation Errors (400 Bad Request)**: Catches all invalid fields at once and return them in a clean list.
- **Database Conflicts (409 Conflict)**: Catches database violations, like trying to register a category name that already exists.
- **Generic Errors (500 Internal Server Error)**: A fallback handler to catch unexpected crashes safely.

---

## What's Next? (Roadmap)

- [ ] **Products**: Create the product module and link it to categories using a `@ManyToOne` relationship.
- [ ] **Sellers**: Implement merchant accounts so different sellers can post products.
- [ ] **Orders/Sales**: Create the checkout logic to handle transactions involving buyers, sellers, and products.

---

## How to Run Locally

1. Clone the repository:

```bash
git clone https://github.com/Lucastemo/marketplace-api.git
```

2. Set up your local environment variables or open `src/main/resources/application.properties` and update the database credentials with your local PostgreSQL setup:

```Properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=your_user
spring.datasource.password=your_password
```

3. Run the project using Maven or your favorite IDE.