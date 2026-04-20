using EcommerceDddDemo.Shared.Tracing;

namespace EcommerceDddDemo.Modules.Inventory.Domain.ValueObjects;

public sealed class Sku
{
    public string Value { get; }

    private Sku(string value)
    {
        Value = value;
    }

    public static async Task<Sku> CreateAsync(string value)
    {
        await TraceHelper.LogAsync($"OrderApplicationService -> {nameof(Sku)}", $"CreateAsync('{value}')");
        if (string.IsNullOrWhiteSpace(value))
            throw new ArgumentException("Sku is required.");

        return new Sku(value.Trim().ToUpperInvariant());
    }
}
