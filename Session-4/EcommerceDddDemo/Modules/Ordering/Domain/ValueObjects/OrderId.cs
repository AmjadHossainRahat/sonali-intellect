using EcommerceDddDemo.Shared.Tracing;

namespace EcommerceDddDemo.Modules.Ordering.Domain.ValueObjects;

public sealed class OrderId
{
    public string Value { get; }

    private OrderId(string value)
    {
        Value = value;
    }

    public static async Task<OrderId> NewAsync()
    {
        await TraceHelper.LogAsync(nameof(OrderId), "creating new OrderId");
        return new OrderId($"ORD-{Guid.NewGuid():N}"[..12].ToUpper());
    }
}
