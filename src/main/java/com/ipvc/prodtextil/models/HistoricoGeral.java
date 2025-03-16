package com.ipvc.prodtextil.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "historico_geral", schema = "public")
public class HistoricoGeral {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historico_geral_id_gen")
    @SequenceGenerator(name = "historico_geral_id_gen", sequenceName = "historico_geral_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_evento_id", nullable = false)
    private com.ipvc.prodtextil.models.TiposEvento tipoEvento;

    @NotNull
    @Column(name = "descricao", nullable = false, length = Integer.MAX_VALUE)
    private String descricao;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_evento")
    private Instant dataEvento;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public com.ipvc.prodtextil.models.TiposEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(com.ipvc.prodtextil.models.TiposEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Instant getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(Instant dataEvento) {
        this.dataEvento = dataEvento;
    }

}