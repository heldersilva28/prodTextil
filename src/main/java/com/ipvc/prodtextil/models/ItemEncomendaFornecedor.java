package com.ipvc.prodtextil.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_encomenda_fornecedor", schema = "public")
public class ItemEncomendaFornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itens_encomenda_fornecedor_id_gen")
    @SequenceGenerator(name = "itens_encomenda_fornecedor_id_gen", sequenceName = "itens_encomenda_fornecedor_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "encomenda_id", nullable = false)
    private EncomendaFornecedor encomenda;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "material_id", nullable = false)
    private Materiais materiais;

    @NotNull
    @Column(name = "quantidade", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidade;

    @NotNull
    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EncomendaFornecedor getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(EncomendaFornecedor encomenda) {
        this.encomenda = encomenda;
    }

    public Materiais getMaterial() {
        return materiais;
    }

    public void setMaterial(Materiais materiais) {
        this.materiais = materiais;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

}