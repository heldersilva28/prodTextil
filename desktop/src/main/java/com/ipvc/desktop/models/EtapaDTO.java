package com.ipvc.desktop.models;

import java.time.LocalDate;

public class EtapaDTO {
    private Integer id;
    private Integer tarefaId;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public EtapaDTO() {
    }

    public EtapaDTO(Integer id, Integer tarefaId, String descricao,
                  LocalDate dataInicio, LocalDate dataFim) {
        this.id = id;
        this.tarefaId = tarefaId;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTarefaId() {
        return tarefaId;
    }

    public void setTarefaId(Integer tarefaId) {
        this.tarefaId = tarefaId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    @Override
    public String toString() {
        return "Etapa #" + id + " - " + descricao;
    }
}
