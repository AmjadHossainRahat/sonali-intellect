using EcommerceDddDemo.Modules.Ordering.Domain.ValueObjects;
using EcommerceDddDemo.Shared.Tracing;

namespace EcommerceDddDemo.Modules.Inventory.Domain.ValueObjects;

public sealed class Quantity
{
    public int Value { get; }

    private Quantity(int value)
    {
        Value = value;
    }

    public static async Task<Quantity> CreateAsync(int value)
    {
        await TraceHelper.LogAsync($"OrderApplicationService -> {nameof(Quantity)}", $"CreateAsync({value})");
        if (value <= 0)
            throw new ArgumentOutOfRangeException(nameof(value), "Quantity must be greater than zero.");

        return new Quantity(value);
    }
}
