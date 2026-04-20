using EcommerceDddDemo.Modules.Inventory.Domain.Aggregates;
using EcommerceDddDemo.Modules.Inventory.Domain.ValueObjects;

namespace EcommerceDddDemo.Modules.Inventory.Domain.Repositories;

public interface IInventoryRepository
{
    Task<InventoryAggregate> GetBySkuAsync(Sku sku);
    Task SaveAsync(InventoryAggregate inventory);
}
