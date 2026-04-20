using EcommerceDddDemo.Modules.Inventory.Domain.Aggregates;
using EcommerceDddDemo.Modules.Inventory.Domain.Repositories;
using EcommerceDddDemo.Modules.Inventory.Domain.ValueObjects;
using EcommerceDddDemo.Shared.Tracing;

namespace EcommerceDddDemo.Modules.Inventory.Infrastructure.Repositories;

public sealed class InMemoryInventoryRepository : IInventoryRepository
{
    public async Task<InventoryAggregate> GetBySkuAsync(Sku sku)
    {
        await TraceHelper.LogAsync(nameof(InMemoryInventoryRepository), "GetBySkuAsync()");
        return await InventoryAggregate.CreateAsync(sku, availableQuantity: 10);
    }

    public async Task SaveAsync(InventoryAggregate inventory)
    {
        await TraceHelper.LogAsync(nameof(InMemoryInventoryRepository), "SaveAsync()");
    }
}
