package com.ipvc.desktop.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EncomendaCliente {
    private Integer id;
    private Integer clienteId;
    private LocalDate dataEncomenda;
    private Integer estadoId;
    private BigDecimal valorTotal;

    private String clienteNome; // Nome do cliente (preenchido do backend ou mapeado)
    private String estadoNome;  // Nome do estado da encomenda (preenchido do backend ou mapeado)

    // Getters
    public Integer getId() { return id; }
    public Integer getClienteId() { return clienteId; }
    public LocalDate getDataEncomenda() { return dataEncomenda; }
    public Integer getEstadoId() { return estadoId; }
    public BigDecimal getValorTotal() { return valorTotal; }

    public String getClienteNome() { return clienteNome; }
    public String getEstadoNome() { return estadoNome; }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }
    public void setDataEncomenda(LocalDate dataEncomenda) { this.dataEncomenda = dataEncomenda; }
    public void setEstadoId(Integer estadoId) { this.estadoId = estadoId; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }
    public void setEstadoNome(String estadoNome) { this.estadoNome = estadoNome; }

    // Método para formatar o valorTotal em formato €
    public String getValorTotalFormatado() {
        return valorTotal != null ? String.format("%.2f €", valorTotal) : "0.00 €";
    }

    // Método para formatar a data
    public String getDataEncomendaFormatada() {
        return dataEncomenda != null ? dataEncomenda.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }
}
