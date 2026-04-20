using EcommerceDddDemo.Modules.Inventory.Domain.ValueObjects;
using EcommerceDddDemo.Modules.Ordering.Domain.ValueObjects;
using EcommerceDddDemo.Shared.Tracing;

namespace EcommerceDddDemo.Modules.Inventory.Domain.Entities;

public sealed class StockReservation
{
    public OrderId OrderId { get; }
    public Sku Sku { get; }
    public Quantity Quantity { get; }

    private StockReservation(OrderId orderId, Sku sku, Quantity quantity)
    {
        OrderId = orderId;
        Sku = sku;
        Quantity = quantity;
    }

    public static async Task<StockReservation> CreateAsync(OrderId orderId, Sku sku, Quantity quantity)
    {
        await TraceHelper.LogAsync(nameof(StockReservation), "CreateAsync()");
        return new StockReservation(orderId, sku, quantity);
    }
}
