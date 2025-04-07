package com.ipvc.bll.controllers;

import com.ipvc.bll.dto.MetodosPagamentoDTO.*;
import com.ipvc.bll.services.MetodosPagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/metodos-pagamento")
@Tag(name = "Métodos de Pagamento", description = "Gestão de métodos de pagamento")
public class MetodosPagamentoController {

    private final MetodosPagamentoService metodosPagamentoService;

    public MetodosPagamentoController(MetodosPagamentoService metodosPagamentoService) {
        this.metodosPagamentoService = metodosPagamentoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os métodos de pagamento", description = "Retorna uma lista de todos os métodos de pagamento disponíveis")
    public ResponseEntity<List<MetodosPagamentoResponseDTO>> getAllMetodosPagamento() {
        return ResponseEntity.ok(metodosPagamentoService.getAllMetodosPagamento());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um método de pagamento", description = "Retorna os detalhes de um método de pagamento específico")
    public ResponseEntity<MetodosPagamentoResponseDTO> getMetodoPagamentoById(@PathVariable Integer id) {
        Optional<MetodosPagamentoResponseDTO> metodo = metodosPagamentoService.getMetodoPagamentoById(id);
        return metodo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo método de pagamento", description = "Registra um novo método de pagamento no sistema")
    public ResponseEntity<MetodosPagamentoResponseDTO> createMetodoPagamento(@RequestBody MetodosPagamentoCreateDTO metodoDTO) {
        MetodosPagamentoResponseDTO novoMetodo = metodosPagamentoService.saveMetodoPagamento(metodoDTO);
        return ResponseEntity.ok(novoMetodo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um método de pagamento", description = "Atualiza os detalhes de um método de pagamento existente")
    public ResponseEntity<MetodosPagamentoResponseDTO> updateMetodoPagamento(
            @PathVariable Integer id, @RequestBody MetodosPagamentoUpdateDTO metodoDTO) {
        Optional<MetodosPagamentoResponseDTO> updatedMetodo = metodosPagamentoService.updateMetodoPagamento(id, metodoDTO);
        return updatedMetodo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um método de pagamento", description = "Remove um método de pagamento do sistema")
    public ResponseEntity<Void> deleteMetodoPagamento(@PathVariable Integer id) {
        metodosPagamentoService.deleteMetodoPagamento(id);
        return ResponseEntity.noContent().build();
    }
}
