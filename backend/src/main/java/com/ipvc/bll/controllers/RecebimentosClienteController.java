package com.ipvc.bll.controllers;

import com.ipvc.bll.dto.RecebimentosClienteDTO.*;
import com.ipvc.bll.services.RecebimentosClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recebimentos-clientes")
@Tag(name = "Recebimentos de Clientes", description = "Gestão dos recebimentos feitos por clientes")
public class RecebimentosClienteController {

    private final RecebimentosClienteService recebimentosClienteService;

    public RecebimentosClienteController(RecebimentosClienteService recebimentosClienteService) {
        this.recebimentosClienteService = recebimentosClienteService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os recebimentos de clientes", description = "Retorna uma lista de todos os recebimentos registrados")
    public ResponseEntity<List<RecebimentosClienteResponseDTO>> getAllRecebimentos() {
        return ResponseEntity.ok(recebimentosClienteService.getAllRecebimentos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um recebimento de cliente", description = "Retorna os detalhes de um recebimento específico")
    public ResponseEntity<RecebimentosClienteResponseDTO> getRecebimentoById(@PathVariable Integer id) {
        Optional<RecebimentosClienteResponseDTO> recebimento = recebimentosClienteService.getRecebimentoById(id);
        return recebimento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo recebimento", description = "Registra um novo recebimento feito por um cliente")
    public ResponseEntity<RecebimentosClienteResponseDTO> createRecebimento(@RequestBody RecebimentosClienteCreateDTO recebimentoDTO) {
        RecebimentosClienteResponseDTO novoRecebimento = recebimentosClienteService.saveRecebimento(recebimentoDTO);
        return ResponseEntity.ok(novoRecebimento);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um recebimento de cliente", description = "Atualiza os detalhes de um recebimento feito por um cliente")
    public ResponseEntity<RecebimentosClienteResponseDTO> updateRecebimento(
            @PathVariable Integer id, @RequestBody RecebimentosClienteUpdateDTO recebimentoDTO) {
        Optional<RecebimentosClienteResponseDTO> updatedRecebimento = recebimentosClienteService.updateRecebimento(id, recebimentoDTO);
        return updatedRecebimento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um recebimento de cliente", description = "Remove um recebimento do sistema")
    public ResponseEntity<Void> deleteRecebimento(@PathVariable Integer id) {
        recebimentosClienteService.deleteRecebimento(id);
        return ResponseEntity.noContent().build();
    }
}
