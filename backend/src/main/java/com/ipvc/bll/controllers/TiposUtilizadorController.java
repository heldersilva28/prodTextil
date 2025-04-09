package com.ipvc.bll.controllers;

import com.ipvc.bll.dto.TiposUtilizadorDTO.*;
import com.ipvc.bll.services.TiposUtilizadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tipos-utilizadores")
@Tag(name = "Tipos de Utilizadores", description = "Gestão dos tipos de utilizadores")
public class TiposUtilizadorController {

    private final TiposUtilizadorService tiposUtilizadorService;

    public TiposUtilizadorController(TiposUtilizadorService tiposUtilizadorService) {
        this.tiposUtilizadorService = tiposUtilizadorService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os tipos de utilizadores", description = "Retorna uma lista de todos os tipos de utilizadores registrados")
    public ResponseEntity<List<TiposUtilizadorResponseDTO>> getAllTiposUtilizadores() {
        return ResponseEntity.ok(tiposUtilizadorService.getAllTiposUtilizadores());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um tipo de utilizador", description = "Retorna os detalhes de um tipo de utilizador específico")
    public ResponseEntity<TiposUtilizadorResponseDTO> getTipoUtilizadorById(@PathVariable Integer id) {
        Optional<TiposUtilizadorResponseDTO> tipoUtilizador = tiposUtilizadorService.getTipoUtilizadorById(id);
        return tipoUtilizador.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Novo endpoint para retornar apenas o nome do tipo de utilizador
    @GetMapping("/{id}/nome")
    @Operation(summary = "Buscar nome de um tipo de utilizador", description = "Retorna o nome de um tipo de utilizador específico")
    public ResponseEntity<String> getTipoUtilizadorNomeById(@PathVariable Integer id) {
        Optional<String> nome = tiposUtilizadorService.getTipoUtilizadorNomeById(id);
        return nome.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo tipo de utilizador", description = "Registra um novo tipo de utilizador")
    public ResponseEntity<TiposUtilizadorResponseDTO> createTipoUtilizador(@RequestBody TiposUtilizadorCreateDTO tipoUtilizadorDTO) {
        TiposUtilizadorResponseDTO novoTipoUtilizador = tiposUtilizadorService.saveTipoUtilizador(tipoUtilizadorDTO);
        return ResponseEntity.ok(novoTipoUtilizador);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um tipo de utilizador", description = "Atualiza os detalhes de um tipo de utilizador")
    public ResponseEntity<TiposUtilizadorResponseDTO> updateTipoUtilizador(
            @PathVariable Integer id, @RequestBody TiposUtilizadorUpdateDTO tipoUtilizadorDTO) {
        Optional<TiposUtilizadorResponseDTO> updatedTipoUtilizador = tiposUtilizadorService.updateTipoUtilizador(id, tipoUtilizadorDTO);
        return updatedTipoUtilizador.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um tipo de utilizador", description = "Remove um tipo de utilizador do sistema")
    public ResponseEntity<Void> deleteTipoUtilizador(@PathVariable Integer id) {
        tiposUtilizadorService.deleteTipoUtilizador(id);
        return ResponseEntity.noContent().build();
    }
}
