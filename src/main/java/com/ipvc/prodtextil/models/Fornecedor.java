package com.ipvc.prodtextil.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "fornecedores", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "fornecedores_email_key", columnNames = {"email"})
})
public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fornecedores_id_gen")
    @SequenceGenerator(name = "fornecedores_id_gen", sequenceName = "fornecedores_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 20)
    @Column(name = "telefone", length = 20)
    private String telefone;

    @Size(max = 255)
    @Column(name = "morada")
    private String morada;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codigo_postal", nullable = false)
    private CodigoPostal codigoPostal;

    @OneToMany(mappedBy = "fornecedor")
    private Set<EncomendaFornecedor> encomendaFornecedors = new LinkedHashSet<>();

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public CodigoPostal getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(CodigoPostal codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Set<EncomendaFornecedor> getEncomendasFornecedores() {
        return encomendaFornecedors;
    }

    public void setEncomendasFornecedores(Set<EncomendaFornecedor> encomendaFornecedors) {
        this.encomendaFornecedors = encomendaFornecedors;
    }

}