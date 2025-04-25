package com.ipvc.bll.dto;

import java.util.Map;

public class ClientesStatsDTO {
    private long total;
    private long ativos;
    private long novosEsteMes;
    private Map<String, Integer> novosPorMes;
    private Map<String, Integer> encomendasPorCliente;

    // Getters e Setters
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getAtivos() {
        return ativos;
    }

    public void setAtivos(long ativos) {
        this.ativos = ativos;
    }

    public long getNovosEsteMes() {
        return novosEsteMes;
    }

    public void setNovosEsteMes(long novosEsteMes) {
        this.novosEsteMes = novosEsteMes;
    }

    public Map<String, Integer> getNovosPorMes() {
        return novosPorMes;
    }

    public void setNovosPorMes(Map<String, Integer> novosPorMes) {
        this.novosPorMes = novosPorMes;
    }

    public Map<String, Integer> getEncomendasPorCliente() {
        return encomendasPorCliente;
    }

    public void setEncomendasPorCliente(Map<String, Integer> encomendasPorCliente) {
        this.encomendasPorCliente = encomendasPorCliente;
    }
}
