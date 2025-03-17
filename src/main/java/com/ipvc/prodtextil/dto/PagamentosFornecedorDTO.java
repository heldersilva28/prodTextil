package com.ipvc.prodtextil.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PagamentosFornecedorDTO {

    // DTO para Criar Pagamento de Fornecedor
    public record PagamentosFornecedorCreateDTO(
            Integer encomendaId,
            BigDecimal valorPago,
            LocalDate dataPagamento,
            Integer metodoPagamentoId
    ) implements Serializable {}

    // DTO para Atualizar Pagamento de Fornecedor
    public record PagamentosFornecedorUpdateDTO(
            BigDecimal valorPago,
            LocalDate dataPagamento,
            Integer metodoPagamentoId
    ) implements Serializable {}

    // DTO para Responder Requisições
    public record PagamentosFornecedorResponseDTO(
            Integer id,
            Integer encomendaId,
            BigDecimal valorPago,
            LocalDate dataPagamento,
            Integer metodoPagamentoId
    ) implements Serializable {}
}
