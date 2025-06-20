package com.ipvc.desktop.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RecebimentoCliente {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("clienteId")
    private Integer clienteId;

    @JsonProperty("clienteNome")
    private String clienteNome;

    @JsonProperty("dataEncomenda")
    private LocalDate dataEncomenda;

    @JsonProperty("estadoPagamento")
    private String estadoPagamento;

    @JsonProperty("valorTotal")
    private Double valorTotal;

    @JsonProperty("valorPago")
    private Double valorPago;

    @JsonProperty("metodoPagamentoId")
    private Integer metodoPagamentoId;

    @JsonProperty("metodoPagamentoNome")
    private String metodoPagamentoNome;

    @JsonProperty("encomendaId")
    private Integer encomendaId;

    @JsonProperty("valorRecebido")
    private Double valorRecebido;

    @JsonProperty("dataRecebimento")
    private LocalDate dataRecebimento;

    public RecebimentoCliente() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public LocalDate getDataEncomenda() {
        return dataEncomenda;
    }

    public void setDataEncomenda(LocalDate dataEncomenda) {
        this.dataEncomenda = dataEncomenda;
    }

    public String getDataEncomendaFormatada() {
        if (dataEncomenda == null) {
            return "";
        }
        return dataEncomenda.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getEstadoPagamento() {
        return estadoPagamento;
    }

    public void setEstadoPagamento(String estadoPagamento) {
        this.estadoPagamento = estadoPagamento;
    }

    public Double getValorTotal() {
        return valorTotal != null ? valorTotal : 0.0;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getValorTotalFormatado() {
        return String.format("%.2f", getValorTotal());
    }

    public Double getValorPago() {
        return valorPago;
    }

    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    public String getValorPagoFormatado() {
        if (valorPago == null) {
            return "0.00 ";
        }
        return String.format("%.2f", valorPago);
    }

    public Integer getMetodoPagamentoId() {
        return metodoPagamentoId;
    }

    public void setMetodoPagamentoId(Integer metodoPagamentoId) {
        this.metodoPagamentoId = metodoPagamentoId;
    }

    public String getMetodoPagamentoNome() {
        return metodoPagamentoNome != null ? metodoPagamentoNome : "-";
    }

    public void setMetodoPagamentoNome(String metodoPagamentoNome) {
        this.metodoPagamentoNome = metodoPagamentoNome;
    }

    public Integer getEncomendaId() {
        return encomendaId;
    }

    public void setEncomendaId(Integer encomendaId) {
        this.encomendaId = encomendaId;
    }

    public Double getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(Double valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public LocalDate getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(LocalDate dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public String getDataRecebimentoFormatada() {
        if (dataRecebimento == null) {
            return "";
        }
        return dataRecebimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
