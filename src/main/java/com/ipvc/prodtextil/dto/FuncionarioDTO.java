package com.ipvc.prodtextil.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

public class FuncionarioDTO {

    // DTO para Criar Cliente
    public record FuncionarioCreateDTO(
            @NotBlank @Size(max = 255) String nome,
            @NotBlank @Email @Size(max = 255) String email,
            @Size(max = 9) String telefone,
            @NotNull String cargo,
            @NotNull LocalDate dataAdmissao
    ) implements Serializable {}

    // DTO para Atualizar Cliente
    public record FuncionarioUpdateDTO(
            @NotBlank @Size(max = 255) String nome,
            @Size(max = 9) String telefone,
            @NotNull String cargo
    ) implements Serializable {}

    // DTO para Responder Requisições
    public record FuncionarioResponseDTO(
            Integer id,
            String nome,
            String email,
            String telefone,
            String cargo,
            LocalDate dataAdmissao
    ) implements Serializable {}
}
