package com.ipvc.prodtextil.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "estados_encomenda", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "estados_encomenda_nome_key", columnNames = {"nome"})
})
public class EstadosEncomenda {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estados_encomenda_id_gen")
    @SequenceGenerator(name = "estados_encomenda_id_gen", sequenceName = "estados_encomenda_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @OneToMany(mappedBy = "estado")
    private Set<EncomendasCliente> encomendasClientes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "estado")
    private Set<EncomendasFornecedore> encomendasFornecedores = new LinkedHashSet<>();

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

    public Set<EncomendasCliente> getEncomendasClientes() {
        return encomendasClientes;
    }

    public void setEncomendasClientes(Set<EncomendasCliente> encomendasClientes) {
        this.encomendasClientes = encomendasClientes;
    }

    public Set<EncomendasFornecedore> getEncomendasFornecedores() {
        return encomendasFornecedores;
    }

    public void setEncomendasFornecedores(Set<EncomendasFornecedore> encomendasFornecedores) {
        this.encomendasFornecedores = encomendasFornecedores;
    }

}