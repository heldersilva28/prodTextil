package com.ipvc.prodtextil.services;

import com.ipvc.prodtextil.dto.FuncionarioDTO;
import com.ipvc.prodtextil.models.Funcionario;
import com.ipvc.prodtextil.repos.FuncionarioRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {
    private final FuncionarioRepo funcionarioRepo;

    public List<Funcionario> getAllClientes() {
        return funcionarioRepo.findAll();  // Retorna uma lista de clientes
    }

    public FuncionarioService(FuncionarioRepo funcionarioRepo) {
        this.funcionarioRepo = funcionarioRepo;
    }

    public List<FuncionarioDTO.FuncionarioResponseDTO> getAllFuncionarios() {
        return funcionarioRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<FuncionarioDTO.FuncionarioResponseDTO> getFuncionarioById(Integer id) {
        return funcionarioRepo.findById(id).map(this::convertToDTO);
    }

    public FuncionarioDTO.FuncionarioResponseDTO saveFuncionario(FuncionarioDTO.FuncionarioCreateDTO funcionarioDTO) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(funcionarioDTO.nome());
        funcionario.setEmail(funcionarioDTO.email());
        funcionario.setTelefone(funcionarioDTO.telefone());
        funcionario.setCargo(funcionarioDTO.cargo());
        funcionario.setDataAdmissao(funcionarioDTO.dataAdmissao());

        return convertToDTO(funcionarioRepo.save(funcionario));
    }

    public Optional<FuncionarioDTO.FuncionarioResponseDTO> updateFuncionario(Integer id, FuncionarioDTO.FuncionarioUpdateDTO funcionarioDTO) {
        return funcionarioRepo.findById(id).map(funcionario -> {
            funcionario.setNome(funcionarioDTO.nome());
            funcionario.setTelefone(funcionarioDTO.telefone());
            funcionario.setCargo(funcionarioDTO.cargo());
            return convertToDTO(funcionarioRepo.save(funcionario));
        });
    }

    public void deleteFuncionario(Integer id) {
        funcionarioRepo.deleteById(id);
    }

    private FuncionarioDTO.FuncionarioResponseDTO convertToDTO(Funcionario funcionario) {
        return new FuncionarioDTO.FuncionarioResponseDTO(
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.getEmail(),
                funcionario.getTelefone(),
                funcionario.getCargo(),
                funcionario.getDataAdmissao()
        );
    }
}
