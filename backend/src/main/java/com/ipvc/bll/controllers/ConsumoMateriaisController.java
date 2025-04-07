package com.ipvc.bll.controllers;

import com.ipvc.bll.dto.ConsumoMateriaisDTO.*;
import com.ipvc.bll.services.ConsumoMateriaisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/consumos-materiais")
@Tag(name = "Consumo de Materiais", description = "Gestão de consumo de materiais na produção")
public class ConsumoMateriaisController {

    private final ConsumoMateriaisService consumoMateriaisService;

    public ConsumoMateriaisController(ConsumoMateriaisService consumoMateriaisService) {
        this.consumoMateriaisService = consumoMateriaisService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os consumos de materiais", description = "Retorna uma lista de todos os consumos registrados")
    public ResponseEntity<List<ConsumoMateriaisResponseDTO>> getAllConsumos() {
        return ResponseEntity.ok(consumoMateriaisService.getAllConsumos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um consumo de material", description = "Retorna os detalhes de um consumo específico pelo ID")
    public ResponseEntity<ConsumoMateriaisResponseDTO> getConsumoById(@PathVariable Integer id) {
        Optional<ConsumoMateriaisResponseDTO> consumo = consumoMateriaisService.getConsumoById(id);
        return consumo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Registrar um novo consumo de material", description = "Registra um novo consumo de material no sistema")
    public ResponseEntity<ConsumoMateriaisResponseDTO> createConsumo(@RequestBody ConsumoMateriaisCreateDTO consumoDTO) {
        ConsumoMateriaisResponseDTO novoConsumo = consumoMateriaisService.saveConsumo(consumoDTO);
        return ResponseEntity.ok(novoConsumo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um consumo de material", description = "Atualiza os dados de um consumo já registrado")
    public ResponseEntity<ConsumoMateriaisResponseDTO> updateConsumo(@PathVariable Integer id, @RequestBody ConsumoMateriaisUpdateDTO consumoDTO) {
        Optional<ConsumoMateriaisResponseDTO> updatedConsumo = consumoMateriaisService.updateConsumo(id, consumoDTO);
        return updatedConsumo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um consumo de material", description = "Remove um registro de consumo de material do sistema")
    public ResponseEntity<Void> deleteConsumo(@PathVariable Integer id) {
        consumoMateriaisService.deleteConsumo(id);
        return ResponseEntity.noContent().build();
    }
}
