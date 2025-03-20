package com.ipvc.prodtextil.services;

import com.ipvc.prodtextil.dto.TiposMateriaisDTO.*;
import com.ipvc.prodtextil.models.TiposMateriais;
import com.ipvc.prodtextil.repos.TiposMateriaisRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TiposMateriaisService {
    private final TiposMateriaisRepo tiposMateriaisRepo;

    public TiposMateriaisService(TiposMateriaisRepo tiposMateriaisRepo) {
        this.tiposMateriaisRepo = tiposMateriaisRepo;
    }

    public List<TiposMateriaisResponseDTO> getAllTiposMateriais() {
        return tiposMateriaisRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<TiposMateriaisResponseDTO> getTipoMaterialById(Integer id) {
        return tiposMateriaisRepo.findById(id).map(this::convertToDTO);
    }

    public TiposMateriaisResponseDTO saveTipoMaterial(TiposMateriaisCreateDTO tipoMaterialDTO) {
        TiposMateriais tipoMaterial = new TiposMateriais();
        tipoMaterial.setNome(tipoMaterialDTO.nome());
        return convertToDTO(tiposMateriaisRepo.save(tipoMaterial));
    }

    public Optional<TiposMateriaisResponseDTO> updateTipoMaterial(Integer id, TiposMateriaisUpdateDTO tipoMaterialDTO) {
        return tiposMateriaisRepo.findById(id).map(tipoMaterial -> {
            tipoMaterial.setNome(tipoMaterialDTO.nome());
            return convertToDTO(tiposMateriaisRepo.save(tipoMaterial));
        });
    }

    public void deleteTipoMaterial(Integer id) {
        tiposMateriaisRepo.deleteById(id);
    }

    private TiposMateriaisResponseDTO convertToDTO(TiposMateriais tipoMaterial) {
        return new TiposMateriaisResponseDTO(
                tipoMaterial.getId(),
                tipoMaterial.getNome()
        );
    }
}