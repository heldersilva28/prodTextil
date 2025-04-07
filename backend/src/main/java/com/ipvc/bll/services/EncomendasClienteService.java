package com.ipvc.bll.services;

import com.ipvc.bll.dto.EncomendasClienteDTO.*;
import com.ipvc.bll.models.EncomendasCliente;
import com.ipvc.bll.models.Cliente;
import com.ipvc.bll.models.EstadosEncomenda;
import com.ipvc.bll.repos.EncomendaClienteRepo;
import com.ipvc.bll.repos.ClienteRepo;
import com.ipvc.bll.repos.EstadosEncomendaRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EncomendasClienteService {
    private final EncomendaClienteRepo encomendasClienteRepo;
    private final ClienteRepo clienteRepo;
    private final EstadosEncomendaRepo estadosEncomendaRepo;

    public EncomendasClienteService(EncomendaClienteRepo encomendasClienteRepo, ClienteRepo clienteRepo, EstadosEncomendaRepo estadosEncomendaRepo) {
        this.encomendasClienteRepo = encomendasClienteRepo;
        this.clienteRepo = clienteRepo;
        this.estadosEncomendaRepo = estadosEncomendaRepo;
    }

    public List<EncomendaClienteResponseDTO> getAllEncomendas() {
        return encomendasClienteRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<EncomendaClienteResponseDTO> getEncomendaById(Integer id) {
        return encomendasClienteRepo.findById(id).map(this::convertToDTO);
    }

    public EncomendaClienteResponseDTO saveEncomenda(EncomendaClienteCreateDTO encomendaDTO) {
        Cliente cliente = clienteRepo.findById(encomendaDTO.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        EstadosEncomenda estado = estadosEncomendaRepo.findById(encomendaDTO.estadoId())
                .orElseThrow(() -> new RuntimeException("Estado de encomenda não encontrado"));

        EncomendasCliente encomenda = new EncomendasCliente();
        encomenda.setCliente(cliente);
        encomenda.setDataEncomenda(encomendaDTO.dataEncomenda());
        encomenda.setEstado(estado);
        encomenda.setValorTotal(encomendaDTO.valorTotal());

        return convertToDTO(encomendasClienteRepo.save(encomenda));
    }

    public Optional<EncomendaClienteResponseDTO> updateEncomenda(Integer id, EncomendaClienteUpdateDTO encomendaDTO) {
        return encomendasClienteRepo.findById(id).map(encomenda -> {
            EstadosEncomenda estado = estadosEncomendaRepo.findById(encomendaDTO.estadoId())
                    .orElseThrow(() -> new RuntimeException("Estado de encomenda não encontrado"));

            encomenda.setDataEncomenda(encomendaDTO.dataEncomenda());
            encomenda.setEstado(estado);
            encomenda.setValorTotal(encomendaDTO.valorTotal());

            return convertToDTO(encomendasClienteRepo.save(encomenda));
        });
    }

    public void deleteEncomenda(Integer id) {
        encomendasClienteRepo.deleteById(id);
    }

    private EncomendaClienteResponseDTO convertToDTO(EncomendasCliente encomenda) {
        return new EncomendaClienteResponseDTO(
                encomenda.getId(),
                encomenda.getCliente().getId(),
                encomenda.getDataEncomenda(),
                encomenda.getEstado().getId(),
                encomenda.getValorTotal()
        );
    }
}
