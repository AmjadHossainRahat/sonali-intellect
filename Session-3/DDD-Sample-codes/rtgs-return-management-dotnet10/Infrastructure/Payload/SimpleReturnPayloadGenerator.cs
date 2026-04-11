using Rtgs.ReturnManagement.Api.Domain.Model;
using Rtgs.ReturnManagement.Api.Domain.Ports;

namespace Rtgs.ReturnManagement.Api.Infrastructure.Payload;

public sealed class SimpleReturnPayloadGenerator : IReturnPayloadGenerator
{
    public string Generate(RtgsReturn rtgsReturn) =>
        "<RtgsReturn>" +
        "<ReturnId>" + rtgsReturn.Id + "</ReturnId>" +
        "<OriginalTransactionReference>" + rtgsReturn.OriginalTransactionReference.Value + "</OriginalTransactionReference>" +
        "<ReasonCode>" + rtgsReturn.Reason.Code + "</ReasonCode>" +
        "<ReasonDescription>" + rtgsReturn.Reason.Description + "</ReasonDescription>" +
        "</RtgsReturn>";
}
