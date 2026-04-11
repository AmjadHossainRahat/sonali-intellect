using Microsoft.AspNetCore.Mvc;
using Rtgs.ReturnManagement.Api.Api.Requests;
using Rtgs.ReturnManagement.Api.Application.Dtos;
using Rtgs.ReturnManagement.Api.Application.Services;

namespace Rtgs.ReturnManagement.Api.Api.Controllers;

[ApiController]
[Route("api/returns")]
public sealed class RtgsReturnsController : ControllerBase
{
    private readonly RtgsReturnApplicationService _service;

    public RtgsReturnsController(RtgsReturnApplicationService service)
    {
        _service = service;
    }

    [HttpPost]
    public ActionResult<RtgsReturnResponse> Create([FromBody] CreateReturnApiRequest request)
    {
        var response = _service.Create(new CreateReturnRequest(request.OriginalTransactionReference, request.ReasonCode, request.ReasonDescription));
        return CreatedAtAction(nameof(GetById), new { id = response.Id }, response);
    }

    [HttpPost("{id:guid}/generate-payload")]
    public ActionResult<RtgsReturnResponse> GeneratePayload(Guid id) => Ok(_service.GeneratePayload(id));

    [HttpPost("{id:guid}/mark-sent")]
    public ActionResult<RtgsReturnResponse> MarkSent(Guid id) => Ok(_service.MarkSent(id));

    [HttpPost("{id:guid}/acknowledge")]
    public ActionResult<RtgsReturnResponse> Acknowledge(Guid id) => Ok(_service.Acknowledge(id));

    [HttpGet("{id:guid}")]
    public ActionResult<RtgsReturnResponse> GetById(Guid id) => Ok(_service.Get(id));
}
