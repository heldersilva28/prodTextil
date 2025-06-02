namespace web.Models
{
    public class OrderDto
    {
        public int Id { get; set; }
        public DateTime DataEncomenda { get; set; }
        public string EstadoNome { get; set; } = string.Empty;
        public decimal ValorTotal { get; set; }
        public List<OrderItemDto> ItensEncomenda { get; set; } = new();
    }

    public class OrderItemDto
    {
        public string Produto { get; set; } = string.Empty;
        public int Quantidade { get; set; }
        public decimal PrecoUnitario { get; set; }
        public decimal Total { get; set; }
    }
}
