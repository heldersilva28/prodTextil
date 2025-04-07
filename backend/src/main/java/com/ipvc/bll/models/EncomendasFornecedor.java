package com.ipvc.bll.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "encomendas_fornecedores", schema = "public")
public class EncomendasFornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "encomendas_fornecedores_id_gen")
    @SequenceGenerator(name = "encomendas_fornecedores_id_gen", sequenceName = "encomendas_fornecedores_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @NotNull
    @Column(name = "data_pedido", nullable = false)
    private LocalDate dataPedido;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadosEncomenda estado;

    @NotNull
    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @OneToMany(mappedBy = "encomenda")
    private Set<ItensEncomendaFornecedor> itensEncomendaFornecedors = new LinkedHashSet<>();

    @OneToMany(mappedBy = "encomenda")
    private Set<PagamentosFornecedor> pagamentosFornecedors = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public EstadosEncomenda getEstado() {
        return estado;
    }

    public void setEstado(EstadosEncomenda estado) {
        this.estado = estado;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Set<ItensEncomendaFornecedor> getItensEncomendaFornecedors() {
        return itensEncomendaFornecedors;
    }

    public void setItensEncomendaFornecedors(Set<ItensEncomendaFornecedor> itensEncomendaFornecedors) {
        this.itensEncomendaFornecedors = itensEncomendaFornecedors;
    }

    public Set<PagamentosFornecedor> getPagamentosFornecedores() {
        return pagamentosFornecedors;
    }

    public void setPagamentosFornecedores(Set<PagamentosFornecedor> pagamentosFornecedors) {
        this.pagamentosFornecedors = pagamentosFornecedors;
    }

}