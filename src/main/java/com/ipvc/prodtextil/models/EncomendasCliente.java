package com.ipvc.prodtextil.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "encomendas_clientes", schema = "public")
public class EncomendasCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "encomendas_clientes_id_gen")
    @SequenceGenerator(name = "encomendas_clientes_id_gen", sequenceName = "encomendas_clientes_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull
    @Column(name = "data_encomenda", nullable = false)
    private LocalDate dataEncomenda;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estado_id", nullable = false)
    private com.ipvc.prodtextil.models.EstadosEncomenda estado;

    @NotNull
    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @OneToMany(mappedBy = "encomenda")
    private Set<com.ipvc.prodtextil.models.ItensEncomendaCliente> itensEncomendaClientes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "encomenda")
    private Set<com.ipvc.prodtextil.models.RecebimentosCliente> recebimentosClientes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "encomenda")
    private Set<com.ipvc.prodtextil.models.TarefasProducao> tarefasProducaos = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getDataEncomenda() {
        return dataEncomenda;
    }

    public void setDataEncomenda(LocalDate dataEncomenda) {
        this.dataEncomenda = dataEncomenda;
    }

    public com.ipvc.prodtextil.models.EstadosEncomenda getEstado() {
        return estado;
    }

    public void setEstado(com.ipvc.prodtextil.models.EstadosEncomenda estado) {
        this.estado = estado;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Set<com.ipvc.prodtextil.models.ItensEncomendaCliente> getItensEncomendaClientes() {
        return itensEncomendaClientes;
    }

    public void setItensEncomendaClientes(Set<com.ipvc.prodtextil.models.ItensEncomendaCliente> itensEncomendaClientes) {
        this.itensEncomendaClientes = itensEncomendaClientes;
    }

    public Set<com.ipvc.prodtextil.models.RecebimentosCliente> getRecebimentosClientes() {
        return recebimentosClientes;
    }

    public void setRecebimentosClientes(Set<com.ipvc.prodtextil.models.RecebimentosCliente> recebimentosClientes) {
        this.recebimentosClientes = recebimentosClientes;
    }

    public Set<com.ipvc.prodtextil.models.TarefasProducao> getTarefasProducaos() {
        return tarefasProducaos;
    }

    public void setTarefasProducaos(Set<com.ipvc.prodtextil.models.TarefasProducao> tarefasProducaos) {
        this.tarefasProducaos = tarefasProducaos;
    }

}