package com.ipvc.prodtextil.dto;

import java.io.Serializable;

public class TiposMateriaisDTO {

    // DTO para Criar um Tipo de Material
    public record TiposMateriaisCreateDTO(
            String nome
    ) implements Serializable {}

    // DTO para Atualizar um Tipo de Material
    public record TiposMateriaisUpdateDTO(
            String nome
    ) implements Serializable {}

    // DTO para Responder Requisições com os dados do Tipo de Material
    public record TiposMateriaisResponseDTO(
            Integer id,
            String nome
    ) implements Serializable {}
}
