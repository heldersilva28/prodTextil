package com.ipvc.bll.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "utilizadores", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "utilizadores_username_key", columnNames = {"username"}),
        @UniqueConstraint(name = "utilizadores_email_key", columnNames = {"email"})
})
public class Utilizador {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "utilizadores_id_gen")
    @SequenceGenerator(name = "utilizadores_id_gen", sequenceName = "utilizadores_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "hash", nullable = false)
    private String hash;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_utilizador_id", nullable = false,unique = false)
    private TiposUtilizador tipoUtilizador;

    @OneToMany(mappedBy = "utilizador")
    private Set<Funcionario> funcionarios = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public TiposUtilizador getTipoUtilizador() {
        return tipoUtilizador;
    }

    public void setTipoUtilizador(TiposUtilizador tipoUtilizador) {
        this.tipoUtilizador = tipoUtilizador;
    }

    public Set<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(Set<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

}