package com.ipvc.bll.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tipos_materiais", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "tipos_materiais_nome_key", columnNames = {"nome"})
})
public class TiposMateriais {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipos_materiais_id_gen")
    @SequenceGenerator(name = "tipos_materiais_id_gen", sequenceName = "tipos_materiais_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @OneToMany(mappedBy = "tipo")
    private Set<Materiais> materiais = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Materiais> getMateriais() {
        return materiais;
    }

    public void setMateriais(Set<Materiais> materiais) {
        this.materiais = materiais;
    }

}