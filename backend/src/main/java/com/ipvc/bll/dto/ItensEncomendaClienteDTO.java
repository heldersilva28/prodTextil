package com.ipvc.bll.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItensEncomendaClienteDTO {

    // DTO para Criar Item de Encomenda Cliente
    public record ItensEncomendaClienteCreateDTO(
            Integer encomendaId,   // Refere-se ao ID da encomenda
            String produto,        // Refere-se ao nome ou código do produto
            Integer quantidade,    // Quantidade do produto na encomenda
            BigDecimal precoUnitario // Preço unitário do produto
    ) implements Serializable {}

    // DTO para Atualizar Item de Encomenda Cliente
    public record ItensEncomendaClienteUpdateDTO(
            String produto,        // Produto a ser atualizado
            Integer quantidade,    // Nova quantidade
            BigDecimal precoUnitario // Novo preço unitário
    ) implements Serializable {}

    // DTO para Responder Requisições (Exemplo de resposta do item da encomenda)
    public record ItensEncomendaClienteResponseDTO(
            Integer encomendaId,   // ID da encomenda
            String produto,        // Nome ou código do produto
            Integer quantidade,    // Quantidade do produto
            BigDecimal precoUnitario // Preço unitário do produto
    ) implements Serializable {}
}
