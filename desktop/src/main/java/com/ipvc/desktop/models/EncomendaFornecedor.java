package com.ipvc.desktop.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EncomendaFornecedor {
    private Integer id;
    private Integer fornecedorId;
    @JsonProperty("dataPedido")
    private LocalDate dataEncomenda;
    private Integer estadoId;
    private BigDecimal valorTotal;

    private String fornecedorNome;
    private String estadoNome;

    // Novo campo: objeto completo do estado
    private EstadoEncomenda estado;

    // Getters
    public Integer getId() { return id; }
    public Integer getFornecedorId() { return fornecedorId; }
    public LocalDate getDataEncomenda() { return dataEncomenda; }
    public Integer getEstadoId() {
        return estado != null ? estado.getId() : estadoId;
    }
    public BigDecimal getValorTotal() { return valorTotal; }

    public String getFornecedorNome() { return fornecedorNome; }
    public String getEstadoNome() {
        return estado != null ? estado.getNome() : estadoNome;
    }

    public EstadoEncomenda getEstado() {
        return estado;
    }

    // Setters
    public void setId(Integer id) { this.id = id; }
    public void setFornecedorId(Integer fornecedorId) { this.fornecedorId = fornecedorId; }
    public void setDataEncomenda(LocalDate dataEncomenda) { this.dataEncomenda = dataEncomenda; }
    public void setEstadoId(Integer estadoId) { this.estadoId = estadoId; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public void setFornecedorNome(String fornecedorNome) { this.fornecedorNome = fornecedorNome; }
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
