package com.ipvc.prodtextil.controllers;

import com.ipvc.prodtextil.dto.ItensEncomendaClienteDTO.*;
import com.ipvc.prodtextil.services.ItensEncomendaClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/itens-encomenda-cliente")
@Tag(name = "Itens Encomenda Cliente", description = "Gestão de itens de encomendas dos clientes")
public class ItensEncomendaClienteController {

    private final ItensEncomendaClienteService itensEncomendaClienteService;

    public ItensEncomendaClienteController(ItensEncomendaClienteService itensEncomendaClienteService) {
        this.itensEncomendaClienteService = itensEncomendaClienteService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os itens de encomenda", description = "Retorna uma lista de todos os itens de encomenda dos clientes")
    public ResponseEntity<List<ItensEncomendaClienteResponseDTO>> getAllItens() {
        return ResponseEntity.ok(itensEncomendaClienteService.getAllItens());
    }

    @GetMapping("/{encomendaId}/{produto}")
    @Operation(summary = "Buscar um item de encomenda", description = "Retorna os detalhes de um item de encomenda específico")
    public ResponseEntity<ItensEncomendaClienteResponseDTO> getItemById(
            @PathVariable Integer encomendaId, @PathVariable String produto) {
        Optional<ItensEncomendaClienteResponseDTO> item = itensEncomendaClienteService.getItemById(encomendaId, produto);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo item de encomenda", description = "Registra um novo item de encomenda para um cliente")
    public ResponseEntity<ItensEncomendaClienteResponseDTO> createItem(@RequestBody ItensEncomendaClienteCreateDTO itemDTO) {
        ItensEncomendaClienteResponseDTO novoItem = itensEncomendaClienteService.saveItem(itemDTO);
        return ResponseEntity.ok(novoItem);
    }

    @PutMapping("/{encomendaId}/{produto}")
    @Operation(summary = "Atualizar um item de encomenda", description = "Atualiza os detalhes de um item de encomenda existente")
    public ResponseEntity<ItensEncomendaClienteResponseDTO> updateItem(
            @PathVariable Integer encomendaId, @PathVariable String produto, @RequestBody ItensEncomendaClienteUpdateDTO itemDTO) {
        Optional<ItensEncomendaClienteResponseDTO> updatedItem = itensEncomendaClienteService.updateItem(encomendaId, produto, itemDTO);
        return updatedItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{encomendaId}/{produto}")
    @Operation(summary = "Deletar um item de encomenda", description = "Remove um item de uma encomenda de cliente")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer encomendaId, @PathVariable String produto) {
        itensEncomendaClienteService.deleteItem(encomendaId, produto);
        return ResponseEntity.noContent().build();
    }
}
