package com.ipvc.bll.dto;

import java.util.Map;

public class EncomendasClientesStatsDTO {

    private long total;
    private long pendentes;
    private long concluidas;
    private Map<String, Integer> porMes;

    // Getters e Setters
    public long getConcluidas() {
        return concluidas;
    }

    public void setConcluidas(long concluidas) {
        this.concluidas = concluidas;
    }

    public long getPendentes() {
        return pendentes;
    }

    public void setPendentes(long pendentes) {
        this.pendentes = pendentes;
    }
    public long getTotal() {
        return total;
    }
    public void setTotal(long total) {
        this.total = total;
    }

    // Getter e Setter para 'porMes'
    public Map<String, Integer> getPorMes() {
        return porMes;
    }

    public void setPorMes(Map<String, Integer> porMes) {
        this.porMes = porMes;
    }
}
