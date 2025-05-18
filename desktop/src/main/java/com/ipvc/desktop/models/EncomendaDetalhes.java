package com.ipvc.desktop.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EncomendaDetalhes {
    private Integer id;
    private Integer clienteId;
    private String clienteNome;
    private LocalDate dataEncomenda;
    private Integer estadoId;
    private String estadoNome;
    private Double valorTotal;

    // Informações de tarefas
    private List<Tarefa> tarefas;

    // Informações de etapas
    private List<Etapa> etapas;

    // Construtor padrão necessário para Jackson
    public EncomendaDetalhes() {}

    // Construtor completo
    @JsonCreator
    public EncomendaDetalhes(
            @JsonProperty("id") Integer id,
            @JsonProperty("clienteId") Integer clienteId,
            @JsonProperty("clienteNome") String clienteNome,
            @JsonProperty("dataEncomenda") LocalDate dataEncomenda,
            @JsonProperty("estadoId") Integer estadoId,
            @JsonProperty("estadoNome") String estadoNome,
            @JsonProperty("valorTotal") Double valorTotal,
            @JsonProperty("tarefas") List<Tarefa> tarefas,
            @JsonProperty("etapas") List<Etapa> etapas) {
        this.id = id;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.dataEncomenda = dataEncomenda;
        this.estadoId = estadoId;
        this.estadoNome = estadoNome;
        this.valorTotal = valorTotal;
        this.tarefas = tarefas;
        this.etapas = etapas;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public LocalDate getDataEncomenda() {
        return dataEncomenda;
    }

    public void setDataEncomenda(LocalDate dataEncomenda) {
        this.dataEncomenda = dataEncomenda;
    }

    public Integer getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Integer estadoId) {
        this.estadoId = estadoId;
    }

    public String getEstadoNome() {
        return estadoNome;
    }

    public void setEstadoNome(String estadoNome) {
        this.estadoNome = estadoNome;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    public List<Etapa> getEtapas() {
        return etapas;
    }

    public void setEtapas(List<Etapa> etapas) {
        this.etapas = etapas;
    }

    // Classe interna para representar Tarefas
    @JsonIgnoreProperties(ignoreUnknown = true) // Ignora propriedades desconhecidas
    public static class Tarefa {
        private Integer id;
        private Integer encomendaId;
        private String descricaoNome;
        private String funcionarioNome; // Nome do funcionário
        private LocalDateTime dataInicio;
        private LocalDateTime dataFim;
        private String estado;

        // Construtor padrão necessário para Jackson
        public Tarefa() {}

        @JsonCreator
        public Tarefa(
                @JsonProperty("id") Integer id,
                @JsonProperty("encomendaId") Integer encomendaId,
                @JsonProperty("descricaoNome") String descricaoNome,
                @JsonProperty("funcionarioNome") String funcionarioNome, // Mapeia o nome do funcionário
                @JsonProperty("dataInicio") LocalDateTime dataInicio,
                @JsonProperty("dataFim") LocalDateTime dataFim,
                @JsonProperty("estado") String estado) {
            this.id = id;
            this.encomendaId = encomendaId;
            this.descricaoNome = descricaoNome;
            this.funcionarioNome = funcionarioNome;
            this.dataInicio = dataInicio;
            this.dataFim = dataFim;
            this.estado = estado;
        }

        // Getters e Setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getEncomendaId() {
            return encomendaId;
        }

        public void setEncomendaId(Integer encomendaId) {
            this.encomendaId = encomendaId;
        }

        public String getDescricaoNome() {
            return descricaoNome;
        }

        public void setDescricaoNome(String descricaoNome) {
            this.descricaoNome = descricaoNome;
        }

        public String getFuncionarioNome() {
            return funcionarioNome;
        }

        public void setFuncionarioNome(String funcionarioNome) {
            this.funcionarioNome = funcionarioNome;
        }

        public LocalDateTime getDataInicio() {
            return dataInicio;
        }

        public void setDataInicio(LocalDateTime dataInicio) {
            this.dataInicio = dataInicio;
        }

        public LocalDateTime getDataFim() {
            return dataFim;
        }

        public void setDataFim(LocalDateTime dataFim) {
            this.dataFim = dataFim;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }
    }

    // Classe interna para representar Etapas
    @JsonIgnoreProperties(ignoreUnknown = true) // Ignora propriedades desconhecidas
    public static class Etapa {
        private Integer id;
        private Integer tipoEtapaId;
        private LocalDateTime dataInicio;
        private LocalDateTime dataFim;

        // Construtor padrão necessário para Jackson
        public Etapa() {}

        @JsonCreator
        public Etapa(
                @JsonProperty("id") Integer id,
                @JsonProperty("tipoEtapaId") Integer tipoEtapaId,
                @JsonProperty("dataInicio") LocalDateTime dataInicio,
                @JsonProperty("dataFim") LocalDateTime dataFim) {
            this.id = id;
            this.tipoEtapaId = tipoEtapaId;
            this.dataInicio = dataInicio;
            this.dataFim = dataFim;
        }

        // Getters e Setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getTipoEtapaId() {
            return tipoEtapaId;
        }

        public void setTipoEtapaId(Integer tipoEtapaId) {
            this.tipoEtapaId = tipoEtapaId;
        }

        public LocalDateTime getDataInicio() {
            return dataInicio;
        }

        public void setDataInicio(LocalDateTime dataInicio) {
            this.dataInicio = dataInicio;
        }

        public LocalDateTime getDataFim() {
            return dataFim;
        }

        public void setDataFim(LocalDateTime dataFim) {
            this.dataFim = dataFim;
        }
    }
}
