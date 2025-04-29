    package com.ipvc.bll.controllers;

    import com.ipvc.bll.dto.EstadosEncomendaDTO.*;
    import com.ipvc.bll.services.EstadosEncomendaService;
    import io.swagger.v3.oas.annotations.Operation;
    import io.swagger.v3.oas.annotations.tags.Tag;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping("/api/estados-encomenda")
    @Tag(name = "Estados de Encomenda", description = "Gestão dos estados das encomendas")
    public class EstadosEncomendaController {

        private final EstadosEncomendaService estadosEncomendaService;

        public EstadosEncomendaController(EstadosEncomendaService estadosEncomendaService) {
            this.estadosEncomendaService = estadosEncomendaService;
        }

        @GetMapping
        @Operation(summary = "Listar todos os estados de encomenda", description = "Retorna uma lista de todos os estados disponíveis")
        public ResponseEntity<List<EstadoEncomendaResponseDTO>> getAllEstados() {
            return ResponseEntity.ok(estadosEncomendaService.getAllEstados());
        }

        @GetMapping("/{id}")
        @Operation(summary = "Buscar um estado específico", description = "Retorna os detalhes de um estado de encomenda pelo ID")
        public ResponseEntity<EstadoEncomendaResponseDTO> getEstadoById(@PathVariable Integer id) {
            Optional<EstadoEncomendaResponseDTO> estado = estadosEncomendaService.getEstadoById(id);
            return estado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PostMapping
        @Operation(summary = "Criar um novo estado de encomenda", description = "Registra um novo estado de encomenda no sistema")
        public ResponseEntity<EstadoEncomendaResponseDTO> createEstado(@RequestBody EstadoEncomendaCreateDTO estadoDTO) {
            EstadoEncomendaResponseDTO novoEstado = estadosEncomendaService.saveEstado(estadoDTO);
            return ResponseEntity.ok(novoEstado);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Atualizar um estado de encomenda", description = "Atualiza os dados de um estado de encomenda existente")
        public ResponseEntity<EstadoEncomendaResponseDTO> updateEstado(@PathVariable Integer id, @RequestBody EstadoEncomendaUpdateDTO estadoDTO) {
            Optional<EstadoEncomendaResponseDTO> updatedEstado = estadosEncomendaService.updateEstado(id, estadoDTO);
            return updatedEstado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Deletar um estado de encomenda", description = "Remove um estado de encomenda do sistema")
        public ResponseEntity<Void> deleteEstado(@PathVariable Integer id) {
            estadosEncomendaService.deleteEstado(id);
            return ResponseEntity.noContent().build();
        }
    }
