package com.ipvc.desktop.models;

public class TipoEtapa {
    private Integer id;
    private String descricao;

    public TipoEtapa() {
    }

    public TipoEtapa(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
