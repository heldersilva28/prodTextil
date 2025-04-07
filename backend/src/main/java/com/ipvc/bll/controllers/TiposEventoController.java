package com.ipvc.bll.controllers;

import com.ipvc.bll.dto.TiposEventoDTO.*;
import com.ipvc.bll.services.TiposEventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tipos-eventos")
@Tag(name = "Tipos de Eventos", description = "Gestão dos tipos de eventos")
public class TiposEventoController {

    private final TiposEventoService tiposEventoService;

    public TiposEventoController(TiposEventoService tiposEventoService) {
        this.tiposEventoService = tiposEventoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os tipos de eventos", description = "Retorna uma lista de todos os tipos de eventos registrados")
    public ResponseEntity<List<TiposEventoResponseDTO>> getAllTiposEventos() {
        return ResponseEntity.ok(tiposEventoService.getAllTiposEventos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um tipo de evento", description = "Retorna os detalhes de um tipo de evento específico")
    public ResponseEntity<TiposEventoResponseDTO> getTipoEventoById(@PathVariable Integer id) {
        Optional<TiposEventoResponseDTO> tipoEvento = tiposEventoService.getTipoEventoById(id);
        return tipoEvento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo tipo de evento", description = "Registra um novo tipo de evento")
    public ResponseEntity<TiposEventoResponseDTO> createTipoEvento(@RequestBody TiposEventoCreateDTO tipoEventoDTO) {
        TiposEventoResponseDTO novoTipoEvento = tiposEventoService.saveTipoEvento(tipoEventoDTO);
        return ResponseEntity.ok(novoTipoEvento);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um tipo de evento", description = "Atualiza os detalhes de um tipo de evento")
    public ResponseEntity<TiposEventoResponseDTO> updateTipoEvento(
            @PathVariable Integer id, @RequestBody TiposEventoUpdateDTO tipoEventoDTO) {
        Optional<TiposEventoResponseDTO> updatedTipoEvento = tiposEventoService.updateTipoEvento(id, tipoEventoDTO);
        return updatedTipoEvento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um tipo de evento", description = "Remove um tipo de evento do sistema")
    public ResponseEntity<Void> deleteTipoEvento(@PathVariable Integer id) {
        tiposEventoService.deleteTipoEvento(id);
        return ResponseEntity.noContent().build();
    }
}
