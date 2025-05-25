package com.ipvc.bll.controllers;

import com.ipvc.bll.dto.ItensEncomendaFornecedorDTO.*;
import com.ipvc.bll.services.ItensEncomendaFornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/itens-encomenda-fornecedor")
@Tag(name = "Itens Encomenda Fornecedor", description = "Gestão de itens de encomendas dos fornecedores")
public class ItensEncomendaFornecedorController {

    private final ItensEncomendaFornecedorService itensEncomendaFornecedorService;

    public ItensEncomendaFornecedorController(ItensEncomendaFornecedorService itensEncomendaFornecedorService) {
        this.itensEncomendaFornecedorService = itensEncomendaFornecedorService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os itens de encomenda", description = "Retorna uma lista de todos os itens de encomenda dos fornecedores")
    public ResponseEntity<List<ItensEncomendaFornecedorResponseDTO>> getAllItens() {
        return ResponseEntity.ok(itensEncomendaFornecedorService.getAllItens());
    }

    @GetMapping("/{encomendaId}/{materialId}")
    @Operation(summary = "Buscar um item de encomenda", description = "Retorna os detalhes de um item de encomenda específico")
    public ResponseEntity<ItensEncomendaFornecedorResponseDTO> getItemById(
            @PathVariable Integer encomendaId, @PathVariable Integer materialId) {
        Optional<ItensEncomendaFornecedorResponseDTO> item = itensEncomendaFornecedorService.getItemById(encomendaId, materialId);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo item de encomenda", description = "Registra um novo item de encomenda para um fornecedor")
    public ResponseEntity<ItensEncomendaFornecedorResponseDTO> createItem(
            @RequestBody ItensEncomendaFornecedorCreateDTO itemDTO) {
        ItensEncomendaFornecedorResponseDTO novoItem = itensEncomendaFornecedorService.saveItem(itemDTO);
        URI location = URI.create(String.format("/api/itens-encomenda-fornecedor/%d/%d", novoItem.encomendaId(), novoItem.materialId()));
        return ResponseEntity.created(location).body(novoItem);
    }

    @PutMapping("/{encomendaId}/{materialId}")
    @Operation(summary = "Atualizar um item de encomenda", description = "Atualiza os detalhes de um item de encomenda existente")
    public ResponseEntity<ItensEncomendaFornecedorResponseDTO> updateItem(
            @PathVariable Integer encomendaId, @PathVariable Integer materialId,
            @RequestBody ItensEncomendaFornecedorUpdateDTO itemDTO) {
        Optional<ItensEncomendaFornecedorResponseDTO> updatedItem = itensEncomendaFornecedorService.updateItem(encomendaId, materialId, itemDTO);
        return updatedItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{encomendaId}/{materialId}")
    @Operation(summary = "Deletar um item de encomenda", description = "Remove um item de uma encomenda de fornecedor")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer encomendaId, @PathVariable Integer materialId) {
        itensEncomendaFornecedorService.deleteItem(encomendaId, materialId);
        return ResponseEntity.noContent().build();
    }
}
