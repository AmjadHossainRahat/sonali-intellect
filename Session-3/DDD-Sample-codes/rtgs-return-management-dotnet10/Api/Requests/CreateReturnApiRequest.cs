using System.ComponentModel.DataAnnotations;

namespace Rtgs.ReturnManagement.Api.Api.Requests;

public sealed class CreateReturnApiRequest
{
    [Required]
    public string OriginalTransactionReference { get; set; } = string.Empty;

    [Required]
    public string ReasonCode { get; set; } = string.Empty;

    [Required]
    public string ReasonDescription { get; set; } = string.Empty;
}
