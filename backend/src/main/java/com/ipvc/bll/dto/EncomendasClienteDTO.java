package com.ipvc.bll.dto;

import com.ipvc.bll.models.EtapasProducao;
import com.ipvc.bll.models.ItensEncomendaCliente;
import com.ipvc.bll.models.TarefasProducao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class EncomendasClienteDTO {

    // DTO para Criar Encomenda de Cliente
    public record EncomendaClienteCreateDTO(
            Integer clienteId,
            LocalDate dataEncomenda,
            Integer estadoId,
            BigDecimal valorTotal
    ) implements Serializable {}

    public record EncomendaClienteCreateFullDTO(
            Integer clienteId,
            LocalDate dataEncomenda,
            Integer estadoId,
            BigDecimal valorTotal,
            List<ItensEncomendaClienteDTO.ItensEncomendaClienteCreateFullDTO> itensEncomenda
    ) implements Serializable {}

    // DTO para Atualizar Encomenda de Cliente
    public record EncomendaClienteUpdateDTO(
            LocalDate dataEncomenda,
            Integer estadoId,
            BigDecimal valorTotal
    ) implements Serializable {}

    // DTO para Responder Requisições
    public record EncomendaClienteResponseDTO(
            Integer id,
            Integer clienteId,
            String clienteNome,
            LocalDate dataEncomenda,
            Integer estadoId,
            String estadoNome,
            BigDecimal valorTotal
    ) implements Serializable {}

    public record EncomendaClienteFullResponseDTO(
            Integer id,
            Integer clienteId,
            String clienteNome,
            LocalDate dataEncomenda,
            Integer estadoId,
            String estadoNome,
            BigDecimal valorTotal,
            List<TarefasProducaoDTO.TarefasProducaoResponseFullDTO> tarefas,
            List<EtapasProducaoDTO.EtapaProducaoResponseDTO> etapas,
            List<ItensEncomendaClienteDTO.ItensEncomendaClienteResponseDTO> itensEncomenda
    ) implements Serializable {}

}
