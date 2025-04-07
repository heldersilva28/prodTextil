package com.ipvc.bll.services;

import com.ipvc.bll.dto.EstadosEncomendaDTO.*;
import com.ipvc.bll.models.EstadosEncomenda;
import com.ipvc.bll.repos.EstadosEncomendaRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstadosEncomendaService {
    private final EstadosEncomendaRepo estadosEncomendaRepo;

    public EstadosEncomendaService(EstadosEncomendaRepo estadosEncomendaRepo) {
        this.estadosEncomendaRepo = estadosEncomendaRepo;
    }

    public List<EstadoEncomendaResponseDTO> getAllEstados() {
        return estadosEncomendaRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<EstadoEncomendaResponseDTO> getEstadoById(Integer id) {
        return estadosEncomendaRepo.findById(id).map(this::convertToDTO);
    }

    public EstadoEncomendaResponseDTO saveEstado(EstadoEncomendaCreateDTO estadoDTO) {
        EstadosEncomenda estado = new EstadosEncomenda();
        estado.setNome(estadoDTO.nome());

        return convertToDTO(estadosEncomendaRepo.save(estado));
    }

    public Optional<EstadoEncomendaResponseDTO> updateEstado(Integer id, EstadoEncomendaUpdateDTO estadoDTO) {
        return estadosEncomendaRepo.findById(id).map(estado -> {
            estado.setNome(estadoDTO.nome());
            return convertToDTO(estadosEncomendaRepo.save(estado));
        });
    }

    public void deleteEstado(Integer id) {
        estadosEncomendaRepo.deleteById(id);
    }

    private EstadoEncomendaResponseDTO convertToDTO(EstadosEncomenda estado) {
        return new EstadoEncomendaResponseDTO(
                estado.getId(),
                estado.getNome()
        );
    }
}
