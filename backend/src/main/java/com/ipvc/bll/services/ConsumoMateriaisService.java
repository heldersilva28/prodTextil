package com.ipvc.bll.services;

import com.ipvc.bll.dto.ConsumoMateriaisDTO.*;
import com.ipvc.bll.models.ConsumoMateriais;
import com.ipvc.bll.models.Materiais;
import com.ipvc.bll.repos.ConsumoMateriaisRepo;
import com.ipvc.bll.repos.MateriaisRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsumoMateriaisService {
    private final ConsumoMateriaisRepo consumoMateriaisRepo;
    private final MateriaisRepo materiaisRepo;

    public ConsumoMateriaisService(ConsumoMateriaisRepo consumoMateriaisRepo, MateriaisRepo materiaisRepo) {
        this.consumoMateriaisRepo = consumoMateriaisRepo;
        this.materiaisRepo = materiaisRepo;
    }

    public List<ConsumoMateriaisResponseDTO> getAllConsumos() {
        return consumoMateriaisRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<ConsumoMateriaisResponseDTO> getConsumoById(Integer id) {
        return consumoMateriaisRepo.findById(id).map(this::convertToDTO);
    }

    public ConsumoMateriaisResponseDTO saveConsumo(ConsumoMateriaisCreateDTO consumoDTO) {
        Materiais material = materiaisRepo.findById(consumoDTO.materialId())
                .orElseThrow(() -> new RuntimeException("Material não encontrado"));

        ConsumoMateriais consumo = new ConsumoMateriais();
        consumo.setMaterial(material);
        consumo.setQuantidadeConsumida(consumoDTO.quantidadeConsumida());
        consumo.setDataRegisto(Instant.now());

        return convertToDTO(consumoMateriaisRepo.save(consumo));
    }

    public Optional<ConsumoMateriaisResponseDTO> updateConsumo(Integer id, ConsumoMateriaisUpdateDTO consumoDTO) {
        return consumoMateriaisRepo.findById(id).map(consumo -> {
            consumo.setQuantidadeConsumida(consumoDTO.quantidadeConsumida());
            return convertToDTO(consumoMateriaisRepo.save(consumo));
        });
    }

    public void deleteConsumo(Integer id) {
        consumoMateriaisRepo.deleteById(id);
    }

    private ConsumoMateriaisResponseDTO convertToDTO(ConsumoMateriais consumo) {
        return new ConsumoMateriaisResponseDTO(
                consumo.getId(),
                consumo.getMaterial().getId(),
                consumo.getQuantidadeConsumida(),
                consumo.getDataRegisto()
        );
    }
}