package com.ipvc.prodtextil.dto;

import java.io.Serializable;

public class EstadosEncomendaDTO {

    // DTO para Criar Estado de Encomenda
    public record EstadoEncomendaCreateDTO(
            String nome
    ) implements Serializable {}

    // DTO para Atualizar Estado de Encomenda
    public record EstadoEncomendaUpdateDTO(
            String nome
    ) implements Serializable {}

    // DTO para Responder Requisições
    public record EstadoEncomendaResponseDTO(
            Integer id,
            String nome
    ) implements Serializable {}
}
