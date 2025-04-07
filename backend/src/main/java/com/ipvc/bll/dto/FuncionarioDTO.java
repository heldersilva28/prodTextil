package com.ipvc.bll.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class FuncionarioDTO {

    // DTO para Criar Funcionario
    public record FuncionarioCreateDTO(
            String telefone,  // Agora é Integer, representando o id do Utilizador
            LocalDate dataAdmissao,
            Integer utilizadorId
    ) implements Serializable {}

    // DTO para Atualizar Funcionario
    public record FuncionarioUpdateDTO(
            String telefone,
            Integer cargo  // Agora é Integer, representando o id do Utilizador
    ) implements Serializable {}

    // DTO para Responder Requisições
    public record FuncionarioResponseDTO(
            Integer id,
            String nome,
            String telefone,
            Integer cargo,  // Agora é Integer, representando o id do Utilizador
            LocalDate dataAdmissao
    ) implements Serializable {}
}
