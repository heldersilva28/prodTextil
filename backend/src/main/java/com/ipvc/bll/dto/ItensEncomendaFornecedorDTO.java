package com.ipvc.bll.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItensEncomendaFornecedorDTO {

    // DTO para Criar Item de Encomenda Fornecedor
    public record ItensEncomendaFornecedorCreateDTO(
            Integer encomendaId,  // ID da encomenda
            Integer materialId,   // ID do material
            BigDecimal quantidade, // Quantidade de material
            BigDecimal precoUnitario // Preço unitário do material
    ) implements Serializable {}

    // DTO para Atualizar Item de Encomenda Fornecedor
    public record ItensEncomendaFornecedorUpdateDTO(
            Integer materialId,   // ID do material a ser atualizado
            BigDecimal quantidade, // Nova quantidade
            BigDecimal precoUnitario // Novo preço unitário
    ) implements Serializable {}

    // DTO para Responder Requisições
    public record ItensEncomendaFornecedorResponseDTO(
            Integer encomendaId,  // ID da encomenda
            Integer materialId,   // ID do material
            String materialNome,  // Nome do material
            BigDecimal quantidade, // Quantidade do material
            BigDecimal precoUnitario, // Preço unitário do material
            Double total // Total calculado (quantidade * precoUnitario)

    ) implements Serializable {}
}
