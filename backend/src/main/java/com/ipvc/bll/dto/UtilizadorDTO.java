package com.ipvc.bll.dto;

import java.io.Serializable;

public class UtilizadorDTO {

    // DTO para Criar um Utilizador
    public record UtilizadorCreateDTO(
            String username,
            String email,
            String hash,
            Integer tipoUtilizadorId
    ) implements Serializable {}

    // DTO para Atualizar um Utilizador
    public record UtilizadorUpdateDTO(
            Integer tipoUtilizadorId
    ) implements Serializable {}

    // DTO para Responder com os dados do Utilizador
    public record UtilizadorResponseDTO(
            Integer id,
            String username,
            String email,
            Integer tipoUtilizadorId,
            String tipoUtilizadorNome
    ) implements Serializable {}
}
