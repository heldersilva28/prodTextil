package com.ipvc.desktop.models;

import java.time.Instant;
import java.time.LocalDate;

public class TarefaDTO {
    private Integer id;
    private Integer encomendaId;
    private Integer descricao;
    private String descricaoNome;
    private Integer funcionarioId;
    private String funcionarioNome;
    private Instant dataInicio;
    private Instant dataFim;
    private String estado;

    public TarefaDTO() {
    }

    public TarefaDTO(Integer id, Integer encomendaId, Integer descricao, String descricaoNome,
                     Integer funcionarioId, String funcionarioNome, Instant dataInicio,
                     Instant dataFim, String estado) {
        this.id = id;
        this.encomendaId = encomendaId;
        this.descricao = descricao;
        this.descricaoNome = descricaoNome;
        this.funcionarioId = funcionarioId;
        this.funcionarioNome = funcionarioNome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.estado = estado;
    }

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

    public Integer getDescricao() {
        return descricao;
    }

    public void setDescricao(Integer descricao) {
        this.descricao = descricao;
    }

    public String getDescricaoNome() {
        return descricaoNome;
    }

    public void setDescricaoNome(String descricaoNome) {
        this.descricaoNome = descricaoNome;
    }

    public Integer getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Integer funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public String getFuncionarioNome() {
        return funcionarioNome;
    }

    public void setFuncionarioNome(String funcionarioNome) {
        this.funcionarioNome = funcionarioNome;
    }

    public Instant getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Instant dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Instant getDataFim() {
        return dataFim;
    }

    public void setDataFim(Instant dataFim) {
        this.dataFim = dataFim;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Tarefa #" + id + " - " + descricaoNome;
    }
}
