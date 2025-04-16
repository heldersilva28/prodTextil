package com.ipvc.bll.dto;

import java.math.BigDecimal;

public record DashboardStatsDTO(
        long totalUtilizadores,
        long totalEncomendasClientes,
        long totalEncomendasFornecedores,
        long totalMateriais,
        long totalFuncionarios,
        BigDecimal rendimentos,
        BigDecimal despesas
) {}
