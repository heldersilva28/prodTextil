package com.ipvc.bll.services;

import com.ipvc.bll.dto.DashboardStatsDTO;
import com.ipvc.bll.models.PagamentosFornecedor;
import com.ipvc.bll.models.RecebimentosCliente;
import com.ipvc.bll.repos.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DashboardService {

    private final UtilizadorRepo utilizadorRepo;
    private final EncomendaClienteRepo encomendaClienteRepo;
    private final EncomendaFornecedorRepo encomendaFornecedorRepo;
    private final MateriaisRepo materiaisRepo;
    private final FuncionarioRepo funcionarioRepo;
    private final RecebimentosClientesRepo recebimentosRepo;
    private final PagamentosFornecedoresRepo pagamentosRepo;
    private final ClienteRepo clienteRepo;

    public DashboardService(
            UtilizadorRepo utilizadorRepo,
            EncomendaClienteRepo encomendaClienteRepo,
            EncomendaFornecedorRepo encomendaFornecedorRepo,
            MateriaisRepo materiaisRepo,
            FuncionarioRepo funcionarioRepo,
            RecebimentosClientesRepo recebimentosRepo,
            PagamentosFornecedoresRepo pagamentosRepo,
            ClienteRepo clienteRepo
    ) {
        this.utilizadorRepo = utilizadorRepo;
        this.encomendaClienteRepo = encomendaClienteRepo;
        this.encomendaFornecedorRepo = encomendaFornecedorRepo;
        this.materiaisRepo = materiaisRepo;
        this.funcionarioRepo = funcionarioRepo;
        this.recebimentosRepo = recebimentosRepo;
        this.pagamentosRepo = pagamentosRepo;
        this.clienteRepo = clienteRepo;
    }

    public DashboardStatsDTO getStats() {
        long totalUtilizadores = utilizadorRepo.count();
        long totalEncomendasClientes = encomendaClienteRepo.count();
        long totalEncomendasFornecedores = encomendaFornecedorRepo.count();
        long totalMateriais = materiaisRepo.count();
        long totalFuncionarios = funcionarioRepo.count();
        long totalClientes = clienteRepo.count();

        BigDecimal rendimentos = recebimentosRepo.findAll().stream()
                .map(RecebimentosCliente::getValorRecebido)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal despesas = pagamentosRepo.findAll().stream()
                .map(PagamentosFornecedor::getValorPago)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardStatsDTO(
                totalUtilizadores,
                totalEncomendasClientes,
                totalEncomendasFornecedores,
                totalMateriais,
                totalFuncionarios,
                totalClientes,
                rendimentos,
                despesas
        );
    }
}
