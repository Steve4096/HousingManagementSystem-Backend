# 🏠 Housing Management System

## 📌 Overview
The Housing Management System is a modular backend application designed to simulate real-world housing operations such as occupancy management, user handling, and payment processing.

The system is currently ~80% feature complete and actively evolving with improved testing coverage and DevOps integration.

The goal of this project is to demonstrate backend engineering principles, software quality practices, and CI/CD adoption.

---

## 🧩 System Architecture

The system is designed using a modular approach:

### 🏠 Occupancy Module
- Manages tenant occupancy records
- Validates occupancy availability
- Ensures business rule consistency

### 💳 Payment Module
- Handles payment processing logic
- Validates payment requests
- Manages success and failure scenarios
- Linked to occupancy validation rules

### 👤 User Module
- Manages system users
- Handles user-related operations

---

## 🧪 Testing Strategy

This project follows a structured testing approach:

### ✔ Unit Testing
- Implemented using JUnit and Mockito
- Focus on service-layer logic validation

### ✔ Coverage Focus
- Core business logic tested
- Payment and occupancy flows partially covered
- Additional edge cases continuously being added

### ✔ Testing Philosophy
- Isolation of services using mocking
- Validation of both success and failure scenarios
- Focus on correctness of business rules

---

## 💳 Payment Module (Key Highlight)

The payment module is one of the most critical components in this system:

- Payment processing workflow
- Occupancy-linked validation
- Error handling for invalid scenarios
- Unit-tested service logic

This module is actively being expanded with additional test coverage and edge case handling.

---

## ⚙️ DevOps & CI/CD (In Progress)

The project is being extended to include DevOps practices:

- 🐳 Docker containerization (planned / in progress)
- 🔁 CI/CD pipeline integration using GitHub Actions (in progress)
- Automated build and test execution
- Future deployment pipeline simulation

---

## 🧠 Engineering Principles Applied

- Modular system design
- Separation of concerns
- Test-driven development mindset
- Clean code practices
- Incremental feature development

---

## 🚀 Project Status

- 🟡 80% feature complete
- 🧪 Unit testing in progress (expanding coverage)
- ⚙️ CI/CD integration in progress
- 🐳 Dockerization planned

---

## 📌 Tech Stack

- Java
- Spring Boot
- JUnit
- Mockito
- REST APIs
- Git / GitHub

---

## 🎯 Learning Outcomes

This project demonstrates:

- Backend system design
- Service-layer testing
- Real-world business logic modeling
- Early-stage DevOps integration
- Continuous software improvement mindset
