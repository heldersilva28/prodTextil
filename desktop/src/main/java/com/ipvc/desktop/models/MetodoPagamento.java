package com.ipvc.desktop.models;

public class MetodoPagamento {
    private Integer id;
    private String nome;
    private Boolean ativo;

    public MetodoPagamento() {
    }

    public MetodoPagamento(Integer id, String nome, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.ativo = ativo;
    }

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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return nome;
    }
}
