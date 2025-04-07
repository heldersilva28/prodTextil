package com.ipvc.bll.services;

import com.ipvc.bll.dto.RecebimentosClienteDTO.*;
import com.ipvc.bll.models.RecebimentosCliente;
import com.ipvc.bll.models.EncomendasCliente;
import com.ipvc.bll.models.MetodosPagamento;
import com.ipvc.bll.repos.RecebimentosClientesRepo;
import com.ipvc.bll.repos.EncomendaClienteRepo;
import com.ipvc.bll.repos.MetodosPagamentoRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecebimentosClienteService {
    private final RecebimentosClientesRepo recebimentosClienteRepo;
    private final EncomendaClienteRepo encomendasClienteRepo;
    private final MetodosPagamentoRepo metodosPagamentoRepo;

    public RecebimentosClienteService(RecebimentosClientesRepo recebimentosClienteRepo, EncomendaClienteRepo encomendasClienteRepo, MetodosPagamentoRepo metodosPagamentoRepo) {
        this.recebimentosClienteRepo = recebimentosClienteRepo;
        this.encomendasClienteRepo = encomendasClienteRepo;
        this.metodosPagamentoRepo = metodosPagamentoRepo;
    }

    public List<RecebimentosClienteResponseDTO> getAllRecebimentos() {
        return recebimentosClienteRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<RecebimentosClienteResponseDTO> getRecebimentoById(Integer id) {
        return recebimentosClienteRepo.findById(id).map(this::convertToDTO);
    }

    public RecebimentosClienteResponseDTO saveRecebimento(RecebimentosClienteCreateDTO recebimentoDTO) {
        EncomendasCliente encomenda = encomendasClienteRepo.findById(recebimentoDTO.encomendaId())
                .orElseThrow(() -> new RuntimeException("Encomenda do cliente não encontrada"));

        MetodosPagamento metodoPagamento = metodosPagamentoRepo.findById(recebimentoDTO.metodoPagamentoId())
                .orElseThrow(() -> new RuntimeException("Método de pagamento não encontrado"));

        RecebimentosCliente recebimento = new RecebimentosCliente();
        recebimento.setEncomenda(encomenda);
        recebimento.setValorRecebido(recebimentoDTO.valorRecebido());
        recebimento.setDataRecebimento(recebimentoDTO.dataRecebimento());
        recebimento.setMetodoPagamento(metodoPagamento);

        return convertToDTO(recebimentosClienteRepo.save(recebimento));
    }

    public Optional<RecebimentosClienteResponseDTO> updateRecebimento(Integer id, RecebimentosClienteUpdateDTO recebimentoDTO) {
        return recebimentosClienteRepo.findById(id).map(recebimento -> {
            MetodosPagamento metodoPagamento = metodosPagamentoRepo.findById(recebimentoDTO.metodoPagamentoId())
                    .orElseThrow(() -> new RuntimeException("Método de pagamento não encontrado"));

            recebimento.setValorRecebido(recebimentoDTO.valorRecebido());
            recebimento.setDataRecebimento(recebimentoDTO.dataRecebimento());
            recebimento.setMetodoPagamento(metodoPagamento);

            return convertToDTO(recebimentosClienteRepo.save(recebimento));
        });
    }

    public void deleteRecebimento(Integer id) {
        recebimentosClienteRepo.deleteById(id);
    }

    private RecebimentosClienteResponseDTO convertToDTO(RecebimentosCliente recebimento) {
        return new RecebimentosClienteResponseDTO(
                recebimento.getId(),
                recebimento.getEncomenda().getId(),
                recebimento.getValorRecebido(),
                recebimento.getDataRecebimento(),
                recebimento.getMetodoPagamento().getId()
        );
    }
}