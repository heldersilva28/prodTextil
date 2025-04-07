package com.ipvc.bll.controllers;

import com.ipvc.bll.dto.FuncionarioDTO;
import com.ipvc.bll.models.Funcionario;
import com.ipvc.bll.services.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/funcionarios")
@Tag(name = "Funcionarios", description = "Gestão de funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os funcionarios", description = "Retorna todos os funcionarios")
    public ResponseEntity<List<FuncionarioDTO.FuncionarioResponseDTO>> getAllClientesDTO() {
        List<Funcionario> funcionarios = funcionarioService.getAllClientes();
        List<FuncionarioDTO.FuncionarioResponseDTO> response = funcionarios.stream()
                .map(funcionario -> new FuncionarioDTO.FuncionarioResponseDTO(
                        funcionario.getId(),
                        funcionario.getNome(),
                        funcionario.getTelefone(),
                        funcionario.getTipoUtilizadorId(),
                        funcionario.getDataAdmissao()// Acesse o campo correto do código postal
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }



    @GetMapping("/{id}")
    @Operation(summary = "Buscar funcionario por ID", description = "Retorna um funcionario específico pelo ID")
    public ResponseEntity<FuncionarioDTO.FuncionarioResponseDTO> getFuncionarioById(@PathVariable Integer id) {
        Optional<FuncionarioDTO.FuncionarioResponseDTO> funcionario = funcionarioService.getFuncionarioById(id);
        return funcionario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo funcionario", description = "Adiciona um novo funcionario ao sistema")
    public ResponseEntity<FuncionarioDTO.FuncionarioResponseDTO> createFuncionario(@RequestBody FuncionarioDTO.FuncionarioCreateDTO funcionarioCreateDTO) {
        FuncionarioDTO.FuncionarioResponseDTO savedCliente = funcionarioService.saveFuncionario(funcionarioCreateDTO);
        return ResponseEntity.ok(savedCliente);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar funcionario", description = "Atualiza os dados de um funcionario existente")
    public ResponseEntity<FuncionarioDTO.FuncionarioResponseDTO> updateFuncionario(@PathVariable Integer id, @RequestBody FuncionarioDTO.FuncionarioUpdateDTO funcionarioUpdateDTO) {
        Optional<FuncionarioDTO.FuncionarioResponseDTO> updatedCliente = funcionarioService.updateFuncionario(id, funcionarioUpdateDTO);
        return updatedCliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar funcionario", description = "Remove um funcionario do sistema")
    public ResponseEntity<Void> deleteFuncionario(@PathVariable Integer id) {
        if (funcionarioService.getFuncionarioById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        funcionarioService.deleteFuncionario(id);
        return ResponseEntity.noContent().build();
    }
}
