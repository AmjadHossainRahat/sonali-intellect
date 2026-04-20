namespace EcommerceDddDemo.Modules.Ordering.Application.Models;

public sealed record PlaceOrderRequest(
    string CustomerId,
    string Sku,
    int Quantity,
    decimal UnitPrice);
