package com.ipvc.desktop.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EncomendaFornecedor {
    private int id;
    private String fornecedorNome;
    private LocalDate dataEncomenda;
    private int estadoId;
    private String estadoNome;
    private BigDecimal valorTotal;
    private String valorTotalFormatado;

    // Getters e setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFornecedorNome() {
        return fornecedorNome;
    }

    public void setFornecedorNome(String fornecedorNome) {
        this.fornecedorNome = fornecedorNome;
    }

    public LocalDate getDataEncomenda() {
        return dataEncomenda;
    }

    public void setDataEncomenda(LocalDate dataEncomenda) {
        this.dataEncomenda = dataEncomenda;
    }

    public int getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(int estadoId) {
        this.estadoId = estadoId;
    }

    public String getEstadoNome() {
        return estadoNome;
    }

    public void setEstadoNome(String estadoNome) {
        this.estadoNome = estadoNome;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getValorTotalFormatado() {
        return valorTotalFormatado;
    }

    public void setValorTotalFormatado(String valorTotalFormatado) {
        this.valorTotalFormatado = valorTotalFormatado;
    }
}
