package com.ipvc.desktop.models;

public class TipoItem {
    private int id;
    private String nome;

    public TipoItem() {}  // Construtor vazio necessário para o Jackson

    public TipoItem(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
