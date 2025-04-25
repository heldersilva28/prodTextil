package com.ipvc.bll.controllers;

import com.ipvc.bll.dto.MateriaisDTO.*;
import com.ipvc.bll.dto.MateriaisStatsDTO;
import com.ipvc.bll.services.MateriaisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/materiais")
@Tag(name = "Materiais", description = "Gestão de materiais")
public class MateriaisController {

    private final MateriaisService materiaisService;

    public MateriaisController(MateriaisService materiaisService) {
        this.materiaisService = materiaisService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os materiais", description = "Retorna uma lista de todos os materiais disponíveis")
    public ResponseEntity<List<MateriaisResponseDTO>> getAllMateriais() {
        return ResponseEntity.ok(materiaisService.getAllMateriais());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um material", description = "Retorna os detalhes de um material específico")
    public ResponseEntity<MateriaisResponseDTO> getMaterialById(@PathVariable Integer id) {
        Optional<MateriaisResponseDTO> material = materiaisService.getMaterialById(id);
        return material.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo material", description = "Registra um novo material no sistema")
    public ResponseEntity<MateriaisResponseDTO> createMaterial(@RequestBody MateriaisCreateDTO materialDTO) {
        MateriaisResponseDTO novoMaterial = materiaisService.saveMaterial(materialDTO);
        return ResponseEntity.ok(novoMaterial);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um material", description = "Atualiza os detalhes de um material existente")
    public ResponseEntity<MateriaisResponseDTO> updateMaterial(
            @PathVariable Integer id, @RequestBody MateriaisUpdateDTO materialDTO) {
        Optional<MateriaisResponseDTO> updatedMaterial = materiaisService.updateMaterial(id, materialDTO);
        return updatedMaterial.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um material", description = "Remove um material do sistema")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Integer id) {
        materiaisService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<MateriaisStatsDTO> getEstatisticasMateriais() {
        return ResponseEntity.ok(materiaisService.obterEstatisticas());
    }
}
