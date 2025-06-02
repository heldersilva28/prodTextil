namespace web.Models;
public class DashboardStatistics
{
    public int Total { get; set; }
    public int Pendentes { get; set; }
    public int Concluidas { get; set; }
    public Dictionary<string, int> PorMes { get; set; }
}