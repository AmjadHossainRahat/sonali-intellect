# RTGS Return Management Service – Project Structure

This document describes the recommended project structure following **DDD Lite + Clean Architecture**.

## Why DDD `Lite`? Why not DDD?
Because it avoids

* Event Sourcing
* Full CQRS split
* SAGA orchestration
* Complex Domain Events framework(event buses + reply logic)


---

## 1. High Level Structure

```
RTGS.ReturnManagement
 ├── src
 │   ├── RTGS.ReturnManagement.Domain
 │   ├── RTGS.ReturnManagement.Application
 │   ├── RTGS.ReturnManagement.Infrastructure
 │   └── RTGS.ReturnManagement.Api
 └── tests
     ├── RTGS.ReturnManagement.Domain.Tests
     ├── RTGS.ReturnManagement.Application.Tests
```

---

## 2. Domain Layer

**Purpose:** Core business logic and rules.

```
Domain
 ├── Aggregates
 │   └── RtgsReturn.cs
 ├── ValueObjects
 │   ├── ReturnReason.cs
 │   └── TransactionReference.cs
 ├── Enums
 │   └── ReturnStatus.cs
 ├── Events
 │   ├── ReturnCreated.cs
 │   ├── ReturnPayloadGenerated.cs
 │   ├── ReturnSent.cs
 │   └── ReturnAcknowledged.cs
 └── Services
     └── IReturnPayloadGenerator.cs
```

Rules:
- No dependency on frameworks (EF, Spring, ASP.NET)
- Contains only business logic

---

## 3. Application Layer

**Purpose:** Use-case orchestration.

```
Application
 ├── UseCases
 │   ├── CreateReturn
 │   ├── GenerateReturnPayload
 │   ├── MarkReturnAsSent
 │   └── AcknowledgeReturn
 └── Ports
     └── IRtgsReturnRepository.cs
```

Rules:
- Calls Domain
- Defines interfaces (ports)
- No infrastructure logic

---

## 4. Infrastructure Layer

**Purpose:** External concerns (DB, messaging, XML generation).

```
Infrastructure
 ├── Persistence
 │   └── RtgsReturnRepository.cs
 ├── PayloadGeneration
 │   └── XmlReturnPayloadGenerator.cs
 ├── Messaging
 │   └── EventPublisher.cs
 └── Configuration
```

Rules:
- Implements interfaces from Application
- Can use frameworks (EF Core, Spring Data, etc.)

---

## 5. API Layer

**Purpose:** Entry point (HTTP / REST).

```
Api
 ├── Controllers
 │   └── ReturnController.cs
 ├── DTOs
 └── Program.cs / Startup.cs
```

Rules:
- No business logic
- Only calls Application layer

---

## 6. Dependency Flow

```
API -> Application -> Domain
             ↓
       Infrastructure
```

Important:
- Dependencies only go inward

---

## 7. Key Design Principles

- Rich Domain Model
- Value Objects for validation
- Use Cases for orchestration
- Repository abstraction
- Clear separation of concerns

---

## 8. What to Avoid

- Anemic domain model
- Business logic in controllers
- Direct DB access from Application
- Mixing infrastructure in Domain

---

## 9. Recommended Steps

1. Domain first
2. Application use cases
3. Infrastructure
4. API
5. Tests

---

This structure is simple, scalable, and production-ready.
