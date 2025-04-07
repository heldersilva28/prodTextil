package com.ipvc.bll.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class MateriaisDTO {

    // DTO para Criar Material
    public record MateriaisCreateDTO(
            String nome,
            Integer tipoId,
            BigDecimal precoUnidade,
            BigDecimal stockDisponivel
    ) implements Serializable {}

    // DTO para Atualizar Material
    public record MateriaisUpdateDTO(
            String nome,
            Integer tipoId,
            BigDecimal precoUnidade,
            BigDecimal stockDisponivel
    ) implements Serializable {}

    // DTO para Responder Requisições
    public record MateriaisResponseDTO(
            Integer id,
            String nome,
            Integer tipoId,
            BigDecimal precoUnidade,
            BigDecimal stockDisponivel
    ) implements Serializable {}
}
