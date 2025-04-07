package com.ipvc.bll.dto;

import java.io.Serializable;
import java.time.Instant;

public class EtapasProducaoDTO {

    // DTO para Criar Etapa de Produção
    public record EtapaProducaoCreateDTO(
            Integer tarefaId,
            Integer tipoEtapaId,
            Instant dataInicio,
            Instant dataFim
    ) implements Serializable {}

    // DTO para Atualizar Etapa de Produção
    public record EtapaProducaoUpdateDTO(
            Instant dataInicio,
            Instant dataFim
    ) implements Serializable {}

    // DTO para Responder Requisições
    public record EtapaProducaoResponseDTO(
            Integer id,
            Integer tarefaId,
            Integer tipoEtapaId,
            Instant dataInicio,
            Instant dataFim
    ) implements Serializable {}
}
