using EcommerceDddDemo.Modules.Inventory.Domain.ValueObjects;
using EcommerceDddDemo.Modules.Ordering.Domain.Entities;
using EcommerceDddDemo.Modules.Ordering.Domain.ValueObjects;
using EcommerceDddDemo.Shared.Tracing;

namespace EcommerceDddDemo.Modules.Ordering.Domain.Aggregates;

public sealed class OrderAggregate
{
    private readonly List<OrderLine> _lines = new();

    public OrderId Id { get; private set; } = default!;
    public CustomerId CustomerId { get; private set; } = default!;
    public OrderStatus Status { get; private set; } = default!;
    public IReadOnlyCollection<OrderLine> OrderLines => _lines.AsReadOnly();

    private OrderAggregate()
    {
    }

    public static async Task<OrderAggregate> CreateDraftAsync(CustomerId customerId)
    {
        await TraceHelper.LogAsync(nameof(OrderAggregate), "CreateDraftAsync()");
        return new OrderAggregate
        {
            Id = await OrderId.NewAsync(),
            CustomerId = customerId,
            Status = await OrderStatus.DraftAsync()
        };
    }

    public async Task PlaceAsync(Sku sku, Quantity quantity, Money unitPrice)
    {
        await TraceHelper.LogAsync(nameof(OrderAggregate), "PlaceAsync() started");

        if (_lines.Count > 0)
            throw new InvalidOperationException("Order already has lines for this demo scenario.");

        OrderLine line = await OrderLine.CreateAsync(sku, quantity, unitPrice);
        _lines.Add(line);

        Status = await OrderStatus.PlacedAsync();

        await TraceHelper.LogAsync(nameof(OrderAggregate), "PlaceAsync() finished");
    }
}
