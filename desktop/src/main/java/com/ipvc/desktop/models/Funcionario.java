package com.ipvc.desktop.models;

import java.time.LocalDate;

public class Funcionario {

    private Integer id;
    private String nome;
    private String telefone;
    private int cargo; // id do cargo
    private String cargoNome; // nome do cargo
    private LocalDate dataAdmissao;

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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    public String getCargoNome() {
        return cargoNome;
    }
    public void setCargoNome(String cargoNome) {
        this.cargoNome = cargoNome;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }
}
