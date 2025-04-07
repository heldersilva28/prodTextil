package com.ipvc.bll.services;

import com.ipvc.bll.dto.ClienteDTO;
import com.ipvc.bll.dto.FuncionarioDTO;
import com.ipvc.bll.models.CodigosPostais;
import com.ipvc.bll.models.Funcionario;
import com.ipvc.bll.models.TiposUtilizador;
import com.ipvc.bll.models.Utilizador;
import com.ipvc.bll.repos.FuncionarioRepo;
import com.ipvc.bll.repos.TipoUtilizadorRepo;
import com.ipvc.bll.repos.UtilizadorRepo;
import jakarta.transaction.Transactional;
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




    @Transactional  // Garante que todas as operações aconteçam dentro de uma única transação
    public Optional<FuncionarioDTO.FuncionarioResponseDTO> updateFuncionario(Integer utilizadorId, FuncionarioDTO.FuncionarioUpdateDTO funcionarioDTO) {
        System.out.println("🔍 Buscando funcionário para o utilizador com ID: " + utilizadorId);

        return funcionarioRepo.findByUtilizador_Id(utilizadorId).map(funcionario -> {
            System.out.println("✅ Funcionário encontrado: " + funcionario.getId());

            // Buscar o novo cargo corretamente (Utilizador)
            Utilizador novoCargo = utilizadorRepo.findById(funcionarioDTO.cargo())
                    .orElseThrow(() -> new RuntimeException("❌ Cargo (Utilizador) não encontrado"));

            // Verificar se o funcionário já tem um cargo associado
            if (funcionario.getCargo() != null) {
                System.out.println("🔄 Cargo antes: " + funcionario.getCargo().getId());
            } else {
                System.out.println("⚠ Cargo atual é NULL. Será atualizado agora.");
            }

            // Atualizar cargo do funcionário corretamente
            funcionario.setCargo(novoCargo);
            System.out.println("✅ Cargo atualizado para: " + novoCargo.getId());

            // Atualizar telefone, se necessário
            if (funcionarioDTO.telefone() != null) {
                funcionario.setTelefone(funcionarioDTO.telefone());
            }

            // Atualizar tipo_utilizador_id na tabela Utilizador
            Utilizador utilizador = funcionario.getUtilizador();
            utilizador.setTipoUtilizador(novoCargo.getTipoUtilizador());
            System.out.println("✅ Tipo de Utilizador atualizado para: " + novoCargo.getTipoUtilizador().getId());

            // ** Evitar problemas de sessão do Hibernate **
            utilizadorRepo.save(utilizador);  // Atualiza primeiro o `Utilizador`
            funcionarioRepo.saveAndFlush(funcionario);  // Depois salva `Funcionario`, garantindo consistência

            System.out.println("✅ Funcionário e Utilizador atualizados com sucesso!");

            return convertToDTO(funcionario);
        });
    }









    public Optional<FuncionarioDTO.FuncionarioResponseDTO> updateFuncionarioCargo(Integer utilizadorId, Integer novoCargoId) {
        return funcionarioRepo.findByUtilizador_Id(utilizadorId).map(funcionario -> {
            System.out.println("✅ Funcionário encontrado para Utilizador ID: " + utilizadorId);

            // Buscar o novo cargo correto a partir do Utilizador
            Utilizador novoCargo = utilizadorRepo.findCargoUtilizadorById(utilizadorId);

            if(novoCargo == null) {
                throw new RuntimeException("❌ Cargo (Utilizador) não encontrado para este utilizador");
            }
            // Verificar se o funcionário já tem um cargo antes da alteração
            if (funcionario.getCargo() != null) {
                System.out.println("🔄 Cargo antes: " + funcionario.getCargo().getId());
            } else {
                System.out.println("⚠ Funcionário sem cargo definido.");
            }

            // Atualizar o cargo do funcionário
            funcionario.setCargo(novoCargo);

            // Salvar a atualização no banco de dados
            Funcionario funcionarioAtualizado = funcionarioRepo.save(funcionario);

            // Exibir o cargo após a atualização
            System.out.println("✅ Cargo atualizado para: " + funcionarioAtualizado.getCargo().getId());

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
