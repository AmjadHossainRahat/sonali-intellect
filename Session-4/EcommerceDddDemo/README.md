# Ecommerce DDD Demo

This is a teaching/demo .NET Console app that shows a visible `Place Order` flow with:
- Use Case
- Application Service
- Repository interfaces
- In-memory repository implementations
- Aggregate
- Entity
- Value Object
- 3-seconds delays for tracing

## Expected flow
Program.cs
-> PlaceOrderUseCase
-> OrderApplicationService
-> ValueObjects
-> InMemoryInventoryRepository
-> InventoryAggregate
-> OrderAggregate
-> OrderLine
-> StockReservation
-> Repositories Save
-> Program.cs end
