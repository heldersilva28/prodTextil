package com.ipvc.prodtextil.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_encomenda_fornecedor", schema = "public")
public class ItensEncomendaFornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itens_encomenda_fornecedor_id_gen")
    @SequenceGenerator(name = "itens_encomenda_fornecedor_id_gen", sequenceName = "itens_encomenda_fornecedor_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "encomenda_id", nullable = false)
    private EncomendasFornecedore encomenda;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "material_id", nullable = false)
    private com.ipvc.prodtextil.models.Materiai material;

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

    public EncomendasFornecedore getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(EncomendasFornecedore encomenda) {
        this.encomenda = encomenda;
    }

    public com.ipvc.prodtextil.models.Materiai getMaterial() {
        return material;
    }

    public void setMaterial(com.ipvc.prodtextil.models.Materiai material) {
        this.material = material;
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