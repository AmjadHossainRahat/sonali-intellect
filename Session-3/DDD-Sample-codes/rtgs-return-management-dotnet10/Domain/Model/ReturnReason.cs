namespace Rtgs.ReturnManagement.Api.Domain.Model;

public sealed record ReturnReason
{
    public string Code { get; }
    public string Description { get; }

    public ReturnReason(string code, string description)
    {
        if (string.IsNullOrWhiteSpace(code))
            throw new ArgumentException("Reason code is required.");
        if (string.IsNullOrWhiteSpace(description))
            throw new ArgumentException("Reason description is required.");
        Code = code;
        Description = description;
    }
}
