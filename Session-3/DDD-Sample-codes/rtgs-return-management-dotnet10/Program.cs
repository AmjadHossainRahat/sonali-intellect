using Rtgs.ReturnManagement.Api.Application.Services;
using Rtgs.ReturnManagement.Api.Domain.Ports;
using Rtgs.ReturnManagement.Api.Infrastructure.Payload;
using Rtgs.ReturnManagement.Api.Infrastructure.Persistence;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddSingleton<IRtgsReturnRepository, InMemoryRtgsReturnRepository>();
builder.Services.AddSingleton<IReturnPayloadGenerator, SimpleReturnPayloadGenerator>();
builder.Services.AddScoped<RtgsReturnApplicationService>();
var app = builder.Build();
app.UseSwagger();
app.UseSwaggerUI();
app.MapControllers();
app.Run();
