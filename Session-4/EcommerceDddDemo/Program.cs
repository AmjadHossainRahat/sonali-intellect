using EcommerceDddDemo.Modules.Ordering.Application.Models;
using EcommerceDddDemo.Modules.Ordering.Application.UseCases;
using EcommerceDddDemo.Modules.Ordering.Infrastructure.Repositories;
using EcommerceDddDemo.Modules.Inventory.Infrastructure.Repositories;
using EcommerceDddDemo.Modules.Ordering.Application.Services;

Console.WriteLine("====================================================");
Console.WriteLine("DDD Demo - Place Order Flow");
Console.WriteLine("Program.cs -> starting application");
Console.WriteLine("====================================================");
await Task.Delay(1000);

var orderRepository = new InMemoryOrderRepository();
var inventoryRepository = new InMemoryInventoryRepository();
var orderApplicationService = new OrderApplicationService(orderRepository, inventoryRepository);

var useCase = new PlaceOrderUseCase(orderApplicationService);

var request = new PlaceOrderRequest(
    CustomerId: "CUST-1001",
    Sku: "SKU-RED-TSHIRT",
    Quantity: 2,
    UnitPrice: 499.00m);

var result = await useCase.ExecuteAsync(request);

Console.WriteLine("====================================================");
Console.WriteLine("Program.cs -> flow finished");
Console.WriteLine($"Order Id     : {result.OrderId}");
Console.WriteLine($"Order Status : {result.OrderStatus}");
Console.WriteLine($"Reserved SKU : {result.Sku}");
Console.WriteLine($"Quantity     : {result.Quantity}");
Console.WriteLine("====================================================");
