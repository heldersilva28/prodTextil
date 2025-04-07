package com.ipvc.bll.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EncomendasFornecedorDTO {

    // DTO para Criar Encomenda de Fornecedor
    public record EncomendaFornecedorCreateDTO(
            Integer fornecedorId,
            LocalDate dataPedido,
            Integer estadoId,
            BigDecimal valorTotal
    ) implements Serializable {}

    // DTO para Atualizar Encomenda de Fornecedor
    public record EncomendaFornecedorUpdateDTO(
            LocalDate dataPedido,
            Integer estadoId,
            BigDecimal valorTotal
    ) implements Serializable {}

    // DTO para Responder Requisições
    public record EncomendaFornecedorResponseDTO(
            Integer id,
            Integer fornecedorId,
            LocalDate dataPedido,
            Integer estadoId,
            BigDecimal valorTotal
    ) implements Serializable {}
}
