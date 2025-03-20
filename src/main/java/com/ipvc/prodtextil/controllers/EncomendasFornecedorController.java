package com.ipvc.prodtextil.controllers;

import com.ipvc.prodtextil.dto.EncomendasFornecedorDTO.*;
import com.ipvc.prodtextil.services.EncomendasFornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/encomendas-fornecedores")
@Tag(name = "Encomendas de Fornecedores", description = "Gestão de encomendas feitas aos fornecedores")
public class EncomendasFornecedorController {

    private final EncomendasFornecedorService encomendasFornecedorService;

    public EncomendasFornecedorController(EncomendasFornecedorService encomendasFornecedorService) {
        this.encomendasFornecedorService = encomendasFornecedorService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as encomendas de fornecedores", description = "Retorna uma lista de todas as encomendas registradas")
    public ResponseEntity<List<EncomendaFornecedorResponseDTO>> getAllEncomendas() {
        return ResponseEntity.ok(encomendasFornecedorService.getAllEncomendas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma encomenda específica", description = "Retorna os detalhes de uma encomenda específica pelo ID")
    public ResponseEntity<EncomendaFornecedorResponseDTO> getEncomendaById(@PathVariable Integer id) {
        Optional<EncomendaFornecedorResponseDTO> encomenda = encomendasFornecedorService.getEncomendaById(id);
        return encomenda.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar uma nova encomenda", description = "Registra uma nova encomenda no sistema")
    public ResponseEntity<EncomendaFornecedorResponseDTO> createEncomenda(@RequestBody EncomendaFornecedorCreateDTO encomendaDTO) {
        EncomendaFornecedorResponseDTO novaEncomenda = encomendasFornecedorService.saveEncomenda(encomendaDTO);
        return ResponseEntity.ok(novaEncomenda);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma encomenda", description = "Atualiza os dados de uma encomenda já registrada")
    public ResponseEntity<EncomendaFornecedorResponseDTO> updateEncomenda(@PathVariable Integer id, @RequestBody EncomendaFornecedorUpdateDTO encomendaDTO) {
        Optional<EncomendaFornecedorResponseDTO> updatedEncomenda = encomendasFornecedorService.updateEncomenda(id, encomendaDTO);
        return updatedEncomenda.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma encomenda", description = "Remove uma encomenda do sistema")
    public ResponseEntity<Void> deleteEncomenda(@PathVariable Integer id) {
        encomendasFornecedorService.deleteEncomenda(id);
        return ResponseEntity.noContent().build();
    }
}
