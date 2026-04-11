namespace Rtgs.ReturnManagement.Api.Application.Dtos;

public sealed record CreateReturnRequest(string OriginalTransactionReference, string ReasonCode, string ReasonDescription);
