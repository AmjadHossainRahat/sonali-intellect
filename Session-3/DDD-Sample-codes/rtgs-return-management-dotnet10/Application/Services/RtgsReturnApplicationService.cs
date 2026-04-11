using Rtgs.ReturnManagement.Api.Application.Dtos;
using Rtgs.ReturnManagement.Api.Domain.Model;
using Rtgs.ReturnManagement.Api.Domain.Ports;

namespace Rtgs.ReturnManagement.Api.Application.Services;

public sealed class RtgsReturnApplicationService
{
    private readonly IRtgsReturnRepository _repository;
    private readonly IReturnPayloadGenerator _payloadGenerator;

    public RtgsReturnApplicationService(IRtgsReturnRepository repository, IReturnPayloadGenerator payloadGenerator)
    {
        _repository = repository;
        _payloadGenerator = payloadGenerator;
    }

    public RtgsReturnResponse Create(CreateReturnRequest request)
    {
        var rtgsReturn = new RtgsReturn(
            new TransactionReference(request.OriginalTransactionReference),
            new ReturnReason(request.ReasonCode, request.ReasonDescription),
            DateTime.UtcNow);

        _repository.Save(rtgsReturn);
        return RtgsReturnResponse.From(rtgsReturn);
    }

    public RtgsReturnResponse GeneratePayload(Guid id)
    {
        var rtgsReturn = GetAggregate(id);
        var payload = _payloadGenerator.Generate(rtgsReturn);
        rtgsReturn.GeneratePayload(payload, DateTime.UtcNow);
        _repository.Save(rtgsReturn);
        return RtgsReturnResponse.From(rtgsReturn);
    }

    public RtgsReturnResponse MarkSent(Guid id)
    {
        var rtgsReturn = GetAggregate(id);
        rtgsReturn.MarkAsSent(DateTime.UtcNow);
        _repository.Save(rtgsReturn);
        return RtgsReturnResponse.From(rtgsReturn);
    }

    public RtgsReturnResponse Acknowledge(Guid id)
    {
        var rtgsReturn = GetAggregate(id);
        rtgsReturn.Acknowledge(DateTime.UtcNow);
        _repository.Save(rtgsReturn);
        return RtgsReturnResponse.From(rtgsReturn);
    }

    public RtgsReturnResponse Get(Guid id) => RtgsReturnResponse.From(GetAggregate(id));

    private RtgsReturn GetAggregate(Guid id) => _repository.GetById(id) ?? throw new ArgumentException($"RTGS Return not found: {id}");
}
