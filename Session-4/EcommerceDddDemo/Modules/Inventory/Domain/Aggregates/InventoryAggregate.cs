using EcommerceDddDemo.Modules.Inventory.Domain.Entities;
using EcommerceDddDemo.Modules.Inventory.Domain.ValueObjects;
using EcommerceDddDemo.Modules.Ordering.Domain.ValueObjects;
using EcommerceDddDemo.Shared.Tracing;

namespace EcommerceDddDemo.Modules.Inventory.Domain.Aggregates;

public sealed class InventoryAggregate
{
    private readonly List<StockReservation> _reservations = new();

    public Sku Sku { get; private set; } = default!;
    public int AvailableQuantity { get; private set; }

    public IReadOnlyCollection<StockReservation> Reservations => _reservations.AsReadOnly();

    private InventoryAggregate()
    {
    }

    public static async Task<InventoryAggregate> CreateAsync(Sku sku, int availableQuantity)
    {
        await TraceHelper.LogAsync(nameof(InventoryAggregate), "CreateAsync()");
        if (availableQuantity < 0)
            throw new ArgumentOutOfRangeException(nameof(availableQuantity));

        return new InventoryAggregate
        {
            Sku = sku,
            AvailableQuantity = availableQuantity
        };
    }

    public async Task ReserveAsync(OrderId orderId, Sku sku, Quantity quantity)
    {
        await TraceHelper.LogAsync(nameof(InventoryAggregate), "ReserveAsync() started");

        if (!string.Equals(Sku.Value, sku.Value, StringComparison.OrdinalIgnoreCase))
            throw new InvalidOperationException("SKU mismatch.");

        if (AvailableQuantity < quantity.Value)
            throw new InvalidOperationException("Not enough stock.");

        StockReservation reservation = await StockReservation.CreateAsync(orderId, sku, quantity);
        _reservations.Add(reservation);
        AvailableQuantity -= quantity.Value;

        await TraceHelper.LogAsync(nameof(InventoryAggregate), "ReserveAsync() finished");
    }
}
