package com.ipvc.bll.services;

import com.ipvc.bll.dto.TiposEventoDTO.*;
import com.ipvc.bll.models.TiposEvento;
import com.ipvc.bll.repos.TiposEventosRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TiposEventoService {
    private final TiposEventosRepo tiposEventoRepo;

    public TiposEventoService(TiposEventosRepo tiposEventoRepo) {
        this.tiposEventoRepo = tiposEventoRepo;
    }

    public List<TiposEventoResponseDTO> getAllTiposEventos() {
        return tiposEventoRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<TiposEventoResponseDTO> getTipoEventoById(Integer id) {
        return tiposEventoRepo.findById(id).map(this::convertToDTO);
    }

    public TiposEventoResponseDTO saveTipoEvento(TiposEventoCreateDTO tipoEventoDTO) {
        TiposEvento tipoEvento = new TiposEvento();
        tipoEvento.setNome(tipoEventoDTO.nome());
        return convertToDTO(tiposEventoRepo.save(tipoEvento));
    }

    public Optional<TiposEventoResponseDTO> updateTipoEvento(Integer id, TiposEventoUpdateDTO tipoEventoDTO) {
        return tiposEventoRepo.findById(id).map(tipoEvento -> {
            tipoEvento.setNome(tipoEventoDTO.nome());
            return convertToDTO(tiposEventoRepo.save(tipoEvento));
        });
    }

    public void deleteTipoEvento(Integer id) {
        tiposEventoRepo.deleteById(id);
    }

    private TiposEventoResponseDTO convertToDTO(TiposEvento tipoEvento) {
        return new TiposEventoResponseDTO(
                tipoEvento.getId(),
                tipoEvento.getNome()
        );
    }
}