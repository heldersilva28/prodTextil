namespace ProdTextil.Models
{
    public class DashboardStatistics
    {
        public int Total { get; set; }
        public int Pendentes { get; set; }
        public int Concluidas { get; set; }
        public MonthlyStats PorMes { get; set; } = new MonthlyStats();
    }

    public class MonthlyStats
    {
        public int Janeiro { get; set; }
        public int Fevereiro { get; set; }
        public int Marco { get; set; }
        public int Abril { get; set; }
        public int Maio { get; set; }
        public int Junho { get; set; }
        public int Julho { get; set; }
        public int Agosto { get; set; }
        public int Setembro { get; set; }
        public int Outubro { get; set; }
        public int Novembro { get; set; }
        public int Dezembro { get; set; }

        public Dictionary<string, int> GetMonthlyData()
        {
            return new Dictionary<string, int>
            {
                { "Janeiro", Janeiro },
                { "Fevereiro", Fevereiro },
                { "Março", Marco },
                { "Abril", Abril },
                { "Maio", Maio },
                { "Junho", Junho },
                { "Julho", Julho },
                { "Agosto", Agosto },
                { "Setembro", Setembro },
                { "Outubro", Outubro },
                { "Novembro", Novembro },
                { "Dezembro", Dezembro }
            };
        }
    }
}
