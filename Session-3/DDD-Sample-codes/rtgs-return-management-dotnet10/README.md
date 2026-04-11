# RTGS Return Management Service (.NET 10 LTS)

DDD-lite starter for RTGS-Return-Management-Service.

## Stack
- .NET 10 (LTS)
- ASP.NET Core Web API
- In-memory repository for simplicity

## Run
```bash
dotnet restore
dotnet run
```

## API
- `POST /api/rtgs-returns`

Sample request:
```json
{
  "originalTransactionReference": "TXN-10001",
  "reasonCode": "R01",
  "reasonDescription": "Invalid account number"
}
```

This starter keeps the model simple for learning DDD step by step.
