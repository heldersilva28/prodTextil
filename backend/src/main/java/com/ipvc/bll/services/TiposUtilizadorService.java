package com.ipvc.bll.services;

import com.ipvc.bll.dto.TiposUtilizadorDTO.*;
import com.ipvc.bll.models.TiposUtilizador;
import com.ipvc.bll.repos.TipoUtilizadorRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TiposUtilizadorService {
    private final TipoUtilizadorRepo tiposUtilizadorRepo;

    public TiposUtilizadorService(TipoUtilizadorRepo tiposUtilizadorRepo) {
        this.tiposUtilizadorRepo = tiposUtilizadorRepo;
    }

    public List<TiposUtilizadorResponseDTO> getAllTiposUtilizadores() {
        return tiposUtilizadorRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<TiposUtilizadorResponseDTO> getTipoUtilizadorById(Integer id) {
        return tiposUtilizadorRepo.findById(id).map(this::convertToDTO);
    }

    public TiposUtilizadorResponseDTO saveTipoUtilizador(TiposUtilizadorCreateDTO tipoUtilizadorDTO) {
        TiposUtilizador tipoUtilizador = new TiposUtilizador();
        tipoUtilizador.setNome(tipoUtilizadorDTO.nome());
        return convertToDTO(tiposUtilizadorRepo.save(tipoUtilizador));
    }

    public Optional<TiposUtilizadorResponseDTO> updateTipoUtilizador(Integer id, TiposUtilizadorUpdateDTO tipoUtilizadorDTO) {
        return tiposUtilizadorRepo.findById(id).map(tipoUtilizador -> {
            tipoUtilizador.setNome(tipoUtilizadorDTO.nome());
            return convertToDTO(tiposUtilizadorRepo.save(tipoUtilizador));
        });
    }

    public void deleteTipoUtilizador(Integer id) {
        tiposUtilizadorRepo.deleteById(id);
    }

    private TiposUtilizadorResponseDTO convertToDTO(TiposUtilizador tipoUtilizador) {
        return new TiposUtilizadorResponseDTO(
                tipoUtilizador.getId(),
                tipoUtilizador.getNome()
        );
    }
}