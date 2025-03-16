package com.ipvc.prodtextil.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "encomendas_fornecedores", schema = "public")
public class EncomendasFornecedore {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "encomendas_fornecedores_id_gen")
    @SequenceGenerator(name = "encomendas_fornecedores_id_gen", sequenceName = "encomendas_fornecedores_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private com.ipvc.prodtextil.models.Fornecedore fornecedor;

    @NotNull
    @Column(name = "data_pedido", nullable = false)
    private LocalDate dataPedido;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estado_id", nullable = false)
    private com.ipvc.prodtextil.models.EstadosEncomenda estado;

    @NotNull
    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @OneToMany(mappedBy = "encomenda")
    private Set<com.ipvc.prodtextil.models.ItensEncomendaFornecedor> itensEncomendaFornecedors = new LinkedHashSet<>();

    @OneToMany(mappedBy = "encomenda")
    private Set<com.ipvc.prodtextil.models.PagamentosFornecedore> pagamentosFornecedores = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public com.ipvc.prodtextil.models.Fornecedore getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(com.ipvc.prodtextil.models.Fornecedore fornecedor) {
        this.fornecedor = fornecedor;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public com.ipvc.prodtextil.models.EstadosEncomenda getEstado() {
        return estado;
    }

    public void setEstado(com.ipvc.prodtextil.models.EstadosEncomenda estado) {
        this.estado = estado;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Set<com.ipvc.prodtextil.models.ItensEncomendaFornecedor> getItensEncomendaFornecedors() {
        return itensEncomendaFornecedors;
    }

    public void setItensEncomendaFornecedors(Set<com.ipvc.prodtextil.models.ItensEncomendaFornecedor> itensEncomendaFornecedors) {
        this.itensEncomendaFornecedors = itensEncomendaFornecedors;
    }

    public Set<com.ipvc.prodtextil.models.PagamentosFornecedore> getPagamentosFornecedores() {
        return pagamentosFornecedores;
    }

    public void setPagamentosFornecedores(Set<com.ipvc.prodtextil.models.PagamentosFornecedore> pagamentosFornecedores) {
        this.pagamentosFornecedores = pagamentosFornecedores;
    }

}