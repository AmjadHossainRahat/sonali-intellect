using EcommerceDddDemo.Shared.Tracing;

namespace EcommerceDddDemo.Modules.Ordering.Domain.ValueObjects;

public sealed class Money
{
    public decimal Amount { get; }

    private Money(decimal amount)
    {
        Amount = amount;
    }

    public static async Task<Money> CreateAsync(decimal amount)
    {
        await TraceHelper.LogAsync($"OrderApplicationService -> {nameof(Money)}", $"CreateAsync({amount})");
        if (amount <= 0)
            throw new ArgumentOutOfRangeException(nameof(amount), "Money must be greater than zero.");

        return new Money(amount);
    }
}
