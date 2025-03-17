package com.ipvc.prodtextil.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

public class CodigosPostaisDTO {

    // DTO para Criar CodigoPostal
    public record CodigoPostalCreateDTO(
            @NotNull @Size(max = 10) String codigo,
            @NotNull @Size(max = 100) String localidade,
            @NotNull @Size(max = 100) String distrito,
            @NotNull @Size(max = 50) String pais
    ) implements Serializable {}

    // DTO para Atualizar CodigoPostal
    public record CodigoPostalUpdateDTO(
            @NotNull @Size(max = 100) String localidade,
            @NotNull @Size(max = 100) String distrito,
            @NotNull @Size(max = 50) String pais
    ) implements Serializable {}

    // DTO para Responder Requisições
    public record CodigoPostalResponseDTO(
            String codigo,
            String localidade,
            String distrito,
            String pais
    ) implements Serializable {}
}
