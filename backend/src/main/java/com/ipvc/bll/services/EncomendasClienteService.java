package com.ipvc.bll.services;

import com.ipvc.bll.dto.EncomendasClienteDTO.*;
import com.ipvc.bll.dto.EncomendasClientesStatsDTO;
import com.ipvc.bll.models.EncomendasCliente;
import com.ipvc.bll.models.Cliente;
import com.ipvc.bll.models.EstadosEncomenda;
import com.ipvc.bll.repos.EncomendaClienteRepo;
import com.ipvc.bll.repos.ClienteRepo;
import com.ipvc.bll.repos.EstadosEncomendaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EncomendasClienteService {
    private final EncomendaClienteRepo encomendasClienteRepo;
    private final ClienteRepo clienteRepo;
    private final EstadosEncomendaRepo estadosEncomendaRepo;
    private final EncomendaClienteRepo encomendaClienteRepo;

    public EncomendasClienteService(EncomendaClienteRepo encomendasClienteRepo, ClienteRepo clienteRepo, EstadosEncomendaRepo estadosEncomendaRepo, EncomendaClienteRepo encomendaClienteRepo) {
        this.encomendasClienteRepo = encomendasClienteRepo;
        this.clienteRepo = clienteRepo;
        this.estadosEncomendaRepo = estadosEncomendaRepo;
        this.encomendaClienteRepo = encomendaClienteRepo;
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



    public EncomendasClientesStatsDTO obterEstatisticas() {
        EncomendasClientesStatsDTO dto = new EncomendasClientesStatsDTO();

        dto.setTotal(encomendasClienteRepo.count());
        dto.setPendentes(encomendasClienteRepo.contarPorEstado(1));  // estado_id = 1
        dto.setConcluidas(encomendasClienteRepo.contarPorEstado(2)); // estado_id = 2

        Map<String, Integer> porMes = new LinkedHashMap<>();

        for (Month mes : Month.values()) {
            String nomeMes = mes.getDisplayName(TextStyle.FULL, new Locale("pt", "PT"));
            porMes.put(nomeMes, 0);
        }

        List<Object[]> resultados = encomendasClienteRepo.contarPorMes();

        for (Object[] linha : resultados) {
            int mes = (int) linha[0];
            int quantidade = ((Number) linha[1]).intValue();
            String nomeMes = Month.of(mes).getDisplayName(TextStyle.FULL, new Locale("pt", "PT"));
            porMes.put(nomeMes, quantidade);
        }

        dto.setPorMes(porMes);
        return dto;
    }
}
