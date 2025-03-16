package com.ipvc.prodtextil.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "funcionarios", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "funcionarios_email_key", columnNames = {"email"})
})
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "funcionarios_id_gen")
    @SequenceGenerator(name = "funcionarios_id_gen", sequenceName = "funcionarios_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 20)
    @Column(name = "telefone", length = 20)
    private String telefone;

    @Size(max = 100)
    @NotNull
    @Column(name = "cargo", nullable = false, length = 100)
    private String cargo;

    @NotNull
    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    @OneToMany(mappedBy = "funcionario")
    private Set<com.ipvc.prodtextil.models.TarefasProducao> tarefasProducaos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "funcionario")
    private Set<Utilizador> utilizadors = new LinkedHashSet<>();

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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Set<com.ipvc.prodtextil.models.TarefasProducao> getTarefasProducaos() {
        return tarefasProducaos;
    }

    public void setTarefasProducaos(Set<com.ipvc.prodtextil.models.TarefasProducao> tarefasProducaos) {
        this.tarefasProducaos = tarefasProducaos;
    }

    public Set<Utilizador> getUtilizadores() {
        return utilizadors;
    }

    public void setUtilizadores(Set<Utilizador> utilizadors) {
        this.utilizadors = utilizadors;
    }

}