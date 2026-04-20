namespace EcommerceDddDemo.Shared.Tracing;

public static class TraceHelper
{
    private static int serialNumber = 1;
    public static async Task LogAsync(string source, string message)
    {
        Console.WriteLine($"{serialNumber++}. {source} -> {message}{Environment.NewLine}");
        await Task.Delay(3000);
    }
}
