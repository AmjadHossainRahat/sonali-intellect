using EcommerceDddDemo.Modules.Ordering.Application.Models;
using EcommerceDddDemo.Modules.Ordering.Application.Services;
using EcommerceDddDemo.Shared.Tracing;

namespace EcommerceDddDemo.Modules.Ordering.Application.UseCases;

public sealed class PlaceOrderUseCase
{
    private readonly OrderApplicationService _orderApplicationService;

    public PlaceOrderUseCase(OrderApplicationService orderApplicationService)
    {
        _orderApplicationService = orderApplicationService;
    }

    public async Task<PlaceOrderResult> ExecuteAsync(PlaceOrderRequest request)
    {
        await TraceHelper.LogAsync(nameof(PlaceOrderUseCase), "ExecuteAsync started");
        await TraceHelper.LogAsync(nameof(PlaceOrderUseCase), "delegating to OrderApplicationService");

        var result = await _orderApplicationService.PlaceOrderAsync(request);

        await TraceHelper.LogAsync(nameof(PlaceOrderUseCase), "ExecuteAsync finished");
        return result;
    }
}
