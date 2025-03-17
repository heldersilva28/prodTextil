package com.ipvc.prodtextil.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tipos_utilizadores", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "tipos_utilizadores_nome_key", columnNames = {"nome"})
})
public class TiposUtilizador {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipos_utilizadores_id_gen")
    @SequenceGenerator(name = "tipos_utilizadores_id_gen", sequenceName = "tipos_utilizadores_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @OneToMany(mappedBy = "tipoUtilizador")
    private Set<Utilizador> utilizadors = new LinkedHashSet<>();

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

    public Set<Utilizador> getUtilizadores() {
        return utilizadors;
    }

    public void setUtilizadores(Set<Utilizador> utilizadors) {
        this.utilizadors = utilizadors;
    }

}