package com.ipvc.prodtextil.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "recebimentos_clientes", schema = "public")
public class RecebimentosCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recebimentos_clientes_id_gen")
    @SequenceGenerator(name = "recebimentos_clientes_id_gen", sequenceName = "recebimentos_clientes_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "encomenda_id", nullable = false)
    private EncomendaCliente encomenda;

    @NotNull
    @Column(name = "valor_recebido", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorRecebido;

    @NotNull
    @Column(name = "data_recebimento", nullable = false)
    private LocalDate dataRecebimento;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "metodo_pagamento_id", nullable = false)
    private MetodosPagamento metodoPagamento;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EncomendaCliente getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(EncomendaCliente encomenda) {
        this.encomenda = encomenda;
    }

    public BigDecimal getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public LocalDate getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(LocalDate dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public MetodosPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodosPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

}