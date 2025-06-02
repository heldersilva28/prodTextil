// Models/OrderDto.cs
using System;
using System.Collections.Generic;
using System.Text.Json.Serialization;

namespace YourAppNamespace.Models
{
    public class OrderDto
    {
        // Não vamos expor ID do pedido ou do cliente, mas precisamos mapear para deserializar
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("clienteId")]
        public int ClienteId { get; set; }

        [JsonPropertyName("clienteNome")]
        public string ClienteNome { get; set; }

        [JsonPropertyName("dataEncomenda")]
        public DateTime DataEncomenda { get; set; }

        [JsonPropertyName("estadoId")]
        public int EstadoId { get; set; }

        [JsonPropertyName("estadoNome")]
        public string EstadoNome { get; set; }

        [JsonPropertyName("valorTotal")]
        public decimal ValorTotal { get; set; }

        // Ignoramos “tarefas” e “etapas” pois o cliente não vai ver. 
        // Mas precisamos incluir para que o JSON não quebre (vamos colocar [JsonIgnore] ou simplesmente não usar)
        [JsonPropertyName("tarefas")]
        [JsonIgnore]
        public object[] Tarefas { get; set; }

        [JsonPropertyName("etapas")]
        [JsonIgnore]
        public object[] Etapas { get; set; }

        [JsonPropertyName("itensEncomenda")]
        public List<OrderItemDto> ItensEncomenda { get; set; }
    }

    public class OrderItemDto
    {
        // Não expor encomendaId nem ID do item; só o que interessa ao cliente
        [JsonPropertyName("encomendaId")]
        [JsonIgnore]
        public int EncomendaId { get; set; }

        // Expondo apenas nome do produto, quantidade, preço e total
        [JsonPropertyName("produto")]
        public string Produto { get; set; }

        [JsonPropertyName("quantidade")]
        public int Quantidade { get; set; }

        [JsonPropertyName("precoUnitario")]
        public decimal PrecoUnitario { get; set; }

        [JsonPropertyName("total")]
        public decimal Total { get; set; }
    }
}
