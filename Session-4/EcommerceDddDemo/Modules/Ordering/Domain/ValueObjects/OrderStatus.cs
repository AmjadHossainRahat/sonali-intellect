using EcommerceDddDemo.Shared.Tracing;

namespace EcommerceDddDemo.Modules.Ordering.Domain.ValueObjects;

public sealed class OrderStatus
{
    public string Value { get; }

    private OrderStatus(string value)
    {
        Value = value;
    }

    public static async Task<OrderStatus> DraftAsync()
    {
        await TraceHelper.LogAsync(nameof(OrderStatus), "DraftAsync()");
        return new OrderStatus("DRAFT");
    }

    public static async Task<OrderStatus> PlacedAsync()
    {
        await TraceHelper.LogAsync(nameof(OrderStatus), "PlacedAsync()");
        return new OrderStatus("PLACED");
    }
}
