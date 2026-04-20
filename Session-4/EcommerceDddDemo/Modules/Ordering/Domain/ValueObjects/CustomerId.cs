using EcommerceDddDemo.Shared.Tracing;

namespace EcommerceDddDemo.Modules.Ordering.Domain.ValueObjects;

public sealed class CustomerId
{
    public string Value { get; }

    private CustomerId(string value)
    {
        Value = value;
    }

    public static async Task<CustomerId> CreateAsync(string value)
    {
        await TraceHelper.LogAsync($"OrderApplicationService -> {nameof(CustomerId)}", $"CreateAsync('{value}')");
        if (string.IsNullOrWhiteSpace(value))
            throw new ArgumentException("CustomerId is required.");

        return new CustomerId(value.Trim());
    }
}
