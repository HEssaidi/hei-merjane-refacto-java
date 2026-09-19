## Refactoring

### Project Structure

The project was reorganized following a lightweight **Hexagonal Architecture**:

```text
adapter/
  in/web/              → REST controllers    ---> I did remove this package due to ambiguity error of integration tests with the `controllers/MyController` class, though I did rename MyController to OrderController
  out/persistence/     → Database adapters
  out/notification/    → Notification adapter

application/
  port/in/              → Use cases
  port/out/             → Output ports
  service/              → Application/business logic

domain/                 → Domain entities
```

### Main Principles

* Separation of responsibilities (Controller / Application / Infrastructure)
* Dependency inversion through ports
* Constructor injection
* Improved testability and readability
* Business logic moved out of the controller

### Behavior & Regression

The main goal was to **refactor without changing the existing behavior**.

Existing business cases were covered by tests before and during the refactoring to ensure that no regression was introduced.

> **Note:** I noticed some differences between the current implementation and the *cahier des charges*, I could be wrong though. Since the instructions explicitly asked to **refactor**, I intentionally did not modify the existing business behavior. Changing those rules would be a functional change rather than a refactoring.

> **Note:** Classes marked with `// WARN: Should not be changed during the exercise` were not modified.

### Testing & simple TDD

Added unit tests to **lock the existing behavior before refactoring**.
Used the tests as a safety net to ensure no regression was introduced.
Refactored incrementally while keeping the tests green.
Added/updated tests according to the new separation of responsibilities.
Running the tests


### Running the tests

From the `api` subdirectory:

* Unit tests: `mvnw test`
* Integration tests: `mvnw integration-test`
* All tests: `mvnw verify`
