package com.ipvc.bll.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "metodos_pagamento", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "metodos_pagamento_nome_key", columnNames = {"nome"})
})
public class MetodosPagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metodos_pagamento_id_gen")
    @SequenceGenerator(name = "metodos_pagamento_id_gen", sequenceName = "metodos_pagamento_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @OneToMany(mappedBy = "metodoPagamento")
    private Set<PagamentosFornecedor> pagamentosFornecedors = new LinkedHashSet<>();

    @OneToMany(mappedBy = "metodoPagamento")
    private Set<RecebimentosCliente> recebimentosClientes = new LinkedHashSet<>();

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

    public Set<PagamentosFornecedor> getPagamentosFornecedores() {
        return pagamentosFornecedors;
    }

    public void setPagamentosFornecedores(Set<PagamentosFornecedor> pagamentosFornecedors) {
        this.pagamentosFornecedors = pagamentosFornecedors;
    }

    public Set<RecebimentosCliente> getRecebimentosClientes() {
        return recebimentosClientes;
    }

    public void setRecebimentosClientes(Set<RecebimentosCliente> recebimentosClientes) {
        this.recebimentosClientes = recebimentosClientes;
    }

}