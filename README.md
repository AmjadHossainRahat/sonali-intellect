# Sonali Intellect Training Materials

This repository contains the core training material prepared for the Software Development Department of Sonali Intellect. It combines presentation slides, practical examples, and small demo applications so participants can connect architecture concepts with real implementation and delivery practices.

The training journey starts from software architecture thinking and gradually moves toward clean code, testing, DDD, security, DevSecOps, containerization, Kubernetes, databases, generative AI, Harbor, Lens, and CI/CD foundations.

## Purpose

The goal of this repository is to document and demonstrate a practical learning path for modern software engineering inside a banking and enterprise software context.

The materials show how to:

- Reason about architecture from requirements, NFRs, and constraints.
- Understand clean code, testing, API validation, and security practices.
- Apply DDD-lite and clean architecture ideas through banking and e-commerce examples.
- Use JMeter to model realistic ATM middleware load.
- Understand containerization and Kubernetes fundamentals.
- Prepare teams for CI/CD, Harbor image publishing, Lens-based Kubernetes observation, and GitOps delivery.
- Discuss database choice, scaling, security, and responsible AI usage for software teams.

## Related Demo Repositories

These two companion repositories extend the training into complete CI/CD and GitOps demonstrations.

| Repository | Purpose |
|---|---|
| [si-demo-harbor-image-push](https://github.com/AmjadHossainRahat/si-demo-harbor-image-push) | Minimal Spring Boot demo showing code push, GitHub Actions validation, Docker image build, Harbor image publication, digest resolution, and smoke testing. |
| [sonali-intellect-demo-cicd-gitops](https://github.com/AmjadHossainRahat/sonali-intellect-demo-cicd-gitops) | Complete CI/CD and GitOps lab showing pull request validation, Harbor publishing, digest promotion, Argo CD reconciliation, Kind Kubernetes runtime, and Lens observation. |

The cloud repository for this training material is:

[sonali-intellect](https://github.com/AmjadHossainRahat/sonali-intellect)

## Repository Structure

| Path | What it contains | What was achieved |
|---|---|---|
| `Session-1` | Introductory architecture slides covering microservice adoption, NFRs, constraints, and architecture trade-offs. | Established the training direction: choose architecture based on context, not fashion. |
| `Session-2` | Clean code, testing foundation, and CI/CD automation slides. | Connected developer practices with maintainability, quality gates, and safer release flow. |
| `Session-3` | DDD, consistency patterns, RTGS middleware, containerization fundamentals, and DDD sample code. | Introduced domain modeling and delivery modernization using banking-oriented examples. |
| `Session-4` | Deeper DDD material and a .NET e-commerce DDD demo. | Made DDD concepts visible through a traced place-order flow. |
| `Session-5` | API testing, integration testing, security testing, and DevSecOps material. | Connected secure development practices with automated validation and delivery pipelines. |
| `Session-6` | Containerization and Kubernetes core slides. | Prepared participants to understand container runtime, Kubernetes objects, and deployment models. |
| `Session-7` | Database systems, scaling, security, generative AI, and sample Dockerfiles. | Expanded the training into persistence choices, AI guardrails, and container build practices. |
| `Session-8` | Kubernetes resources, Lens terminology, Harbor foundations, and GitHub Actions image-push preparation. | Prepared the class for the full CI/CD and GitOps demo repository. |
| `JMeter-Setup-For-ATM-Middleware` | Spring MVC ATM middleware simulator, JMeter plan, load-modeling notes, metrics, and screenshots. | Demonstrated realistic TCP load testing for ATM-like middleware traffic. |
| `zk-spring-authz-validation-sample` | Spring Boot, Spring Security, ZK UI, H2 permission table, REST authorization tests, and Selenium UI tests. | Demonstrated fine-grained authorization validation across backend and UI behavior. |

## Demo Applications In This Repository

| Demo | Location | Focus |
|---|---|---|
| ATM middleware load testing demo | `JMeter-Setup-For-ATM-Middleware` | Plain Spring MVC WAR with TCP listeners, ISO-like message handling, JMeter load testing, and Prometheus-style metrics. |
| ZK authorization validation sample | `zk-spring-authz-validation-sample` | Authentication, DB-backed permissions, method security, ZK UI authorization, and automated validation. |
| E-commerce DDD demo | `Session-4/EcommerceDddDemo` | .NET console demo showing use case, application service, aggregates, entities, value objects, and repositories. |
| RTGS return management samples | `Session-3/DDD-Sample-codes` | DDD-lite examples in Spring Boot and .NET for a banking return-management flow. |

## Training Outcome

By completing these sessions and demos, participants should be able to explain not only what modern tools do, but why they are used, where each responsibility belongs, and how code moves from development practices toward reliable software delivery.

The training also included guidance on preparing architectural and technical documentation. A reusable documentation template was shared with the team, and participants applied it to improve the quality, structure, and clarity of their own technical documentation.

The repository is intentionally educational. Some examples are simplified so the main learning points remain visible during a live training session.
