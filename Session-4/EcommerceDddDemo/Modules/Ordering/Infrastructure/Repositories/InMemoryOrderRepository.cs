using EcommerceDddDemo.Modules.Ordering.Domain.Aggregates;
using EcommerceDddDemo.Modules.Ordering.Domain.Repositories;
using EcommerceDddDemo.Shared.Tracing;

namespace EcommerceDddDemo.Modules.Ordering.Infrastructure.Repositories;

public sealed class InMemoryOrderRepository : IOrderRepository
{
    private readonly List<OrderAggregate> _orders = new();

    public async Task SaveAsync(OrderAggregate order)
    {
        await TraceHelper.LogAsync(nameof(InMemoryOrderRepository), "SaveAsync()");
        _orders.Add(order);
    }
}
