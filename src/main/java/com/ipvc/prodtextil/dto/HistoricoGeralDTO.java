package com.ipvc.prodtextil.dto;

import java.io.Serializable;
import java.time.Instant;

public class HistoricoGeralDTO {

    // DTO para Criar Histórico Geral
    public record HistoricoGeralCreateDTO(
            Integer tipoEventoId,
            String descricao,
            Instant dataEvento
    ) implements Serializable {}

    // DTO para Atualizar Histórico Geral
    public record HistoricoGeralUpdateDTO(
            String descricao,
            Instant dataEvento
    ) implements Serializable {}

    // DTO para Responder Requisições
    public record HistoricoGeralResponseDTO(
            Integer id,
            Integer tipoEventoId,
            String descricao,
            Instant dataEvento
    ) implements Serializable {}
}
