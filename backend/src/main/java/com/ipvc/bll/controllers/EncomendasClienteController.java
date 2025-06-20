package com.ipvc.bll.controllers;

import com.ipvc.bll.dto.EncomendasClienteDTO.*;
import com.ipvc.bll.dto.EncomendasClientesStatsDTO;
import com.ipvc.bll.services.EncomendasClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/encomendas-clientes")
@Tag(name = "Encomendas de Clientes", description = "Gestão de encomendas feitas pelos clientes")
public class EncomendasClienteController {

    private final EncomendasClienteService encomendasClienteService;

    public EncomendasClienteController(EncomendasClienteService encomendasClienteService) {
        this.encomendasClienteService = encomendasClienteService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as encomendas de clientes", description = "Retorna uma lista de todas as encomendas registradas")
    public ResponseEntity<List<EncomendaClienteResponseDTO>> getAllEncomendas() {
        return ResponseEntity.ok(encomendasClienteService.getAllEncomendas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma encomenda específica", description = "Retorna os detalhes de uma encomenda específica pelo ID")
    public ResponseEntity<EncomendaClienteResponseDTO> getEncomendaById(@PathVariable Integer id) {
        Optional<EncomendaClienteResponseDTO> encomenda = encomendasClienteService.getEncomendaById(id);
        return encomenda.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/encomendas-sem-tarefas")
    @Operation(summary = "Listar encomendas sem tarefas", description = "Retorna uma lista de encomendas que não possuem tarefas associadas")
    public ResponseEntity<List<EncomendaClienteResponseDTO>> getEncomendasSemTarefas() {
        List<EncomendaClienteResponseDTO> encomendas = encomendasClienteService.encomendaSemTarefas();
        if (encomendas.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(encomendas);
    }
    @GetMapping("/encomendas-sem-pagamento")
    @Operation(summary = "Listar encomendas sem tarefas", description = "Retorna uma lista de encomendas que não possuem tarefas associadas")
    public ResponseEntity<List<EncomendaClienteResponseDTO>> getEncomendasSemPagamento() {
        List<EncomendaClienteResponseDTO> encomendas = encomendasClienteService.encomendaSemPagamento();
        if (encomendas.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(encomendas);
    }

//    @PostMapping
//    @Operation(summary = "Criar uma nova encomenda", description = "Registra uma nova encomenda no sistema")
//    public ResponseEntity<EncomendaClienteResponseDTO> createEncomenda(@RequestBody EncomendaClienteCreateDTO encomendaDTO) {
//        EncomendaClienteResponseDTO novaEncomenda = encomendasClienteService.saveEncomenda(encomendaDTO);
//        return ResponseEntity.ok(novaEncomenda);
//    }

    @PostMapping
    @Operation(summary = "Criar uma nova encomenda", description = "Registra uma nova encomenda no sistema")
    public ResponseEntity<EncomendaClienteResponseDTO> createEncomenda(@RequestBody EncomendaClienteCreateFullDTO encomendaDTO) {
        EncomendaClienteResponseDTO novaEncomenda = encomendasClienteService.saveEncomendaFull(encomendaDTO);
        return ResponseEntity.ok(novaEncomenda);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma encomenda", description = "Atualiza os dados de uma encomenda já registrada")
    public ResponseEntity<EncomendaClienteResponseDTO> updateEncomenda(@PathVariable Integer id, @RequestBody EncomendaClienteUpdateDTO encomendaDTO) {
        Optional<EncomendaClienteResponseDTO> updatedEncomenda = encomendasClienteService.updateEncomenda(id, encomendaDTO);
        return updatedEncomenda.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma encomenda", description = "Remove uma encomenda do sistema")
    public ResponseEntity<Void> deleteEncomenda(@PathVariable Integer id) {
        encomendasClienteService.deleteEncomenda(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estatisticas")
    @Operation(summary = "Estatísticas de encomendas de clientes", description = "Retorna estatísticas gerais sobre as encomendas dos clientes")
    public ResponseEntity<EncomendasClientesStatsDTO> obterEstatisticas() {
        return ResponseEntity.ok(encomendasClienteService.obterEstatisticas());
    }

    @GetMapping("/{id}/estatisticas")
    @Operation(summary = "Estatísticas de encomendas de um cliente", description = "Retorna estatísticas específicas sobre as encomendas de um cliente pelo ID")
    public ResponseEntity<EncomendasClientesStatsDTO> obterEstatisticasPorCliente(@PathVariable Integer id) {
        return ResponseEntity.ok(encomendasClienteService.obterEstatisticasCliente(id));
    }

    @GetMapping("/funcionario/{id}")
    @Operation(summary = "Encomendas de associadas a funcionario", description = "Retorna encomendas específicas associadas a um funcionario pelo ID")
    public ResponseEntity<List<EncomendaClienteResponseDTO>> getEncomendasByFuncionarioId(@PathVariable Integer id) {
        return ResponseEntity.ok(encomendasClienteService.getEncomendasByFuncionarioId(id));
    }

    @GetMapping("/{id}/encomendas")
    @Operation(summary = "Buscar uma encomenda de um cliente", description = "Retorna os detalhes das encomendas pelo ID do Cliente")
    public ResponseEntity<List<EncomendaClienteFullResponseDTO>> obterEncomendas(@PathVariable Integer id) {
        List<EncomendaClienteFullResponseDTO> encomendas = encomendasClienteService.obterEncomendasClientePorId(id);
        if (encomendas.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(encomendas);
    }
}
