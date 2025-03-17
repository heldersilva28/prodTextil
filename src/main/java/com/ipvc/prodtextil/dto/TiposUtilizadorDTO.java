package com.ipvc.prodtextil.dto;

import java.io.Serializable;

public class TiposUtilizadorDTO {

    // DTO para Criar um Tipo de Utilizador
    public record TiposUtilizadorCreateDTO(
            String nome
    ) implements Serializable {}

    // DTO para Atualizar um Tipo de Utilizador
    public record TiposUtilizadorUpdateDTO(
            String nome
    ) implements Serializable {}

    // DTO para Responder com os dados do Tipo de Utilizador
    public record TiposUtilizadorResponseDTO(
            Integer id,
            String nome
    ) implements Serializable {}
}
