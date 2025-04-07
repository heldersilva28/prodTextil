package com.ipvc.bll.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "consumo_materiais", schema = "public")
public class ConsumoMateriais {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consumo_materiais_id_gen")
    @SequenceGenerator(name = "consumo_materiais_id_gen", sequenceName = "consumo_materiais_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "material_id", nullable = false)
    private Materiais material;

    @NotNull
    @Column(name = "quantidade_consumida", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeConsumida;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_registo")
    private Instant dataRegisto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Materiais getMaterial() {
        return material;
    }

    public void setMaterial(Materiais material) {
        this.material = material;
    }

    public BigDecimal getQuantidadeConsumida() {
        return quantidadeConsumida;
    }

    public void setQuantidadeConsumida(BigDecimal quantidadeConsumida) {
        this.quantidadeConsumida = quantidadeConsumida;
    }

    public Instant getDataRegisto() {
        return dataRegisto;
    }

    public void setDataRegisto(Instant dataRegisto) {
        this.dataRegisto = dataRegisto;
    }

}