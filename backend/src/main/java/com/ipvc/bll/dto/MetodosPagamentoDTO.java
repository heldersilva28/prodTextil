package com.ipvc.bll.dto;

import java.io.Serializable;

public class MetodosPagamentoDTO {

    // DTO para Criar Método de Pagamento
    public record MetodosPagamentoCreateDTO(
            String nome
    ) implements Serializable {}

    // DTO para Atualizar Método de Pagamento
    public record MetodosPagamentoUpdateDTO(
            String nome
    ) implements Serializable {}

    // DTO para Responder Requisições
    public record MetodosPagamentoResponseDTO(
            Integer id,
            String nome
    ) implements Serializable {}
}
