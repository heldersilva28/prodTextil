package com.ipvc.bll.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

public class FornecedorDTO {

    // DTO para Criar Fornecedor
    public record FornecedorCreateDTO(
            @NotBlank @Size(max = 255) String nome,
            @NotBlank @Email @Size(max = 255) String email,
            @Size(max = 20) String telefone,
            @Size(max = 255) String morada,
            @NotNull String codigoPostalId
    ) implements Serializable {}

    // DTO para Atualizar Fornecedor
    public record FornecedorUpdateDTO(
            @NotBlank @Size(max = 255) String nome,
            @Size(max = 20) String telefone,
            @Size(max = 255) String morada,
            @NotNull String codigoPostalId
    ) implements Serializable {}

    // DTO para Responder Requisições
    public record FornecedorResponseDTO(
            Integer id,
            String nome,
            String email,
            String telefone,
            String morada,
            String codigoPostal
    ) implements Serializable {}
}
