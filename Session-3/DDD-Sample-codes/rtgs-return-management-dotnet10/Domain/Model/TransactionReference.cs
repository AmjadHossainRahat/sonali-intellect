namespace Rtgs.ReturnManagement.Api.Domain.Model;

public sealed record TransactionReference
{
    public string Value { get; }

    public TransactionReference(string value)
    {
        if (string.IsNullOrWhiteSpace(value))
            throw new ArgumentException("Transaction reference is required.");
        Value = value;
    }
}
