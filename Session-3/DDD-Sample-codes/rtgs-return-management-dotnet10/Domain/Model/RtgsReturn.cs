namespace Rtgs.ReturnManagement.Api.Domain.Model;

public sealed class RtgsReturn
{
    public Guid Id { get; }
    public TransactionReference OriginalTransactionReference { get; }
    public ReturnReason Reason { get; }
    public ReturnStatus Status { get; private set; }
    public string? Payload { get; private set; }
    public DateTime CreatedAtUtc { get; }
    public DateTime? GeneratedAtUtc { get; private set; }
    public DateTime? SentAtUtc { get; private set; }
    public DateTime? AcknowledgedAtUtc { get; private set; }

    public RtgsReturn(TransactionReference originalTransactionReference, ReturnReason reason, DateTime createdAtUtc)
    {
        Id = Guid.NewGuid();
        OriginalTransactionReference = originalTransactionReference;
        Reason = reason;
        CreatedAtUtc = createdAtUtc;
        Status = ReturnStatus.Draft;
    }

    public void GeneratePayload(string payload, DateTime generatedAtUtc)
    {
        if (Status != ReturnStatus.Draft) throw new InvalidOperationException("Payload can only be generated from Draft status.");
        if (string.IsNullOrWhiteSpace(payload)) throw new ArgumentException("Payload is required.");
        Payload = payload;
        GeneratedAtUtc = generatedAtUtc;
        Status = ReturnStatus.Generated;
    }

    public void MarkAsSent(DateTime sentAtUtc)
    {
        if (Status != ReturnStatus.Generated) throw new InvalidOperationException("Return can only be sent after payload generation.");
        SentAtUtc = sentAtUtc;
        Status = ReturnStatus.Sent;
    }

    public void Acknowledge(DateTime acknowledgedAtUtc)
    {
        if (Status != ReturnStatus.Sent) throw new InvalidOperationException("Only sent returns can be acknowledged.");
        AcknowledgedAtUtc = acknowledgedAtUtc;
        Status = ReturnStatus.Acknowledged;
    }
}
