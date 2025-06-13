package com.ipvc.bll.controllers;

import com.ipvc.bll.dto.*;
import com.ipvc.bll.models.Cliente;
import com.ipvc.bll.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Gestão de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os clientes", description = "Retorna todos os clientes")
    public ResponseEntity<List<ClienteDTO.ClienteResponseDTO>> getAllClientesDTO() {
        List<Cliente> clientes = clienteService.getAllClientes();
        List<ClienteDTO.ClienteResponseDTO> response = clientes.stream()
                .map(cliente -> new ClienteDTO.ClienteResponseDTO(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getEmail(),
                        cliente.getMorada(),
                        cliente.getTelefone(),
                        cliente.getCodigoPostal().getCodigo() // Acesse o campo correto do código postal
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/emails")
    @Operation(summary = "Listar todos os emails de clientes", description = "Retorna todos os emails de clientes")
    public ResponseEntity<Map<Integer,String>> getAllEmailClientes() {
        List<Cliente> clientes = clienteService.getAllClientes();
        Map<Integer,String> emails = new HashMap<>();
        for (Cliente cliente : clientes) {
            emails.put(cliente.getId(), cliente.getEmail());
        }
        return ResponseEntity.ok(emails);
    }



    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente específico pelo ID")
    public ResponseEntity<ClienteDTO.ClienteResponseDTO> getClienteById(@PathVariable Integer id) {
        Optional<ClienteDTO.ClienteResponseDTO> cliente = clienteService.getClienteById(id);
        return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo cliente", description = "Adiciona um novo cliente ao sistema")
    public ResponseEntity<AuthResponse> createCliente(@RequestBody ClienteDTO.ClienteCreateDTO clienteDTO) {
        ClienteDTO.ClienteResponseDTO savedCliente = clienteService.saveCliente(clienteDTO);
        if (savedCliente == null) {
            return ResponseEntity.badRequest().body(new AuthResponse("Erro ao criar cliente", false));
        }
        return ResponseEntity.ok(new AuthResponse("Cliente criado com sucesso",true));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
    public ResponseEntity<ClienteDTO.ClienteResponseDTO> updateCliente(@PathVariable Integer id, @RequestBody ClienteDTO.ClienteUpdateDTO clienteDTO) {
        Optional<ClienteDTO.ClienteResponseDTO> updatedCliente = clienteService.updateCliente(id, clienteDTO);
        return updatedCliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cliente", description = "Remove um cliente do sistema")
    public ResponseEntity<Void> deleteCliente(@PathVariable Integer id) {
        if (clienteService.getClienteById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<ClientesStatsDTO> obterEstatisticas() {
        return ResponseEntity.ok(clienteService.obterEstatisticasClientes());
    }

    @GetMapping("/clientes/id")
    public int getIdClienteByEmail(@RequestParam String email) {
        return clienteService.getIdClienteByEmail(email);
    }
}
