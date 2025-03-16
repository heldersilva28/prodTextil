package com.ipvc.prodtextil.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "materiais", schema = "public")
public class Materiais {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "materiais_id_gen")
    @SequenceGenerator(name = "materiais_id_gen", sequenceName = "materiais_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_id", nullable = false)
    private TiposMateriais tipo;

    @NotNull
    @Column(name = "preco_unidade", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnidade;

    @NotNull
    @Column(name = "stock_disponivel", nullable = false, precision = 10, scale = 2)
    private BigDecimal stockDisponivel;

    @OneToMany(mappedBy = "material")
    private Set<ConsumoMateriais> consumoMateriais = new LinkedHashSet<>();

    @OneToMany(mappedBy = "material")
    private Set<ItemEncomendaFornecedor> itemEncomendaFornecedors = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TiposMateriais getTipo() {
        return tipo;
    }

    public void setTipo(TiposMateriais tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPrecoUnidade() {
        return precoUnidade;
    }

    public void setPrecoUnidade(BigDecimal precoUnidade) {
        this.precoUnidade = precoUnidade;
    }

    public BigDecimal getStockDisponivel() {
        return stockDisponivel;
    }

    public void setStockDisponivel(BigDecimal stockDisponivel) {
        this.stockDisponivel = stockDisponivel;
    }

    public Set<ConsumoMateriais> getConsumoMateriais() {
        return consumoMateriais;
    }

    public void setConsumoMateriais(Set<ConsumoMateriais> consumoMateriais) {
        this.consumoMateriais = consumoMateriais;
    }

    public Set<ItemEncomendaFornecedor> getItensEncomendaFornecedors() {
        return itemEncomendaFornecedors;
    }

    public void setItensEncomendaFornecedors(Set<ItemEncomendaFornecedor> itemEncomendaFornecedors) {
        this.itemEncomendaFornecedors = itemEncomendaFornecedors;
    }

}