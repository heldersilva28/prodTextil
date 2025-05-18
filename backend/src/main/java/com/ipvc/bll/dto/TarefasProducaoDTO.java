package com.ipvc.bll.dto;

import java.io.Serializable;
import java.time.Instant;

public class TarefasProducaoDTO {

    // DTO para Criar uma Tarefa de Produção
    public record TarefasProducaoCreateDTO(
            Integer encomendaId,
            int descricao,
            Integer funcionarioId,
            Instant dataInicio,
            String estado
    ) implements Serializable {}

    // DTO para Atualizar uma Tarefa de Produção
    public record TarefasProducaoUpdateDTO(
            int descricao,
            Integer funcionarioId,
            Instant dataInicio,
            Instant dataFim,
            String estado
    ) implements Serializable {}

    // DTO para Responder Requisições com os dados da Tarefa de Produção
    public record TarefasProducaoResponseDTO(
            Integer id,
            Integer encomendaId,
            int descricao,
            Integer funcionarioId,
            Instant dataInicio,
            Instant dataFim,
            String estado
    ) implements Serializable {}

    public record TarefasProducaoResponseFullDTO(
            Integer id,
            Integer encomendaId,
            Integer tipoEventoId,
            String descricaoNome,
            Integer funcionarioId,
            String funcionarioNome,
            Instant dataInicio,
            Instant dataFim,
            String estado
    ) implements Serializable {}
}
