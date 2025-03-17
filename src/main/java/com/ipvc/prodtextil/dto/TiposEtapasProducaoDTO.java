package com.ipvc.prodtextil.dto;

import java.io.Serializable;

public class TiposEtapasProducaoDTO {

    // DTO para Criar um Tipo de Etapa de Produção
    public record TiposEtapasProducaoCreateDTO(
            String descricao
    ) implements Serializable {}

    // DTO para Atualizar um Tipo de Etapa de Produção
    public record TiposEtapasProducaoUpdateDTO(
            String descricao
    ) implements Serializable {}

    // DTO para Responder Requisições com os dados do Tipo de Etapa de Produção
    public record TiposEtapasProducaoResponseDTO(
            Integer id,
            String descricao
    ) implements Serializable {}
}
