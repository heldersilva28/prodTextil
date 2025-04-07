package com.ipvc.bll.services;

import com.ipvc.bll.dto.ItensEncomendaFornecedorDTO.*;
import com.ipvc.bll.models.ItensEncomendaFornecedor;
import com.ipvc.bll.models.EncomendasFornecedor;
import com.ipvc.bll.models.Materiais;
import com.ipvc.bll.repos.ItemEncomendaFornecedorRepo;
import com.ipvc.bll.repos.EncomendaFornecedorRepo;
import com.ipvc.bll.repos.MateriaisRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItensEncomendaFornecedorService {
    private final ItemEncomendaFornecedorRepo itensEncomendaFornecedorRepo;
    private final EncomendaFornecedorRepo encomendasFornecedorRepo;
    private final MateriaisRepo materiaisRepo;

    public ItensEncomendaFornecedorService(ItemEncomendaFornecedorRepo itensEncomendaFornecedorRepo, EncomendaFornecedorRepo encomendasFornecedorRepo, MateriaisRepo materiaisRepo) {
        this.itensEncomendaFornecedorRepo = itensEncomendaFornecedorRepo;
        this.encomendasFornecedorRepo = encomendasFornecedorRepo;
        this.materiaisRepo = materiaisRepo;
    }

    public List<ItensEncomendaFornecedorResponseDTO> getAllItens() {
        return itensEncomendaFornecedorRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<ItensEncomendaFornecedorResponseDTO> getItemById(Integer encomendaId, Integer materialId) {
        return itensEncomendaFornecedorRepo.findByEncomendaIdAndMaterialId(encomendaId, materialId).map(this::convertToDTO);
    }

    public ItensEncomendaFornecedorResponseDTO saveItem(ItensEncomendaFornecedorCreateDTO itemDTO) {
        EncomendasFornecedor encomenda = encomendasFornecedorRepo.findById(itemDTO.encomendaId())
                .orElseThrow(() -> new RuntimeException("Encomenda de fornecedor não encontrada"));

        Materiais material = materiaisRepo.findById(itemDTO.materialId())
                .orElseThrow(() -> new RuntimeException("Material não encontrado"));

        ItensEncomendaFornecedor item = new ItensEncomendaFornecedor();
        item.setEncomenda(encomenda);
        item.setMaterial(material);
        item.setQuantidade(itemDTO.quantidade());
        item.setPrecoUnitario(itemDTO.precoUnitario());

        return convertToDTO(itensEncomendaFornecedorRepo.save(item));
    }

    public Optional<ItensEncomendaFornecedorResponseDTO> updateItem(Integer encomendaId, Integer materialId, ItensEncomendaFornecedorUpdateDTO itemDTO) {
        return itensEncomendaFornecedorRepo.findByEncomendaIdAndMaterialId(encomendaId, materialId).map(item -> {
            item.setQuantidade(itemDTO.quantidade());
            item.setPrecoUnitario(itemDTO.precoUnitario());
            return convertToDTO(itensEncomendaFornecedorRepo.save(item));
        });
    }

    public void deleteItem(Integer encomendaId, Integer materialId) {
        itensEncomendaFornecedorRepo.deleteByEncomendaIdAndMaterialId(encomendaId, materialId);
    }

    private ItensEncomendaFornecedorResponseDTO convertToDTO(ItensEncomendaFornecedor item) {
        return new ItensEncomendaFornecedorResponseDTO(
                item.getEncomenda().getId(),
                item.getMaterial().getId(),
                item.getQuantidade(),
                item.getPrecoUnitario()
        );
    }
}
