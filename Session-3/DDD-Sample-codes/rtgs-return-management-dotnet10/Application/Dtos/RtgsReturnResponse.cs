using Rtgs.ReturnManagement.Api.Domain.Model;

namespace Rtgs.ReturnManagement.Api.Application.Dtos;

public sealed record RtgsReturnResponse(
    Guid Id,
    string OriginalTransactionReference,
    string ReasonCode,
    string ReasonDescription,
    ReturnStatus Status,
    string? Payload,
    DateTime CreatedAtUtc,
    DateTime? GeneratedAtUtc,
    DateTime? SentAtUtc,
    DateTime? AcknowledgedAtUtc
)
{
    public static RtgsReturnResponse From(RtgsReturn rtgsReturn) => new(
        rtgsReturn.Id,
        rtgsReturn.OriginalTransactionReference.Value,
        rtgsReturn.Reason.Code,
        rtgsReturn.Reason.Description,
        rtgsReturn.Status,
        rtgsReturn.Payload,
        rtgsReturn.CreatedAtUtc,
        rtgsReturn.GeneratedAtUtc,
        rtgsReturn.SentAtUtc,
        rtgsReturn.AcknowledgedAtUtc
    );
}
