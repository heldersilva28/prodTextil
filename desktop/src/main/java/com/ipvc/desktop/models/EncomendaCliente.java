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

    private String clienteNome;
    private String estadoNome;

    // Novo campo: objeto completo do estado
    private EstadoEncomenda estado;

    // Getters
    public Integer getId() { return id; }
    public Integer getClienteId() { return clienteId; }
    public LocalDate getDataEncomenda() { return dataEncomenda; }
    public Integer getEstadoId() {
        return estado != null ? estado.getId() : estadoId;
    }
    public BigDecimal getValorTotal() { return valorTotal; }

    public String getClienteNome() { return clienteNome; }
    public String getEstadoNome() {
        return estado != null ? estado.getNome() : estadoNome;
    }

    public EstadoEncomenda getEstado() {
        return estado;
    }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }
    public void setDataEncomenda(LocalDate dataEncomenda) { this.dataEncomenda = dataEncomenda; }
    public void setEstadoId(Integer estadoId) { this.estadoId = estadoId; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }
    public void setEstadoNome(String estadoNome) { this.estadoNome = estadoNome; }

    public void setEstado(EstadoEncomenda estado) {
        this.estado = estado;
        if (estado != null) {
            this.estadoId = estado.getId(); // Sincroniza estadoId
            this.estadoNome = estado.getNome(); // Sincroniza nome para exibição
        }
    }

    // Métodos utilitários
    public String getValorTotalFormatado() {
        return valorTotal != null ? String.format("%.2f €", valorTotal) : "0.00 €";
    }

    public String getDataEncomendaFormatada() {
        return dataEncomenda != null ? dataEncomenda.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }
}

