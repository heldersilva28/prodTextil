package com.ipvc.bll.services;

import com.ipvc.bll.dto.ItensEncomendaClienteDTO.*;
import com.ipvc.bll.models.ItensEncomendaCliente;
import com.ipvc.bll.models.EncomendasCliente;
import com.ipvc.bll.repos.ItemEncomendaClienteRepo;
import com.ipvc.bll.repos.EncomendaClienteRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItensEncomendaClienteService {
    private final ItemEncomendaClienteRepo itensEncomendaClienteRepo;
    private final EncomendaClienteRepo encomendasClienteRepo;

    public ItensEncomendaClienteService(ItemEncomendaClienteRepo itensEncomendaClienteRepo, EncomendaClienteRepo encomendasClienteRepo) {
        this.itensEncomendaClienteRepo = itensEncomendaClienteRepo;
        this.encomendasClienteRepo = encomendasClienteRepo;
    }

    public List<ItensEncomendaClienteResponseDTO> getAllItens() {
        return itensEncomendaClienteRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<ItensEncomendaClienteResponseDTO> getItemById(Integer encomendaId, String produto) {
        return itensEncomendaClienteRepo.findByEncomendaIdAndProduto(encomendaId, produto).map(this::convertToDTO);
    }

    public ItensEncomendaClienteResponseDTO saveItem(ItensEncomendaClienteCreateDTO itemDTO) {
        EncomendasCliente encomenda = encomendasClienteRepo.findById(itemDTO.encomendaId())
                .orElseThrow(() -> new RuntimeException("Encomenda não encontrada"));

        ItensEncomendaCliente item = new ItensEncomendaCliente();
        item.setEncomenda(encomenda);
        item.setProduto(itemDTO.produto());
        item.setQuantidade(itemDTO.quantidade());
        item.setPrecoUnitario(itemDTO.precoUnitario());

        return convertToDTO(itensEncomendaClienteRepo.save(item));
    }

    public Optional<ItensEncomendaClienteResponseDTO> updateItem(Integer encomendaId, String produto, ItensEncomendaClienteUpdateDTO itemDTO) {
        return itensEncomendaClienteRepo.findByEncomendaIdAndProduto(encomendaId, produto).map(item -> {
            item.setQuantidade(itemDTO.quantidade());
            item.setPrecoUnitario(itemDTO.precoUnitario());
            return convertToDTO(itensEncomendaClienteRepo.save(item));
        });
    }

    public void deleteItem(Integer encomendaId, String produto) {
        itensEncomendaClienteRepo.deleteByEncomendaIdAndProduto(encomendaId, produto);
    }

    private ItensEncomendaClienteResponseDTO convertToDTO(ItensEncomendaCliente item) {
        return new ItensEncomendaClienteResponseDTO(
                item.getEncomenda().getId(),
                item.getProduto(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getQuantidade() != null && item.getPrecoUnitario() != null
                        ? item.getQuantidade() * (item.getPrecoUnitario()).doubleValue()
                        : 0.0
        );
    }
}
