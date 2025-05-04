package com.ipvc.desktop.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Cliente {

    private int id;
    private String nome;
    private String email;
    private String telefone;
    private String morada;
    @JsonProperty("codpostal")
    private String codigo_postal;

    // Construtor vazio necessário para o Jackson
    public Cliente() {
    }

    // Construtor completo (opcional)
    public Cliente(int id, String nome, String email, String telefone, String morada, String codigo_postal) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.morada = morada;
        this.codigo_postal = codigo_postal;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getCodigo_postal() {
        return codigo_postal;
    }

    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }

    @Override
    public String toString() {
        return nome + " (" + email + ")";
    }
}
