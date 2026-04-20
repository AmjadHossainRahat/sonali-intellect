using EcommerceDddDemo.Modules.Ordering.Domain.Aggregates;

namespace EcommerceDddDemo.Modules.Ordering.Domain.Repositories;

public interface IOrderRepository
{
    Task SaveAsync(OrderAggregate order);
}
