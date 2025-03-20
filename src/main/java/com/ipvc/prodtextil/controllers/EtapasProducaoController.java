package com.ipvc.prodtextil.controllers;

import com.ipvc.prodtextil.dto.EtapasProducaoDTO.*;
import com.ipvc.prodtextil.services.EtapasProducaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/etapas-producao")
@Tag(name = "Etapas de Produção", description = "Gestão das etapas de produção")
public class EtapasProducaoController {

    private final EtapasProducaoService etapasProducaoService;

    public EtapasProducaoController(EtapasProducaoService etapasProducaoService) {
        this.etapasProducaoService = etapasProducaoService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as etapas de produção", description = "Retorna uma lista de todas as etapas registradas")
    public ResponseEntity<List<EtapaProducaoResponseDTO>> getAllEtapas() {
        return ResponseEntity.ok(etapasProducaoService.getAllEtapas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma etapa de produção", description = "Retorna os detalhes de uma etapa de produção pelo ID")
    public ResponseEntity<EtapaProducaoResponseDTO> getEtapaById(@PathVariable Integer id) {
        Optional<EtapaProducaoResponseDTO> etapa = etapasProducaoService.getEtapaById(id);
        return etapa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar uma nova etapa de produção", description = "Registra uma nova etapa de produção no sistema")
    public ResponseEntity<EtapaProducaoResponseDTO> createEtapa(@RequestBody EtapaProducaoCreateDTO etapaDTO) {
        EtapaProducaoResponseDTO novaEtapa = etapasProducaoService.saveEtapa(etapaDTO);
        return ResponseEntity.ok(novaEtapa);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma etapa de produção", description = "Atualiza os dados de uma etapa de produção existente")
    public ResponseEntity<EtapaProducaoResponseDTO> updateEtapa(@PathVariable Integer id, @RequestBody EtapaProducaoUpdateDTO etapaDTO) {
        Optional<EtapaProducaoResponseDTO> updatedEtapa = etapasProducaoService.updateEtapa(id, etapaDTO);
        return updatedEtapa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma etapa de produção", description = "Remove uma etapa de produção do sistema")
    public ResponseEntity<Void> deleteEtapa(@PathVariable Integer id) {
        etapasProducaoService.deleteEtapa(id);
        return ResponseEntity.noContent().build();
    }
}
