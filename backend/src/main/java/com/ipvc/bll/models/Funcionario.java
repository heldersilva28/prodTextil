package com.ipvc.bll.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "funcionarios", schema = "public")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "funcionarios_id_gen")
    @SequenceGenerator(name = "funcionarios_id_gen", sequenceName = "funcionarios_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilizador_id")
    private Utilizador utilizador;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)  // Alteração para criar FK com a tabela Utilizadores
    @JoinColumn(name = "cargo", referencedColumnName = "id", nullable = false)  // A FK agora aponta para o id de Utilizadores
    private TiposUtilizador cargo;  // Cargo agora é uma chave estrangeira para Utilizador

    @Size(max = 20)
    @Column(name = "telefone", length = 20)
    private String telefone;

    @NotNull
    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    @OneToMany(mappedBy = "funcionario")
    private Set<TarefasProducao> tarefasProducaos = new LinkedHashSet<>();

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Utilizador getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(Utilizador utilizador) {
        this.utilizador = utilizador;
    }

    public String getNome(){
        return utilizador.getUsername();
    }

    public void setNome(String nome) {
        this.utilizador.setUsername(nome);
    }

    public int getTipoUtilizadorId() {
        return utilizador.getTipoUtilizador().getId();
    }

    public TiposUtilizador getCargo(){
        return cargo;
    }

    public void setCargo(TiposUtilizador cargo) {
        this.cargo = cargo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Set<TarefasProducao> getTarefasProducaos() {
        return tarefasProducaos;
    }

    public void setTarefasProducaos(Set<TarefasProducao> tarefasProducaos) {
        this.tarefasProducaos = tarefasProducaos;
    }
}
