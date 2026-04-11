using System.Collections.Concurrent;
using Rtgs.ReturnManagement.Api.Domain.Model;
using Rtgs.ReturnManagement.Api.Domain.Ports;

namespace Rtgs.ReturnManagement.Api.Infrastructure.Persistence;

public sealed class InMemoryRtgsReturnRepository : IRtgsReturnRepository
{
    private readonly ConcurrentDictionary<Guid, RtgsReturn> _store = new();

    public RtgsReturn Save(RtgsReturn rtgsReturn)
    {
        _store[rtgsReturn.Id] = rtgsReturn;
        return rtgsReturn;
    }

    public RtgsReturn? GetById(Guid id) => _store.TryGetValue(id, out var rtgsReturn) ? rtgsReturn : null;
}
