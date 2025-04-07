package com.ipvc.bll.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pagamentos_fornecedores", schema = "public")
public class PagamentosFornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pagamentos_fornecedores_id_gen")
    @SequenceGenerator(name = "pagamentos_fornecedores_id_gen", sequenceName = "pagamentos_fornecedores_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "encomenda_id", nullable = false)
    private EncomendasFornecedor encomenda;

    @NotNull
    @Column(name = "valor_pago", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorPago;

    @NotNull
    @Column(name = "data_pagamento", nullable = false)
    private LocalDate dataPagamento;

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

    public EncomendasFornecedor getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(EncomendasFornecedor encomenda) {
        this.encomenda = encomenda;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public MetodosPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodosPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

}