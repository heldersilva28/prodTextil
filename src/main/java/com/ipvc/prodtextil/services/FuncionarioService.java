package com.ipvc.prodtextil.services;

import com.ipvc.prodtextil.dto.ClienteDTO;
import com.ipvc.prodtextil.dto.FuncionarioDTO;
import com.ipvc.prodtextil.models.CodigosPostais;
import com.ipvc.prodtextil.models.Funcionario;
import com.ipvc.prodtextil.models.TiposUtilizador;
import com.ipvc.prodtextil.models.Utilizador;
import com.ipvc.prodtextil.repos.FuncionarioRepo;
import com.ipvc.prodtextil.repos.TipoUtilizadorRepo;
import com.ipvc.prodtextil.repos.UtilizadorRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {
    private final FuncionarioRepo funcionarioRepo;
    private final UtilizadorRepo utilizadorRepo;
    private final TipoUtilizadorRepo tipoUtilizadorRepo;

    public List<Funcionario> getAllClientes() {
        return funcionarioRepo.findAll();  // Retorna uma lista de clientes
    }

    public FuncionarioService(FuncionarioRepo funcionarioRepo, UtilizadorRepo utilizadorRepo, TipoUtilizadorRepo tipoUtilizadorRepo) {
        this.funcionarioRepo = funcionarioRepo;
        this.utilizadorRepo = utilizadorRepo;
        this.tipoUtilizadorRepo = tipoUtilizadorRepo;
    }

    public List<FuncionarioDTO.FuncionarioResponseDTO> getAllFuncionarios() {
        return funcionarioRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<FuncionarioDTO.FuncionarioResponseDTO> getFuncionarioById(Integer id) {
        return funcionarioRepo.findById(id).map(this::convertToDTO);
    }

    public FuncionarioDTO.FuncionarioResponseDTO saveFuncionario(FuncionarioDTO.FuncionarioCreateDTO funcionarioDTO) {
        // Carregar o utilizador que será associado ao funcionário, baseado no ID
        Utilizador utilizador = utilizadorRepo.findById(funcionarioDTO.utilizadorId())
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        Utilizador cargo = utilizadorRepo.findCargoUtilizadorById(utilizador.getId());

        // Criar o novo funcionário
        Funcionario funcionario = new Funcionario();
        funcionario.setUtilizador(utilizador);
        funcionario.setCargo(cargo);
        funcionario.setTelefone(funcionarioDTO.telefone());
        funcionario.setDataAdmissao(funcionarioDTO.dataAdmissao());

        // Salvar o funcionário no banco de dados
        Funcionario savedFuncionario = funcionarioRepo.save(funcionario);

        return convertToDTO(savedFuncionario);
    }


    public Optional<FuncionarioDTO.FuncionarioResponseDTO> updateFuncionario(Integer id, FuncionarioDTO.FuncionarioUpdateDTO funcionarioDTO) {
        return funcionarioRepo.findById(id).map(funcionario -> {
            // Atualizar o utilizador (se necessário)
                Utilizador utilizador = utilizadorRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));
                funcionario.setUtilizador(utilizador);

                Utilizador cargo = utilizadorRepo.findById(funcionarioDTO.cargo())
                        .orElseThrow(() -> new RuntimeException("Cargo (Utilizador) não encontrado"));
                funcionario.setCargo(cargo);


            // Atualizar os outros campos
            if (funcionarioDTO.telefone() != null) {
                funcionario.setTelefone(funcionarioDTO.telefone());
            }
            if (funcionarioDTO.dataAdmissao() != null) {
                funcionario.setDataAdmissao(funcionarioDTO.dataAdmissao());
            }

            // Salvar as alterações
            Funcionario updatedFuncionario = funcionarioRepo.save(funcionario);

            return convertToDTO(updatedFuncionario);
        });
    }



    public void deleteFuncionario(Integer id) {
        funcionarioRepo.deleteById(id);
    }

    private FuncionarioDTO.FuncionarioResponseDTO convertToDTO(Funcionario funcionario) {
        return new FuncionarioDTO.FuncionarioResponseDTO(
                funcionario.getId(),
                funcionario.getUtilizador().getUsername(),
                funcionario.getTelefone(),
                funcionario.getTipoUtilizadorId(),
                funcionario.getDataAdmissao()
        );
    }

}
