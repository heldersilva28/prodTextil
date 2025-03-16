package com.ipvc.prodtextil.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "utilizadores", schema = "public")
public class Utilizador {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "utilizadores_id_gen")
    @SequenceGenerator(name = "utilizadores_id_gen", sequenceName = "utilizadores_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @Size(max = 50)
    @NotNull
    @Column(name = "tipo_utilizador", nullable = false, length = 50)
    private String tipoUtilizador;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public String getTipoUtilizador() {
        return tipoUtilizador;
    }

    public void setTipoUtilizador(String tipoUtilizador) {
        this.tipoUtilizador = tipoUtilizador;
    }

}