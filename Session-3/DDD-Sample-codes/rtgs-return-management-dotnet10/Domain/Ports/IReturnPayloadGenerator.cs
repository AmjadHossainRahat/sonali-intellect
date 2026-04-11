using Rtgs.ReturnManagement.Api.Domain.Model;

namespace Rtgs.ReturnManagement.Api.Domain.Ports;

public interface IReturnPayloadGenerator
{
    string Generate(RtgsReturn rtgsReturn);
}
