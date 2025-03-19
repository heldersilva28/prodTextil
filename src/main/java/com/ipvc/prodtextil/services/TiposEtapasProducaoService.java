package com.ipvc.prodtextil.services;

import com.ipvc.prodtextil.dto.TiposEtapasProducaoDTO.*;
import com.ipvc.prodtextil.models.TiposEtapasProducao;
import com.ipvc.prodtextil.repos.TiposEtapasProducaoRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TiposEtapasProducaoService {
    private final TiposEtapasProducaoRepo tiposEtapasProducaoRepo;

    public TiposEtapasProducaoService(TiposEtapasProducaoRepo tiposEtapasProducaoRepo) {
        this.tiposEtapasProducaoRepo = tiposEtapasProducaoRepo;
    }

    public List<TiposEtapasProducaoResponseDTO> getAllTiposEtapas() {
        return tiposEtapasProducaoRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<TiposEtapasProducaoResponseDTO> getTipoEtapaById(Integer id) {
        return tiposEtapasProducaoRepo.findById(id).map(this::convertToDTO);
    }

    public TiposEtapasProducaoResponseDTO saveTipoEtapa(TiposEtapasProducaoCreateDTO tipoEtapaDTO) {
        TiposEtapasProducao tipoEtapa = new TiposEtapasProducao();
        tipoEtapa.setDescricao(tipoEtapaDTO.descricao());
        return convertToDTO(tiposEtapasProducaoRepo.save(tipoEtapa));
    }

    public Optional<TiposEtapasProducaoResponseDTO> updateTipoEtapa(Integer id, TiposEtapasProducaoUpdateDTO tipoEtapaDTO) {
        return tiposEtapasProducaoRepo.findById(id).map(tipoEtapa -> {
            tipoEtapa.setDescricao(tipoEtapaDTO.descricao());
            return convertToDTO(tiposEtapasProducaoRepo.save(tipoEtapa));
        });
    }

    public void deleteTipoEtapa(Integer id) {
        tiposEtapasProducaoRepo.deleteById(id);
    }

    private TiposEtapasProducaoResponseDTO convertToDTO(TiposEtapasProducao tipoEtapa) {
        return new TiposEtapasProducaoResponseDTO(
                tipoEtapa.getId(),
                tipoEtapa.getDescricao()
        );
    }
}