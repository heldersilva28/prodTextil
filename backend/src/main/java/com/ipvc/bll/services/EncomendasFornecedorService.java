package com.ipvc.bll.services;

import com.ipvc.bll.dto.EncomendasClientesStatsDTO;
import com.ipvc.bll.dto.EncomendasFornecedorDTO.*;
import com.ipvc.bll.dto.ItensEncomendaFornecedorDTO;
import com.ipvc.bll.models.EncomendasFornecedor;
import com.ipvc.bll.models.Fornecedor;
import com.ipvc.bll.models.EstadosEncomenda;
import com.ipvc.bll.repos.EncomendaFornecedorRepo;
import com.ipvc.bll.repos.FornecedorRepo;
import com.ipvc.bll.repos.EstadosEncomendaRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EncomendasFornecedorService {
    private final EncomendaFornecedorRepo encomendasFornecedorRepo;
    private final FornecedorRepo fornecedorRepo;
    private final EstadosEncomendaRepo estadosEncomendaRepo;

    public EncomendasFornecedorService(EncomendaFornecedorRepo encomendasFornecedorRepo, FornecedorRepo fornecedorRepo, EstadosEncomendaRepo estadosEncomendaRepo) {
        this.encomendasFornecedorRepo = encomendasFornecedorRepo;
        this.fornecedorRepo = fornecedorRepo;
        this.estadosEncomendaRepo = estadosEncomendaRepo;
    }

    public List<EncomendaFornecedorResponseDTO> getAllEncomendas() {
        return encomendasFornecedorRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<EncomendaFornecedorResponseDTO> getEncomendaById(Integer id) {
        return encomendasFornecedorRepo.findById(id).map(this::convertToDTO);
    }

    public EncomendaFornecedorResponseDTO saveEncomenda(EncomendaFornecedorCreateDTO encomendaDTO) {
        Fornecedor fornecedor = fornecedorRepo.findById(encomendaDTO.fornecedorId())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        EstadosEncomenda estado = estadosEncomendaRepo.findById(encomendaDTO.estadoId())
                .orElseThrow(() -> new RuntimeException("Estado de encomenda não encontrado"));

        EncomendasFornecedor encomenda = new EncomendasFornecedor();
        encomenda.setFornecedor(fornecedor);
        encomenda.setDataPedido(encomendaDTO.dataPedido());
        encomenda.setEstado(estado);
        encomenda.setValorTotal(encomendaDTO.valorTotal());

        return convertToDTO(encomendasFornecedorRepo.save(encomenda));
    }

    public Optional<EncomendaFornecedorResponseDTO> updateEncomenda(Integer id, EncomendaFornecedorUpdateDTO encomendaDTO) {
        return encomendasFornecedorRepo.findById(id).map(encomenda -> {
            EstadosEncomenda estado = estadosEncomendaRepo.findById(encomendaDTO.estadoId())
                    .orElseThrow(() -> new RuntimeException("Estado de encomenda não encontrado"));

            encomenda.setDataPedido(encomendaDTO.dataPedido());
            encomenda.setEstado(estado);
            encomenda.setValorTotal(encomendaDTO.valorTotal());

            return convertToDTO(encomendasFornecedorRepo.save(encomenda));
        });
    }

    public void deleteEncomenda(Integer id) {
        encomendasFornecedorRepo.deleteById(id);
    }

    private EncomendaFornecedorResponseDTO convertToDTO(EncomendasFornecedor encomenda) {
        return new EncomendaFornecedorResponseDTO(
                encomenda.getId(),
                encomenda.getFornecedor().getId(),
                encomenda.getFornecedor().getNome(),
                encomenda.getDataPedido(),
                encomenda.getEstado().getId(),
                encomenda.getEstado().getNome(),
                encomenda.getValorTotal()
        );
    }

    public EncomendasClientesStatsDTO obterEstatisticas() {
        EncomendasClientesStatsDTO dto = new EncomendasClientesStatsDTO();

        dto.setTotal(encomendasFornecedorRepo.count());
        dto.setPendentes(encomendasFornecedorRepo.contarPorEstado(1));  // estado_id = 1
        dto.setConcluidas(encomendasFornecedorRepo.contarPorEstado(2)); // estado_id = 2

        Map<String, Integer> porMes = new LinkedHashMap<>();

        for (Month mes : Month.values()) {
            String nomeMes = mes.getDisplayName(TextStyle.FULL, new Locale("pt", "PT"));
            porMes.put(nomeMes, 0);
        }

        List<Object[]> resultados = encomendasFornecedorRepo.contarPorMes();

        for (Object[] linha : resultados) {
            int mes = (int) linha[0];
            int quantidade = ((Number) linha[1]).intValue();
            String nomeMes = Month.of(mes).getDisplayName(TextStyle.FULL, new Locale("pt", "PT"));
            porMes.put(nomeMes, quantidade);
        }

        dto.setPorMes(porMes);
        return dto;
    }

    public List<EncomendaFornecedorFullResponseDTO> obterEncomendasFornecedorPorId(Integer fornecedorId) {
        List<EncomendasFornecedor> encomendas = encomendasFornecedorRepo.findByFornecedor_Id(fornecedorId);
        if (encomendas.isEmpty()) {
            throw new RuntimeException("Nenhuma encomenda encontrada para o fornecedor");
        }
        List<EncomendaFornecedorFullResponseDTO> resultado = new ArrayList<>();
        for (EncomendasFornecedor encomenda : encomendas) {
            List<ItensEncomendaFornecedorDTO.ItensEncomendaFornecedorResponseDTO> itens = encomenda.getItensEncomendaFornecedors().stream()
                    .map(itensEncomendaFornecedor -> new ItensEncomendaFornecedorDTO.ItensEncomendaFornecedorResponseDTO(
                            itensEncomendaFornecedor.getEncomenda().getId(),
                            itensEncomendaFornecedor.getMaterial().getId(),
                            itensEncomendaFornecedor.getMaterial().getNome(),
                            itensEncomendaFornecedor.getQuantidade(),
                            itensEncomendaFornecedor.getPrecoUnitario(),
                            itensEncomendaFornecedor.getQuantidade() != null && itensEncomendaFornecedor.getPrecoUnitario() != null
                                    ? itensEncomendaFornecedor.getQuantidade().multiply(itensEncomendaFornecedor.getPrecoUnitario()).doubleValue()
                                    : 0.0
                    ))
                    .collect(Collectors.toList());

            resultado.add(new EncomendaFornecedorFullResponseDTO(
                    encomenda.getId(),
                    encomenda.getFornecedor().getId(),
                    encomenda.getFornecedor().getNome(),
                    encomenda.getDataPedido(),
                    encomenda.getEstado().getId(),
                    encomenda.getEstado().getNome(),
                    encomenda.getValorTotal(),
                    itens
            ));
        }
        return resultado;
    }
}