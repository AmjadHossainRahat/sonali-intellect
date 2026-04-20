namespace EcommerceDddDemo.Modules.Ordering.Application.Models;

public sealed record PlaceOrderResult(
    string OrderId,
    string OrderStatus,
    string Sku,
    int Quantity);
