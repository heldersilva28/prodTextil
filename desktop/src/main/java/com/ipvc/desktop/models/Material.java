package com.ipvc.desktop.models;

import javafx.beans.property.*;

import java.math.BigDecimal;

public class Material {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nome = new SimpleStringProperty();
    private final IntegerProperty tipoId = new SimpleIntegerProperty();
    private final StringProperty tipoNome = new SimpleStringProperty();
    private final ObjectProperty<BigDecimal> precoUnidade = new SimpleObjectProperty<>();
    private final ObjectProperty<BigDecimal> stockDisponivel = new SimpleObjectProperty<>();

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNome() {
        return nome.get();
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public int getTipoId() {
        return tipoId.get();
    }

    public IntegerProperty tipoIdProperty() {
        return tipoId;
    }

    public void setTipoId(int tipoId) {
        this.tipoId.set(tipoId);
    }

    public String getTipoNome() {
        return tipoNome.get();
    }

    public StringProperty tipoNomeProperty() {
        return tipoNome;
    }

    public void setTipoNome(String tipoNome) {
        this.tipoNome.set(tipoNome);
    }

    public BigDecimal getPrecoUnidade() {
        return precoUnidade.get();
    }

    public ObjectProperty<BigDecimal> precoUnidadeProperty() {
        return precoUnidade;
    }

    public void setPrecoUnidade(BigDecimal precoUnidade) {
        this.precoUnidade.set(precoUnidade);
    }

    public BigDecimal getStockDisponivel() {
        return stockDisponivel.get();
    }

    public ObjectProperty<BigDecimal> stockDisponivelProperty() {
        return stockDisponivel;
    }

    public void setStockDisponivel(BigDecimal stockDisponivel) {
        this.stockDisponivel.set(stockDisponivel);
    }
}
