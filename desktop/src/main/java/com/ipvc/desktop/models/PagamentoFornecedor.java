package com.ipvc.desktop.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PagamentoFornecedor {
    private Integer id;
    private Integer fornecedorId;
    private String fornecedorNome;
    private LocalDate dataEncomenda;
    private String estadoPagamento;
    private Double valorTotal;
    private Double valorPago;
    private Integer metodoPagamentoId;
    private String metodoPagamentoNome;

    // Construtores
    public PagamentoFornecedor() {
    }

    public PagamentoFornecedor(Integer id, Integer fornecedorId, String fornecedorNome,
                              LocalDate dataEncomenda, String estadoPagamento,
                              Double valorTotal, Double valorPago,
                              Integer metodoPagamentoId, String metodoPagamentoNome) {
        this.id = id;
        this.fornecedorId = fornecedorId;
        this.fornecedorNome = fornecedorNome;
        this.dataEncomenda = dataEncomenda;
        this.estadoPagamento = estadoPagamento;
        this.valorTotal = valorTotal;
        this.valorPago = valorPago;
        this.metodoPagamentoId = metodoPagamentoId;
        this.metodoPagamentoNome = metodoPagamentoNome;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Integer fornecedorId) {
        this.fornecedorId = fornecedorId;
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

    public String getDataEncomendaFormatada() {
        if (dataEncomenda != null) {
            return dataEncomenda.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return "";
    }

    public String getEstadoPagamento() {
        return estadoPagamento;
    }

    public void setEstadoPagamento(String estadoPagamento) {
        this.estadoPagamento = estadoPagamento;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getValorTotalFormatado() {
        if (valorTotal != null) {
            return String.format("%.2f", valorTotal);
        }
        return "0.00";
    }

    public Double getValorPago() {
        return valorPago;
    }

    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    public String getValorPagoFormatado() {
        if (valorPago != null) {
            return String.format("%.2f", valorPago);
        }
        return "0.00";
    }

    public Integer getMetodoPagamentoId() {
        return metodoPagamentoId;
    }

    public void setMetodoPagamentoId(Integer metodoPagamentoId) {
        this.metodoPagamentoId = metodoPagamentoId;
    }

    public String getMetodoPagamentoNome() {
        return metodoPagamentoNome;
    }

    public void setMetodoPagamentoNome(String metodoPagamentoNome) {
        this.metodoPagamentoNome = metodoPagamentoNome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PagamentoFornecedor that = (PagamentoFornecedor) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PagamentoFornecedor{" +
                "id=" + id +
                ", fornecedorNome='" + fornecedorNome + '\'' +
                ", estadoPagamento='" + estadoPagamento + '\'' +
                ", valorTotal=" + valorTotal +
                ", valorPago=" + valorPago +
                '}';
    }
}
