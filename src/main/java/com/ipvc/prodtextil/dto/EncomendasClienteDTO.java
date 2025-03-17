package com.ipvc.prodtextil.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EncomendasClienteDTO {

    // DTO para Criar Encomenda de Cliente
    public record EncomendaClienteCreateDTO(
            Integer clienteId,
            LocalDate dataEncomenda,
            Integer estadoId,
            BigDecimal valorTotal
    ) implements Serializable {}

    // DTO para Atualizar Encomenda de Cliente
    public record EncomendaClienteUpdateDTO(
            LocalDate dataEncomenda,
            Integer estadoId,
            BigDecimal valorTotal
    ) implements Serializable {}

    // DTO para Responder Requisições
    public record EncomendaClienteResponseDTO(
            Integer id,
            Integer clienteId,
            LocalDate dataEncomenda,
            Integer estadoId,
            BigDecimal valorTotal
    ) implements Serializable {}
}
