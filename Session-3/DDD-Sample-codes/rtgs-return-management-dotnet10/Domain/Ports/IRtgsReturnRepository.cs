using Rtgs.ReturnManagement.Api.Domain.Model;

namespace Rtgs.ReturnManagement.Api.Domain.Ports;

public interface IRtgsReturnRepository
{
    RtgsReturn Save(RtgsReturn rtgsReturn);
    RtgsReturn? GetById(Guid id);
}
