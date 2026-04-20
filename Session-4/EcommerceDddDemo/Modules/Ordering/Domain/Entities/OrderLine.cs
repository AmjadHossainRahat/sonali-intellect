using EcommerceDddDemo.Modules.Inventory.Domain.ValueObjects;
using EcommerceDddDemo.Modules.Ordering.Domain.ValueObjects;
using EcommerceDddDemo.Shared.Tracing;

namespace EcommerceDddDemo.Modules.Ordering.Domain.Entities;

public sealed class OrderLine
{
    public Sku Sku { get; }
    public Quantity Quantity { get; }
    public Money UnitPrice { get; }

    private OrderLine(Sku sku, Quantity quantity, Money unitPrice)
    {
        Sku = sku;
        Quantity = quantity;
        UnitPrice = unitPrice;
    }

    public static async Task<OrderLine> CreateAsync(Sku sku, Quantity quantity, Money unitPrice)
    {
        await TraceHelper.LogAsync(nameof(OrderLine), "CreateAsync()");
        return new OrderLine(sku, quantity, unitPrice);
    }
}
