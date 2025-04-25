package com.ipvc.bll.dto;

import java.math.BigDecimal;
import java.util.Map;

public class MateriaisStatsDTO {

    private long total;
    private long baixoStock;
    private Map<String, Integer> porCategoria;
    private Map<String, BigDecimal> valorPorCategoria;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getBaixoStock() {
        return baixoStock;
    }

    public void setBaixoStock(long baixoStock) {
        this.baixoStock = baixoStock;
    }

    public Map<String, Integer> getPorCategoria() {
        return porCategoria;
    }

    public void setPorCategoria(Map<String, Integer> porCategoria) {
        this.porCategoria = porCategoria;
    }

    public Map<String, BigDecimal> getValorPorCategoria() {
        return valorPorCategoria;
    }
    public void setValorPorCategoria(Map<String, BigDecimal> valorPorCategoria) {
        this.valorPorCategoria = valorPorCategoria;
    }
}
