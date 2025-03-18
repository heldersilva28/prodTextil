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

import java.time.LocalDate;
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


    public Optional<FuncionarioDTO.FuncionarioResponseDTO> updateFuncionario(Integer utilizadorId, FuncionarioDTO.FuncionarioUpdateDTO funcionarioDTO) {
        System.out.println("🔍 Buscando funcionário para o utilizador com ID: " + utilizadorId);

        return funcionarioRepo.findFuncionariosByUtilizadorId(utilizadorId).map(funcionario -> {
            System.out.println("✅ Funcionário encontrado: " + funcionario.getId());

            // Buscar o novo cargo corretamente
            Utilizador novoCargo = utilizadorRepo.findById(funcionarioDTO.cargo())
                    .orElseThrow(() -> new RuntimeException("❌ Cargo (Utilizador) não encontrado"));

            // Atualizar cargo
            System.out.println("🔄 Cargo antes: " + funcionario.getCargo().getId());
            funcionario.setCargo(novoCargo);
            System.out.println("✅ Cargo atualizado para: " + novoCargo.getId());

            // Atualizar telefone, se necessário
            if (funcionarioDTO.telefone() != null) {
                funcionario.setTelefone(funcionarioDTO.telefone());
            }

            // Salvar as alterações
            Funcionario updatedFuncionario = funcionarioRepo.save(funcionario);
            System.out.println("✅ Funcionário atualizado com sucesso: " + updatedFuncionario.getId());

            return convertToDTO(updatedFuncionario);
        });
    }




    public Optional<FuncionarioDTO.FuncionarioResponseDTO> updateFuncionarioCargo(Integer utilizadorId, Integer novoCargoId) {
        return funcionarioRepo.findByUtilizadorId(utilizadorId).map(funcionario -> {
            // Verifica se o funcionário tem um cargo antes da alteração
            if (funcionario.getCargo() != null) {
                System.out.println("Cargo antes: " + funcionario.getCargo().getId());
            } else {
                System.out.println("Funcionário sem cargo definido.");
            }

            // Buscar o novo cargo do utilizador
            Utilizador novoCargo = utilizadorRepo.findById(novoCargoId)
                    .orElseThrow(() -> new RuntimeException("Cargo (Utilizador) não encontrado"));

            // Atualizar o cargo do funcionário
            funcionario.setCargo(novoCargo);

            // Salvar a atualização no banco de dados
            Funcionario funcionarioAtualizado = funcionarioRepo.save(funcionario);

            // Exibir o cargo após a atualização
            System.out.println("Cargo depois: " + funcionarioAtualizado.getCargo().getId());

            return convertToDTO(funcionarioAtualizado);
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
