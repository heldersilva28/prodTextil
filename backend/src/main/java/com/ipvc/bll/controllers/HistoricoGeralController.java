package com.ipvc.bll.controllers;

import com.ipvc.bll.dto.HistoricoGeralDTO.*;
import com.ipvc.bll.services.HistoricoGeralService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/historico")
@Tag(name = "Histórico Geral", description = "Gestão do histórico de eventos do sistema")
public class HistoricoGeralController {

    private final HistoricoGeralService historicoGeralService;

    public HistoricoGeralController(HistoricoGeralService historicoGeralService) {
        this.historicoGeralService = historicoGeralService;
    }

    @GetMapping
    @Operation(summary = "Listar todo o histórico", description = "Retorna uma lista de todos os registros de eventos")
    public ResponseEntity<List<HistoricoGeralResponseDTO>> getAllHistorico() {
        return ResponseEntity.ok(historicoGeralService.getAllHistorico());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um registro do histórico", description = "Retorna os detalhes de um evento específico pelo ID")
    public ResponseEntity<HistoricoGeralResponseDTO> getHistoricoById(@PathVariable Integer id) {
        Optional<HistoricoGeralResponseDTO> historico = historicoGeralService.getHistoricoById(id);
        return historico.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo registro no histórico", description = "Registra um novo evento no sistema")
    public ResponseEntity<HistoricoGeralResponseDTO> createHistorico(@RequestBody HistoricoGeralCreateDTO historicoDTO) {
        HistoricoGeralResponseDTO novoHistorico = historicoGeralService.saveHistorico(historicoDTO);
        return ResponseEntity.ok(novoHistorico);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um registro do histórico", description = "Atualiza os dados de um evento registrado")
    public ResponseEntity<HistoricoGeralResponseDTO> updateHistorico(@PathVariable Integer id, @RequestBody HistoricoGeralUpdateDTO historicoDTO) {
        Optional<HistoricoGeralResponseDTO> updatedHistorico = historicoGeralService.updateHistorico(id, historicoDTO);
        return updatedHistorico.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um registro do histórico", description = "Remove um evento registrado do sistema")
    public ResponseEntity<Void> deleteHistorico(@PathVariable Integer id) {
        historicoGeralService.deleteHistorico(id);
        return ResponseEntity.noContent().build();
    }
}
