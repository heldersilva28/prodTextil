package com.ipvc.bll.services;

import com.ipvc.bll.dto.MateriaisDTO.*;
import com.ipvc.bll.models.Materiais;
import com.ipvc.bll.models.TiposMateriais;
import com.ipvc.bll.repos.MateriaisRepo;
import com.ipvc.bll.repos.TiposMateriaisRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MateriaisService {
    private final MateriaisRepo materiaisRepo;
    private final TiposMateriaisRepo tiposMateriaisRepo;

    public MateriaisService(MateriaisRepo materiaisRepo, TiposMateriaisRepo tiposMateriaisRepo) {
        this.materiaisRepo = materiaisRepo;
        this.tiposMateriaisRepo = tiposMateriaisRepo;
    }

    public List<MateriaisResponseDTO> getAllMateriais() {
        return materiaisRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<MateriaisResponseDTO> getMaterialById(Integer id) {
        return materiaisRepo.findById(id).map(this::convertToDTO);
    }

    public MateriaisResponseDTO saveMaterial(MateriaisCreateDTO materialDTO) {
        TiposMateriais tipo = tiposMateriaisRepo.findById(materialDTO.tipoId())
                .orElseThrow(() -> new RuntimeException("Tipo de material não encontrado"));

        Materiais material = new Materiais();
        material.setNome(materialDTO.nome());
        material.setTipo(tipo);
        material.setPrecoUnidade(materialDTO.precoUnidade());
        material.setStockDisponivel(materialDTO.stockDisponivel());

        return convertToDTO(materiaisRepo.save(material));
    }

    public Optional<MateriaisResponseDTO> updateMaterial(Integer id, MateriaisUpdateDTO materialDTO) {
        return materiaisRepo.findById(id).map(material -> {
            TiposMateriais tipo = tiposMateriaisRepo.findById(materialDTO.tipoId())
                    .orElseThrow(() -> new RuntimeException("Tipo de material não encontrado"));

            material.setNome(materialDTO.nome());
            material.setTipo(tipo);
            material.setPrecoUnidade(materialDTO.precoUnidade());
            material.setStockDisponivel(materialDTO.stockDisponivel());

            return convertToDTO(materiaisRepo.save(material));
        });
    }

    public void deleteMaterial(Integer id) {
        materiaisRepo.deleteById(id);
    }

    private MateriaisResponseDTO convertToDTO(Materiais material) {
        return new MateriaisResponseDTO(
                material.getId(),
                material.getNome(),
                material.getTipo().getId(),
                material.getPrecoUnidade(),
                material.getStockDisponivel()
        );
    }
}
