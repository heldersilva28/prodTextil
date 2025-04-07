package com.ipvc.bll.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

public class ClienteDTO {

    // DTO para Criar Cliente
    public record ClienteCreateDTO(
            @NotBlank @Size(max = 255) String nome,
            @NotBlank @Email @Size(max = 255) String email,
            @NotBlank @Size(max = 255) String morada,
            @Size(max = 9) String telefone,
            @NotNull String codpostalId
    ) implements Serializable {}

    // DTO para Atualizar Cliente
    public record ClienteUpdateDTO(
            @NotBlank @Size(max = 255) String nome,
            @NotBlank @Size(max = 255) String morada,
            @Size(max = 9) String telefone,
            @NotNull String codpostalId
    ) implements Serializable {}

    // DTO para Responder Requisições
    public record ClienteResponseDTO(
            Integer id,
            String nome,
            String email,
            String morada,
            String telefone,
            String codpostal
    ) implements Serializable {}
}
