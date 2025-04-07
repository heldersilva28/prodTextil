package com.ipvc.bll.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tipos_etapas_producao", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "tipos_etapas_producao_descricao_key", columnNames = {"descricao"})
})
public class TiposEtapasProducao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipos_etapas_producao_id_gen")
    @SequenceGenerator(name = "tipos_etapas_producao_id_gen", sequenceName = "tipos_etapas_producao_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @OneToMany(mappedBy = "tipoEtapa")
    private Set<EtapasProducao> etapasProducaos = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<EtapasProducao> getEtapasProducaos() {
        return etapasProducaos;
    }

    public void setEtapasProducaos(Set<EtapasProducao> etapasProducaos) {
        this.etapasProducaos = etapasProducaos;
    }

}