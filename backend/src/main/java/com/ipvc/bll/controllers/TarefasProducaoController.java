package com.ipvc.bll.controllers;

import com.ipvc.bll.dto.TarefasProducaoDTO.*;
import com.ipvc.bll.services.TarefasProducaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarefas-producao")
@Tag(name = "Tarefas de Produção", description = "Gestão das tarefas de produção associadas às encomendas")
public class TarefasProducaoController {

    private final TarefasProducaoService tarefasProducaoService;

    public TarefasProducaoController(TarefasProducaoService tarefasProducaoService) {
        this.tarefasProducaoService = tarefasProducaoService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as tarefas de produção", description = "Retorna uma lista de todas as tarefas de produção registradas")
    public ResponseEntity<List<TarefasProducaoResponseDTO>> getAllTarefas() {
        return ResponseEntity.ok(tarefasProducaoService.getAllTarefas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma tarefa de produção", description = "Retorna os detalhes de uma tarefa de produção específica")
    public ResponseEntity<TarefasProducaoResponseDTO> getTarefaById(@PathVariable Integer id) {
        Optional<TarefasProducaoResponseDTO> tarefa = tarefasProducaoService.getTarefaById(id);
        return tarefa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar uma nova tarefa de produção", description = "Registra uma nova tarefa de produção")
    public ResponseEntity<TarefasProducaoResponseDTO> createTarefa(@RequestBody TarefasProducaoCreateDTO tarefaDTO) {
        TarefasProducaoResponseDTO novaTarefa = tarefasProducaoService.saveTarefa(tarefaDTO);
        return ResponseEntity.ok(novaTarefa);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma tarefa de produção", description = "Atualiza os detalhes de uma tarefa de produção")
    public ResponseEntity<TarefasProducaoResponseDTO> updateTarefa(
            @PathVariable Integer id, @RequestBody TarefasProducaoUpdateDTO tarefaDTO) {
        Optional<TarefasProducaoResponseDTO> updatedTarefa = tarefasProducaoService.updateTarefa(id, tarefaDTO);
        return updatedTarefa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma tarefa de produção", description = "Remove uma tarefa de produção do sistema")
    public ResponseEntity<Void> deleteTarefa(@PathVariable Integer id) {
        tarefasProducaoService.deleteTarefa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/funcionario/{id}")
    @Operation(summary = "Buscar tarefas por funcionário", description = "Retorna todas as tarefas de produção associadas a um funcionário específico")
    public ResponseEntity<List<TarefasProducaoResponseDTO>> getTarefasByFuncionarioId(@PathVariable Integer id) {
        List<TarefasProducaoResponseDTO> tarefas = tarefasProducaoService.getTarefasByFuncionarioId(id);
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/encomendas/{id}")
    @Operation(summary = "Buscar tarefas por encomenda", description = "Retorna todas as tarefas de produção associadas a uma encomenda específica")
    public ResponseEntity<List<TarefasProducaoResponseFullDTO>> getTarefasByEncomendaId(@PathVariable Integer id) {
        List<TarefasProducaoResponseFullDTO> tarefas = tarefasProducaoService.getTarefasByEncomendaId(id);
        return ResponseEntity.ok(tarefas);
    }
}
