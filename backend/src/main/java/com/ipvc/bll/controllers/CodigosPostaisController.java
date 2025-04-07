package com.ipvc.bll.controllers;

import com.ipvc.bll.dto.CodigosPostaisDTO.*;
import com.ipvc.bll.services.CodigosPostaisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/codigos-postais")
@Tag(name = "Códigos Postais", description = "Gestão de Códigos Postais")
public class CodigosPostaisController {

    private final CodigosPostaisService codigosPostaisService;

    public CodigosPostaisController(CodigosPostaisService codigosPostaisService) {
        this.codigosPostaisService = codigosPostaisService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os códigos postais", description = "Retorna uma lista de todos os códigos postais disponíveis")
    public ResponseEntity<List<CodigoPostalResponseDTO>> getAllCodigosPostais() {
        return ResponseEntity.ok(codigosPostaisService.getAllCodigosPostais());
    }

    @GetMapping("/{codigo}")
    @Operation(summary = "Buscar um código postal", description = "Retorna os detalhes de um código postal específico")
    public ResponseEntity<CodigoPostalResponseDTO> getCodigoPostalByCodigo(@PathVariable String codigo) {
        Optional<CodigoPostalResponseDTO> codigoPostal = codigosPostaisService.getCodigoPostalByCodigo(codigo);
        return codigoPostal.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo código postal", description = "Adiciona um novo código postal ao sistema")
    public ResponseEntity<CodigoPostalResponseDTO> createCodigoPostal(@RequestBody CodigoPostalCreateDTO codigoPostalDTO) {
        CodigoPostalResponseDTO novoCodigoPostal = codigosPostaisService.saveCodigoPostal(codigoPostalDTO);
        return ResponseEntity.ok(novoCodigoPostal);
    }

    @PutMapping("/{codigo}")
    @Operation(summary = "Atualizar um código postal", description = "Atualiza as informações de um código postal existente")
    public ResponseEntity<CodigoPostalResponseDTO> updateCodigoPostal(@PathVariable String codigo, @RequestBody CodigoPostalUpdateDTO codigoPostalDTO) {
        Optional<CodigoPostalResponseDTO> updatedCodigoPostal = codigosPostaisService.updateCodigoPostal(codigo, codigoPostalDTO);
        return updatedCodigoPostal.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{codigo}")
    @Operation(summary = "Deletar um código postal", description = "Remove um código postal do sistema")
    public ResponseEntity<Void> deleteCodigoPostal(@PathVariable String codigo) {
        codigosPostaisService.deleteCodigoPostal(codigo);
        return ResponseEntity.noContent().build();
    }
}
