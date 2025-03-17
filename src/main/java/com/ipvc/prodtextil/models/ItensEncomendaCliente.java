package com.ipvc.prodtextil.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "itens_encomenda_cliente", schema = "public")
@IdClass(ItensEncomendaClientePK.class)
public class ItensEncomendaCliente implements Serializable {

    @Id
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "encomenda_id", nullable = false)
    private EncomendasCliente encomenda;

    @Id
    @Size(max = 255)
    @NotNull
    @Column(name = "produto", nullable = false)
    private String produto;

    @NotNull
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @NotNull
    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    public EncomendasCliente getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(EncomendasCliente encomenda) {
        this.encomenda = encomenda;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}
