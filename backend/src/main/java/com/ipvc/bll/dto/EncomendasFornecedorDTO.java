package com.ipvc.bll.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
            String fornecedorNome,
            LocalDate dataPedido,
            Integer estadoId,
            String estadoNome,
            BigDecimal valorTotal
    ) implements Serializable {}

    public record EncomendaFornecedorFullResponseDTO(
            Integer id,
            Integer fornecedorId,
            String fornecedorNome,
            LocalDate dataPedido,
            Integer estadoId,
            String estadoNome,
            BigDecimal valorTotal,
            List<ItensEncomendaFornecedorDTO.ItensEncomendaFornecedorResponseDTO> itensEncomenda
    ) implements Serializable {}
}
