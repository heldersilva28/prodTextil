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
public class Fornecedore {
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
    private CodigosPostai codigoPostal;

    @OneToMany(mappedBy = "fornecedor")
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

    public CodigosPostai getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(CodigosPostai codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Set<EncomendasFornecedore> getEncomendasFornecedores() {
        return encomendasFornecedores;
    }

    public void setEncomendasFornecedores(Set<EncomendasFornecedore> encomendasFornecedores) {
        this.encomendasFornecedores = encomendasFornecedores;
    }

}