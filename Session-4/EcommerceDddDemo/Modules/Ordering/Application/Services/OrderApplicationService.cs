using EcommerceDddDemo.Modules.Ordering.Application.Models;
using EcommerceDddDemo.Modules.Ordering.Domain.Aggregates;
using EcommerceDddDemo.Modules.Ordering.Domain.Repositories;
using EcommerceDddDemo.Modules.Ordering.Domain.ValueObjects;
using EcommerceDddDemo.Modules.Inventory.Domain.Repositories;
using EcommerceDddDemo.Modules.Inventory.Domain.Aggregates;
using EcommerceDddDemo.Modules.Inventory.Domain.ValueObjects;
using EcommerceDddDemo.Shared.Tracing;

namespace EcommerceDddDemo.Modules.Ordering.Application.Services;

public sealed class OrderApplicationService
{
    private readonly IOrderRepository _orderRepository;
    private readonly IInventoryRepository _inventoryRepository;

    public OrderApplicationService(
        IOrderRepository orderRepository,
        IInventoryRepository inventoryRepository)
    {
        _orderRepository = orderRepository;
        _inventoryRepository = inventoryRepository;
    }

    public async Task<PlaceOrderResult> PlaceOrderAsync(PlaceOrderRequest request)
    {
        await TraceHelper.LogAsync(nameof(OrderApplicationService), "PlaceOrderAsync started");
        await TraceHelper.LogAsync(nameof(OrderApplicationService), "creating ValueObjects");

        var customerId = await CustomerId.CreateAsync(request.CustomerId);
        var sku = await Sku.CreateAsync(request.Sku);
        var quantity = await Quantity.CreateAsync(request.Quantity);
        var money = await Money.CreateAsync(request.UnitPrice);

        await TraceHelper.LogAsync(nameof(OrderApplicationService), "loading InventoryAggregate from repository");
        InventoryAggregate inventory = await _inventoryRepository.GetBySkuAsync(sku);

        await TraceHelper.LogAsync(nameof(OrderApplicationService), "creating OrderAggregate");
        OrderAggregate order = await OrderAggregate.CreateDraftAsync(customerId);

        await TraceHelper.LogAsync(nameof(OrderApplicationService), "calling OrderAggregate.PlaceAsync");
        await order.PlaceAsync(sku, quantity, money);

        await TraceHelper.LogAsync(nameof(OrderApplicationService), "calling InventoryAggregate.ReserveAsync");
        await inventory.ReserveAsync(order.Id, sku, quantity);

        await TraceHelper.LogAsync(nameof(OrderApplicationService), "saving aggregates through repositories");
        await _orderRepository.SaveAsync(order);
        await _inventoryRepository.SaveAsync(inventory);

        await TraceHelper.LogAsync(nameof(OrderApplicationService), "building result model");
        return new PlaceOrderResult(
            order.Id.Value,
            order.Status.Value,
            sku.Value,
            quantity.Value);
    }
}
