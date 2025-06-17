package com.ipvc.desktop.models;

public class FuncionarioProducaoDTO {
    private Integer id;
    private Integer utilizadorId;
    private String nome;
    private String telefone;
    private Integer cargo;
    private String cargoNome;
    private String dataAdmissao;

    public FuncionarioProducaoDTO() {}

    public FuncionarioProducaoDTO(Integer id, Integer utilizadorId, String nome, String telefone,
                                  Integer cargo, String cargoNome, String dataAdmissao) {
        this.id = id;
        this.utilizadorId = utilizadorId;
        this.nome = nome;
        this.telefone = telefone;
        this.cargo = cargo;
        this.cargoNome = cargoNome;
        this.dataAdmissao = dataAdmissao;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUtilizadorId() { return utilizadorId; }
    public void setUtilizadorId(Integer utilizadorId) { this.utilizadorId = utilizadorId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public Integer getCargo() { return cargo; }
    public void setCargo(Integer cargo) { this.cargo = cargo; }

    public String getCargoNome() { return cargoNome; }
    public void setCargoNome(String cargoNome) { this.cargoNome = cargoNome; }

    public String getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(String dataAdmissao) { this.dataAdmissao = dataAdmissao; }

    @Override
    public String toString() {
        return nome;
    }
}
