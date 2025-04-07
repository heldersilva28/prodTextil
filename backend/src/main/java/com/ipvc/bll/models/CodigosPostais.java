package com.ipvc.bll.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "codigos_postais", schema = "public")
public class CodigosPostais {
    @Id
    @Size(max = 10)
    @SequenceGenerator(name = "codigos_postais_id_gen", sequenceName = "clientes_id_seq", allocationSize = 1)
    @Column(name = "codigo", nullable = false, length = 10)
    private String codigo;

    @Size(max = 100)
    @NotNull
    @Column(name = "localidade", nullable = false, length = 100)
    private String localidade;

    @Size(max = 100)
    @NotNull
    @Column(name = "distrito", nullable = false, length = 100)
    private String distrito;

    @Size(max = 50)
    @NotNull
    @ColumnDefault("'Portugal'")
    @Column(name = "pais", nullable = false, length = 50)
    private String pais;

    @OneToMany(mappedBy = "codigoPostal")
    private Set<Cliente> clientes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "codigoPostal")
    private Set<Fornecedor> fornecedors = new LinkedHashSet<>();

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Set<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(Set<Cliente> clientes) {
        this.clientes = clientes;
    }

    public Set<Fornecedor> getFornecedores() {
        return fornecedors;
    }

    public void setFornecedores(Set<Fornecedor> fornecedors) {
        this.fornecedors = fornecedors;
    }

}