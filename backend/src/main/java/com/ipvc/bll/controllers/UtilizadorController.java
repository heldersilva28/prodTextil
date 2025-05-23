package com.ipvc.bll.controllers;

import com.ipvc.bll.dto.UtilizadorDTO.*;
import com.ipvc.bll.services.UtilizadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/utilizadores")
@Tag(name = "Utilizadores", description = "Gestão de Utilizadores")
public class UtilizadorController {

    private final UtilizadorService utilizadorService;

    public UtilizadorController(UtilizadorService utilizadorService) {
        this.utilizadorService = utilizadorService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os utilizadores", description = "Retorna uma lista de utilizadores")
    public ResponseEntity<List<UtilizadorResponseDTO>> getAllUtilizadores() {
        return ResponseEntity.ok(utilizadorService.getAllUtilizadores());
    }

    @GetMapping("/sem-funcionario")
    @Operation(summary = "Listar utilizadores sem funcionário e nao sao Admin", description = "Retorna uma lista de utilizadores que não são funcionários nem administradores")
    public ResponseEntity<List<UtilizadorResponseDTO>> getAllUtilizadoresSemFuncionarioSemAdmin() {
        return ResponseEntity.ok(utilizadorService.getAllUtilizadoresSemFuncionarioSemAdmin());
    }

    @GetMapping("/cargo")
    @Operation(summary = "Listar Cargo do Utilizador", description = "Retorna o ID do Cargo do Utilizador")
    public ResponseEntity<Integer> getCargoPorEmail(@RequestParam String email) {
        int cargo = utilizadorService.obterCargoPorEmail(email);
        return ResponseEntity.ok(cargo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar utilizador por ID", description = "Retorna um utilizador específico pelo ID")
    public ResponseEntity<UtilizadorResponseDTO> getUtilizadorById(@PathVariable Integer id) {
        Optional<UtilizadorResponseDTO> utilizador = utilizadorService.getUtilizadorById(id);
        return utilizador.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/email")
    @Operation(summary = "Buscar utilizador por email", description = "Retorna um utilizador específico pelo email")
    public ResponseEntity<UtilizadorResponseDTO> getUtilizadorByEmail(@RequestParam String email) {
        Optional<UtilizadorResponseDTO> utilizador = utilizadorService.getUtilizadorByEmail(email);
        return utilizador.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo utilizador", description = "Adiciona um novo utilizador ao sistema")
    public ResponseEntity<UtilizadorResponseDTO> createUtilizador(@RequestBody UtilizadorCreateDTO utilizadorDTO) {
        return ResponseEntity.ok(utilizadorService.saveUtilizador(utilizadorDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar utilizador", description = "Atualiza os dados de um utilizador existente")
    public ResponseEntity<UtilizadorResponseDTO> updateUtilizador(@PathVariable Integer id, @RequestBody UtilizadorUpdateDTO utilizadorDTO) {
        Optional<UtilizadorResponseDTO> updatedUtilizador = utilizadorService.updateUtilizador(id, utilizadorDTO);
        return updatedUtilizador.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar utilizador", description = "Remove um utilizador do sistema")
    public ResponseEntity<Void> deleteUtilizador(@PathVariable Integer id) {
        utilizadorService.deleteUtilizador(id);
        return ResponseEntity.noContent().build();
    }
}