package com.ipvc.prodtextil.dto;

import java.io.Serializable;

public class TiposEventoDTO {

    // DTO para Criar um Tipo de Evento
    public record TiposEventoCreateDTO(
            String nome
    ) implements Serializable {}

    // DTO para Atualizar um Tipo de Evento
    public record TiposEventoUpdateDTO(
            String nome
    ) implements Serializable {}

    // DTO para Responder Requisições com os dados do Tipo de Evento
    public record TiposEventoResponseDTO(
            Integer id,
            String nome
    ) implements Serializable {}
}
