package com.ipvc.prodtextil.controllers;

import com.ipvc.prodtextil.dto.PagamentosFornecedorDTO.*;
import com.ipvc.prodtextil.services.PagamentosFornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pagamentos-fornecedores")
@Tag(name = "Pagamentos de Fornecedores", description = "Gestão dos pagamentos realizados a fornecedores")
public class PagamentosFornecedorController {

    private final PagamentosFornecedorService pagamentosFornecedorService;

    public PagamentosFornecedorController(PagamentosFornecedorService pagamentosFornecedorService) {
        this.pagamentosFornecedorService = pagamentosFornecedorService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os pagamentos de fornecedores", description = "Retorna uma lista de todos os pagamentos registrados")
    public ResponseEntity<List<PagamentosFornecedorResponseDTO>> getAllPagamentos() {
        return ResponseEntity.ok(pagamentosFornecedorService.getAllPagamentos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um pagamento de fornecedor", description = "Retorna os detalhes de um pagamento específico")
    public ResponseEntity<PagamentosFornecedorResponseDTO> getPagamentoById(@PathVariable Integer id) {
        Optional<PagamentosFornecedorResponseDTO> pagamento = pagamentosFornecedorService.getPagamentoById(id);
        return pagamento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo pagamento", description = "Registra um novo pagamento para um fornecedor")
    public ResponseEntity<PagamentosFornecedorResponseDTO> createPagamento(@RequestBody PagamentosFornecedorCreateDTO pagamentoDTO) {
        PagamentosFornecedorResponseDTO novoPagamento = pagamentosFornecedorService.savePagamento(pagamentoDTO);
        return ResponseEntity.ok(novoPagamento);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um pagamento de fornecedor", description = "Atualiza os detalhes de um pagamento realizado a um fornecedor")
    public ResponseEntity<PagamentosFornecedorResponseDTO> updatePagamento(
            @PathVariable Integer id, @RequestBody PagamentosFornecedorUpdateDTO pagamentoDTO) {
        Optional<PagamentosFornecedorResponseDTO> updatedPagamento = pagamentosFornecedorService.updatePagamento(id, pagamentoDTO);
        return updatedPagamento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um pagamento de fornecedor", description = "Remove um pagamento do sistema")
    public ResponseEntity<Void> deletePagamento(@PathVariable Integer id) {
        pagamentosFornecedorService.deletePagamento(id);
        return ResponseEntity.noContent().build();
    }
}
