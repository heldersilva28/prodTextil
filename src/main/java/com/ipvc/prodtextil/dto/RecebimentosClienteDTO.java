package com.ipvc.prodtextil.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class RecebimentosClienteDTO {

    // DTO para Criar Recebimento de Cliente
    public record RecebimentosClienteCreateDTO(
            Integer encomendaId,
            BigDecimal valorRecebido,
            LocalDate dataRecebimento,
            Integer metodoPagamentoId
    ) implements Serializable {}

    // DTO para Atualizar Recebimento de Cliente
    public record RecebimentosClienteUpdateDTO(
            BigDecimal valorRecebido,
            LocalDate dataRecebimento,
            Integer metodoPagamentoId
    ) implements Serializable {}

    // DTO para Responder Requisições
    public record RecebimentosClienteResponseDTO(
            Integer id,
            Integer encomendaId,
            BigDecimal valorRecebido,
            LocalDate dataRecebimento,
            Integer metodoPagamentoId
    ) implements Serializable {}
}
