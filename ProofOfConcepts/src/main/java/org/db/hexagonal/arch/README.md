# 🚀 Hexagonal Architecture

Hexagonal Architecture (also called **Ports and Adapters**) provides a clear way to separate the **core business logic** from external dependencies.

---

## 🧩 Core Idea

- The **core logic (domain)** communicates only through **interfaces (ports)**.
- The actual implementations of these interfaces are provided by **adapters** (e.g., databases, APIs, UIs, messaging systems).
- This ensures the **domain remains independent** of any specific technology or framework.

---

## ✅ Benefits

- Core logic remains **pure** and free from external concerns.
- External systems (inputs/outputs) can be easily **replaced** without impacting the domain.
- Improves **testability**, as dependencies can be mocked or swapped with ease.
- Encourages a **plug-and-play** architecture using the Adapter pattern.

---

## 🏗️ Structure Overview

- **Domain (Core)** → Entities, business rules, and use cases.
- **Ports (Interfaces)** → Define contracts for external interactions.
- **Adapters (Implementations)** → Plug into ports (e.g., DB adapter, REST API adapter, CLI adapter).

---

## 🔌 POC Example

In the POC:

- The **core service** talks to
    - a **`UserInput`** interface (for inputs)
    - a **`DataOutput`** interface (for persistence).

- These interfaces are implemented by:
    - **`UserController`** (input adapter)
    - **`MySqlJpaRepository`** (output adapter).

This demonstrates how **Hexagonal Architecture** enforces clear separation between core logic and external systems.

---

## ⚠️ Trade-offs

- The approach **increases the number of interfaces** you need to manage.
- Often requires **POJO initialization** boilerplate.
- Fortunately, this can be mitigated by:
    - Modern **web frameworks** (e.g., Spring, Micronaut, NestJS)
    - **IDE autocompletion**
    - **AI-assisted code generation**

---

## 📊 Diagram

```text
         +--------------------+
         |   User Controller  |   <-- Input Adapter
         +--------------------+
                  |
                  v
     +--------------------------+
     |   UserInput (Port)       |
     +--------------------------+
                  |
     +--------------------------+
     |   Core Service (Domain)  |
     +--------------------------+
                  |
     +--------------------------+
     |   DataOutput (Port)      |
     +--------------------------+
                  |
                  v
     +--------------------------+
     | MySqlJpaRepository Impl  |   <-- Output Adapter
     +--------------------------+


## ✅ Best Use Cases


1. **Multiple Input/Output Channels**  
   - Same core logic needs to work with **different adapters**.  
   - Example: REST + GraphQL + CLI + gRPC all using the same service.  

2. **Technology Independence**  
   - You want to **swap databases, frameworks, or APIs** without rewriting core logic.  
   - Example: Move from MySQL to Postgres, or from Kafka to SQS.  

3. **Testability Requirements**  
   - Need to test business logic in isolation without involving DB, APIs, or UI.  
   - Example: unit-testing core services with in-memory mocks.  

4. **Long-Lived Applications**  
   - Systems expected to run for years and survive **tech migrations**.  
   - Example: enterprise applications that outlast UI frameworks or DB engines.  


If you dont think above will be the case 3 tier architecture will be sufficient in most cases :). 