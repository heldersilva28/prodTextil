package com.ipvc.desktop.models;

public class EstadoEncomenda {
    private Integer id;
    private String nome;

    // Construtor vazio obrigatório para Jackson
    public EstadoEncomenda() {}

    // Construtor adicional (opcional)
    public EstadoEncomenda(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters e Setters
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

    // Importante para exibir corretamente no ComboBox
    @Override
    public String toString() {
        return nome;
    }
}
