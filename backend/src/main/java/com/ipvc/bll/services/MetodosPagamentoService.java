package com.ipvc.bll.services;

import com.ipvc.bll.dto.MetodosPagamentoDTO.*;
import com.ipvc.bll.models.MetodosPagamento;
import com.ipvc.bll.repos.MetodosPagamentoRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MetodosPagamentoService {
    private final MetodosPagamentoRepo metodosPagamentoRepo;

    public MetodosPagamentoService(MetodosPagamentoRepo metodosPagamentoRepo) {
        this.metodosPagamentoRepo = metodosPagamentoRepo;
    }

    public List<MetodosPagamentoResponseDTO> getAllMetodosPagamento() {
        return metodosPagamentoRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<MetodosPagamentoResponseDTO> getMetodoPagamentoById(Integer id) {
        return metodosPagamentoRepo.findById(id).map(this::convertToDTO);
    }

    public MetodosPagamentoResponseDTO saveMetodoPagamento(MetodosPagamentoCreateDTO metodoDTO) {
        MetodosPagamento metodo = new MetodosPagamento();
        metodo.setNome(metodoDTO.nome());
        return convertToDTO(metodosPagamentoRepo.save(metodo));
    }

    public Optional<MetodosPagamentoResponseDTO> updateMetodoPagamento(Integer id, MetodosPagamentoUpdateDTO metodoDTO) {
        return metodosPagamentoRepo.findById(id).map(metodo -> {
            metodo.setNome(metodoDTO.nome());
            return convertToDTO(metodosPagamentoRepo.save(metodo));
        });
    }

    public void deleteMetodoPagamento(Integer id) {
        metodosPagamentoRepo.deleteById(id);
    }

    private MetodosPagamentoResponseDTO convertToDTO(MetodosPagamento metodo) {
        return new MetodosPagamentoResponseDTO(
                metodo.getId(),
                metodo.getNome()
        );
    }
}
