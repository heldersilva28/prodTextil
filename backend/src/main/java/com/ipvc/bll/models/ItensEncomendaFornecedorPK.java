package com.ipvc.bll.models;

import java.io.Serializable;
import java.util.Objects;

public class ItensEncomendaFornecedorPK implements Serializable {
    private Integer encomenda;
    private Integer material;

    public ItensEncomendaFornecedorPK() {}

    public ItensEncomendaFornecedorPK(Integer encomenda, Integer material) {
        this.encomenda = encomenda;
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItensEncomendaFornecedorPK that = (ItensEncomendaFornecedorPK) o;
        return Objects.equals(encomenda, that.encomenda) && Objects.equals(material, that.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(encomenda, material);
    }
}
