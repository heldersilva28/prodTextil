package com.ipvc.prodtextil.models;

import java.io.Serializable;
import java.util.Objects;

public class ItensEncomendaClientePK implements Serializable {
    private Integer encomenda;
    private String produto;

    public ItensEncomendaClientePK() {}

    public ItensEncomendaClientePK(Integer encomenda, String produto) {
        this.encomenda = encomenda;
        this.produto = produto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItensEncomendaClientePK that = (ItensEncomendaClientePK) o;
        return Objects.equals(encomenda, that.encomenda) && Objects.equals(produto, that.produto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(encomenda, produto);
    }
}
