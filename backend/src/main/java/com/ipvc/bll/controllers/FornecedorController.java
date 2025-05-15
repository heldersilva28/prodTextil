package com.ipvc.bll.controllers;

import com.ipvc.bll.dto.FornecedorDTO.*;
import com.ipvc.bll.models.Cliente;
import com.ipvc.bll.models.Fornecedor;
import com.ipvc.bll.services.FornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/fornecedores")
@Tag(name = "Fornecedores", description = "Gestão de Fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os fornecedores", description = "Retorna uma lista de fornecedores")
    public ResponseEntity<List<FornecedorResponseDTO>> getAllFornecedores() {
        return ResponseEntity.ok(fornecedorService.getAllFornecedores());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar fornecedor por ID", description = "Retorna um fornecedor específico pelo ID")
    public ResponseEntity<FornecedorResponseDTO> getFornecedorById(@PathVariable Integer id) {
        Optional<FornecedorResponseDTO> fornecedor = fornecedorService.getFornecedorById(id);
        return fornecedor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo fornecedor", description = "Adiciona um novo fornecedor ao sistema")
    public ResponseEntity<FornecedorResponseDTO> createFornecedor(@RequestBody FornecedorCreateDTO fornecedorDTO) {
        return ResponseEntity.ok(fornecedorService.saveFornecedor(fornecedorDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar fornecedor", description = "Atualiza os dados de um fornecedor existente")
    public ResponseEntity<FornecedorResponseDTO> updateFornecedor(@PathVariable Integer id, @RequestBody FornecedorUpdateDTO fornecedorDTO) {
        Optional<FornecedorResponseDTO> updatedFornecedor = fornecedorService.updateFornecedor(id, fornecedorDTO);
        return updatedFornecedor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar fornecedor", description = "Remove um fornecedor do sistema")
    public ResponseEntity<Void> deleteFornecedor(@PathVariable Integer id) {
        fornecedorService.deleteFornecedor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/emails")
    @Operation(summary = "Listar todos os emails de clientes", description = "Retorna todos os emails de clientes")
    public ResponseEntity<Map<Integer,String>> getAllEmailFornecedores() {
        List<Fornecedor> fornecedores = fornecedorService.getAllFornecedores();
        Map<Integer,String> emails = new HashMap<>();
        for (Cliente cliente : clientes) {
            emails.put(cliente.getId(), cliente.getEmail());
        }
        return ResponseEntity.ok(emails);
    }
}
