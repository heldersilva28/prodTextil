package com.ipvc.bll.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Entity
@Table(name = "etapas_producao", schema = "public")
public class EtapasProducao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "etapas_producao_id_gen")
    @SequenceGenerator(name = "etapas_producao_id_gen", sequenceName = "etapas_producao_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tarefa_id", nullable = false)
    private TarefasProducao tarefa;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_etapa_id", nullable = false)
    private TiposEtapasProducao tipoEtapa;

    @NotNull
    @Column(name = "data_inicio", nullable = false)
    private Instant dataInicio;

    @Column(name = "data_fim")
    private Instant dataFim;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TarefasProducao getTarefa() {
        return tarefa;
    }

    public void setTarefa(TarefasProducao tarefa) {
        this.tarefa = tarefa;
    }

    public TiposEtapasProducao getTipoEtapa() {
        return tipoEtapa;
    }

    public void setTipoEtapa(TiposEtapasProducao tipoEtapa) {
        this.tipoEtapa = tipoEtapa;
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

}