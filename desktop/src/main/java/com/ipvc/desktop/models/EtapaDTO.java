package com.ipvc.desktop.models;

import java.time.Instant;

public class EtapaDTO {
    private Integer id;
    private Integer tarefaId;
    private Integer tipoEtapaId; // Adicionado o id do tipo de etapa
    private String descricao;
    private Instant dataInicio;
    private Instant dataFim;

    public EtapaDTO() {
    }

    public EtapaDTO(Integer id, Integer tarefaId, Integer tipoEtapaId, String descricao,
                  Instant dataInicio, Instant dataFim) {
        this.id = id;
        this.tarefaId = tarefaId;
        this.tipoEtapaId = tipoEtapaId;
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

    public Integer getTipoEtapaId() {
        return tipoEtapaId;
    }

    public void setTipoEtapaId(Integer tipoEtapaId) {
        this.tipoEtapaId = tipoEtapaId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    @Override
    public String toString() {
        return "Etapa #" + id + " - " + descricao;
    }
}
