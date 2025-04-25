package com.ipvc.bll.services;

import com.ipvc.bll.dto.MateriaisDTO.*;
import com.ipvc.bll.dto.MateriaisStatsDTO;
import com.ipvc.bll.models.Materiais;
import com.ipvc.bll.models.TiposMateriais;
import com.ipvc.bll.repos.MateriaisRepo;
import com.ipvc.bll.repos.TiposMateriaisRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    public MateriaisStatsDTO obterEstatisticas() {
        MateriaisStatsDTO dto = new MateriaisStatsDTO();

        dto.setTotal(materiaisRepo.count());
        dto.setBaixoStock(materiaisRepo.countByStockDisponivelLessThanEqual(100)); // Considerar <= 100 como baixo stock

        Map<String, Integer> porCategoria = new HashMap<>();
        Map<String, BigDecimal> valorPorCategoria = new HashMap<>();

        tiposMateriaisRepo.findAll().forEach(tipo -> {
            String nomeTipo = tipo.getNome();

            // Somar o total de stock disponível por tipo de material
            int totalStock = materiaisRepo.findByTipo_Id(tipo.getId()).stream()
                    .mapToInt(mat -> mat.getStockDisponivel().intValue()) // Se for BigDecimal
                    .sum();
            porCategoria.put(nomeTipo, totalStock);

            // Calcular valor total (stock * preco)
            BigDecimal valorTotal = materiaisRepo.findByTipo_Id(tipo.getId()).stream()
                    .map(mat -> mat.getPrecoUnidade().multiply(mat.getStockDisponivel()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            valorPorCategoria.put(nomeTipo, valorTotal);
        });

        dto.setPorCategoria(porCategoria);
        dto.setValorPorCategoria(valorPorCategoria);

        return dto;
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
