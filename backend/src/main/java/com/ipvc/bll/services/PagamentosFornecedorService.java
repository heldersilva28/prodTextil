package com.ipvc.bll.services;

import com.ipvc.bll.dto.PagamentosFornecedorDTO.*;
import com.ipvc.bll.models.PagamentosFornecedor;
import com.ipvc.bll.models.EncomendasFornecedor;
import com.ipvc.bll.models.MetodosPagamento;
import com.ipvc.bll.repos.PagamentosFornecedoresRepo;
import com.ipvc.bll.repos.EncomendaFornecedorRepo;
import com.ipvc.bll.repos.MetodosPagamentoRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PagamentosFornecedorService {
    private final PagamentosFornecedoresRepo pagamentosFornecedorRepo;
    private final EncomendaFornecedorRepo encomendasFornecedorRepo;
    private final MetodosPagamentoRepo metodosPagamentoRepo;

    public PagamentosFornecedorService(PagamentosFornecedoresRepo pagamentosFornecedorRepo, EncomendaFornecedorRepo encomendasFornecedorRepo, MetodosPagamentoRepo metodosPagamentoRepo) {
        this.pagamentosFornecedorRepo = pagamentosFornecedorRepo;
        this.encomendasFornecedorRepo = encomendasFornecedorRepo;
        this.metodosPagamentoRepo = metodosPagamentoRepo;
    }

    public List<PagamentosFornecedorResponseDTO> getAllPagamentos() {
        return pagamentosFornecedorRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<PagamentosFornecedorResponseDTO> getPagamentoById(Integer id) {
        return pagamentosFornecedorRepo.findById(id).map(this::convertToDTO);
    }

    public PagamentosFornecedorResponseDTO savePagamento(PagamentosFornecedorCreateDTO pagamentoDTO) {
        EncomendasFornecedor encomenda = encomendasFornecedorRepo.findById(pagamentoDTO.encomendaId())
                .orElseThrow(() -> new RuntimeException("Encomenda de fornecedor não encontrada"));

        MetodosPagamento metodoPagamento = metodosPagamentoRepo.findById(pagamentoDTO.metodoPagamentoId())
                .orElseThrow(() -> new RuntimeException("Método de pagamento não encontrado"));

        PagamentosFornecedor pagamento = new PagamentosFornecedor();
        pagamento.setEncomenda(encomenda);
        pagamento.setValorPago(pagamentoDTO.valorPago());
        pagamento.setDataPagamento(pagamentoDTO.dataPagamento());
        pagamento.setMetodoPagamento(metodoPagamento);

        return convertToDTO(pagamentosFornecedorRepo.save(pagamento));
    }

    public Optional<PagamentosFornecedorResponseDTO> updatePagamento(Integer id, PagamentosFornecedorUpdateDTO pagamentoDTO) {
        return pagamentosFornecedorRepo.findById(id).map(pagamento -> {
            MetodosPagamento metodoPagamento = metodosPagamentoRepo.findById(pagamentoDTO.metodoPagamentoId())
                    .orElseThrow(() -> new RuntimeException("Método de pagamento não encontrado"));

            pagamento.setValorPago(pagamentoDTO.valorPago());
            pagamento.setDataPagamento(pagamentoDTO.dataPagamento());
            pagamento.setMetodoPagamento(metodoPagamento);

            return convertToDTO(pagamentosFornecedorRepo.save(pagamento));
        });
    }

    public void deletePagamento(Integer id) {
        pagamentosFornecedorRepo.deleteById(id);
    }

    private PagamentosFornecedorResponseDTO convertToDTO(PagamentosFornecedor pagamento) {
        return new PagamentosFornecedorResponseDTO(
                pagamento.getId(),
                pagamento.getEncomenda().getId(),
                pagamento.getValorPago(),
                pagamento.getDataPagamento(),
                pagamento.getMetodoPagamento().getId()
        );
    }
}