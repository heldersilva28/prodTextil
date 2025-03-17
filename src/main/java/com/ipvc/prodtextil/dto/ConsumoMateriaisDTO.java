package com.ipvc.prodtextil.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public class ConsumoMateriaisDTO {

    // DTO para Criar Consumo de Materiais
    public record ConsumoMateriaisCreateDTO(
            Integer materialId,
            BigDecimal quantidadeConsumida
    ) implements Serializable {}

    // DTO para Atualizar Consumo de Materiais
    public record ConsumoMateriaisUpdateDTO(
            BigDecimal quantidadeConsumida
    ) implements Serializable {}

    // DTO para Responder Requisições
    public record ConsumoMateriaisResponseDTO(
            Integer id,
            Integer materialId,
            BigDecimal quantidadeConsumida,
            Instant dataRegisto
    ) implements Serializable {}
}
