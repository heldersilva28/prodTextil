package com.ipvc.prodtextil.controllers;

import com.ipvc.prodtextil.dto.TiposMateriaisDTO.*;
import com.ipvc.prodtextil.services.TiposMateriaisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tipos-materiais")
@Tag(name = "Tipos de Materiais", description = "Gestão dos tipos de materiais")
public class TiposMateriaisController {

    private final TiposMateriaisService tiposMateriaisService;

    public TiposMateriaisController(TiposMateriaisService tiposMateriaisService) {
        this.tiposMateriaisService = tiposMateriaisService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os tipos de materiais", description = "Retorna uma lista de todos os tipos de materiais registrados")
    public ResponseEntity<List<TiposMateriaisResponseDTO>> getAllTiposMateriais() {
        return ResponseEntity.ok(tiposMateriaisService.getAllTiposMateriais());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um tipo de material", description = "Retorna os detalhes de um tipo de material específico")
    public ResponseEntity<TiposMateriaisResponseDTO> getTipoMaterialById(@PathVariable Integer id) {
        Optional<TiposMateriaisResponseDTO> tipoMaterial = tiposMateriaisService.getTipoMaterialById(id);
        return tipoMaterial.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo tipo de material", description = "Registra um novo tipo de material")
    public ResponseEntity<TiposMateriaisResponseDTO> createTipoMaterial(@RequestBody TiposMateriaisCreateDTO tipoMaterialDTO) {
        TiposMateriaisResponseDTO novoTipoMaterial = tiposMateriaisService.saveTipoMaterial(tipoMaterialDTO);
        return ResponseEntity.ok(novoTipoMaterial);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um tipo de material", description = "Atualiza os detalhes de um tipo de material")
    public ResponseEntity<TiposMateriaisResponseDTO> updateTipoMaterial(
            @PathVariable Integer id, @RequestBody TiposMateriaisUpdateDTO tipoMaterialDTO) {
        Optional<TiposMateriaisResponseDTO> updatedTipoMaterial = tiposMateriaisService.updateTipoMaterial(id, tipoMaterialDTO);
        return updatedTipoMaterial.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um tipo de material", description = "Remove um tipo de material do sistema")
    public ResponseEntity<Void> deleteTipoMaterial(@PathVariable Integer id) {
        tiposMateriaisService.deleteTipoMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
