package com.ipvc.prodtextil.controllers;

import com.ipvc.prodtextil.dto.TiposEtapasProducaoDTO.*;
import com.ipvc.prodtextil.services.TiposEtapasProducaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tipos-etapas")
@Tag(name = "Tipos de Etapas de Produção", description = "Gestão dos tipos de etapas de produção")
public class TiposEtapasProducaoController {

    private final TiposEtapasProducaoService tiposEtapasProducaoService;

    public TiposEtapasProducaoController(TiposEtapasProducaoService tiposEtapasProducaoService) {
        this.tiposEtapasProducaoService = tiposEtapasProducaoService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as etapas de produção", description = "Retorna uma lista de todas os tipos de etapas de produção registradas")
    public ResponseEntity<List<TiposEtapasProducaoResponseDTO>> getAllTiposEtapas() {
        return ResponseEntity.ok(tiposEtapasProducaoService.getAllTiposEtapas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um tipo de etapa de produção", description = "Retorna os detalhes de um tipo de etapa de produção específica")
    public ResponseEntity<TiposEtapasProducaoResponseDTO> getTipoEtapaById(@PathVariable Integer id) {
        Optional<TiposEtapasProducaoResponseDTO> tipoEtapa = tiposEtapasProducaoService.getTipoEtapaById(id);
        return tipoEtapa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo tipo de etapa de produção", description = "Registra um novo tipo de etapa de produção")
    public ResponseEntity<TiposEtapasProducaoResponseDTO> createTipoEtapa(@RequestBody TiposEtapasProducaoCreateDTO tipoEtapaDTO) {
        TiposEtapasProducaoResponseDTO novaEtapa = tiposEtapasProducaoService.saveTipoEtapa(tipoEtapaDTO);
        return ResponseEntity.ok(novaEtapa);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um tipo de etapa de produção", description = "Atualiza os detalhes de um tipo de etapa de produção")
    public ResponseEntity<TiposEtapasProducaoResponseDTO> updateTipoEtapa(
            @PathVariable Integer id, @RequestBody TiposEtapasProducaoUpdateDTO tipoEtapaDTO) {
        Optional<TiposEtapasProducaoResponseDTO> updatedEtapa = tiposEtapasProducaoService.updateTipoEtapa(id, tipoEtapaDTO);
        return updatedEtapa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um tipo de etapa de produção", description = "Remove um tipo de etapa de produção do sistema")
    public ResponseEntity<Void> deleteTipoEtapa(@PathVariable Integer id) {
        tiposEtapasProducaoService.deleteTipoEtapa(id);
        return ResponseEntity.noContent().build();
    }
}
