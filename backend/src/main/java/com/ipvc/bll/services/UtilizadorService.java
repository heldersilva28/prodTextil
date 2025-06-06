package com.ipvc.bll.services;

import com.ipvc.bll.dto.ClienteDTO;
import com.ipvc.bll.dto.FuncionarioDTO;
import com.ipvc.bll.dto.UtilizadorDTO;
import com.ipvc.bll.dto.TiposUtilizadorDTO;
import com.ipvc.bll.models.Funcionario;
import com.ipvc.bll.models.TiposUtilizador;
import com.ipvc.bll.models.Utilizador;
import com.ipvc.bll.repos.FuncionarioRepo;
import com.ipvc.bll.repos.TipoUtilizadorRepo;
import com.ipvc.bll.repos.UtilizadorRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilizadorService {
    private final UtilizadorRepo utilizadorRepo;
    private final TipoUtilizadorRepo tiposUtilizadorRepo;
    private final FuncionarioService funcionarioService;
    private final ClienteService clienteService;

    public UtilizadorService(UtilizadorRepo utilizadorRepo, TipoUtilizadorRepo tiposUtilizadorRepo, FuncionarioService funcionarioService, ClienteService clienteService) {
        this.utilizadorRepo = utilizadorRepo;
        this.tiposUtilizadorRepo = tiposUtilizadorRepo;
        this.funcionarioService = funcionarioService;
        this.clienteService = clienteService;
    }

    public List<UtilizadorDTO.UtilizadorResponseDTO> getAllUtilizadores() {
        return utilizadorRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<UtilizadorDTO.UtilizadorResponseDTO> getAllUtilizadoresSemFuncionarioSemAdmin() {
        List<FuncionarioDTO.FuncionarioResponseDTO> funcionarios = funcionarioService.getAllFuncionarios();
        List<UtilizadorDTO.UtilizadorResponseDTO> u = getAllUtilizadores();
        // Filtrar os utilizadores que não são funcionários e não são administradores
        List<Integer> idsFuncionarios = new ArrayList<>();
        for (UtilizadorDTO.UtilizadorResponseDTO utilizador : u) {
            if (utilizador.tipoUtilizadorId() == 4) {
                idsFuncionarios.add(utilizador.id());
            }
        }
        for (FuncionarioDTO.FuncionarioResponseDTO funcionario : funcionarios) {
            idsFuncionarios.add(funcionario.utilizadorId());
        }

        return utilizadorRepo.findAll().stream()
                .filter(utilizador -> !idsFuncionarios.contains(utilizador.getId()) && utilizador.getTipoUtilizador().getId() != 1)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<UtilizadorDTO.UtilizadorResponseDTO> getUtilizadorById(Integer id) {
        return utilizadorRepo.findById(id).map(this::convertToDTO);
    }

    public Optional<UtilizadorDTO.UtilizadorResponseDTO> getUtilizadorByEmail(String email) {
        return utilizadorRepo.findByEmail(email).map(this::convertToDTO);
    }

    public int obterCargoPorEmail(String email) {
        Utilizador utilizador = utilizadorRepo.findCargoUtilizadorByEmail(email);
        if (utilizador != null && utilizador.getTipoUtilizador() != null) {
            return utilizador.getTipoUtilizador().getId();
        }
        return -1;
    }

    public UtilizadorDTO.UtilizadorResponseDTO saveUtilizador(UtilizadorDTO.UtilizadorCreateDTO utilizadorDTO) {
        // Verificar se já existe um utilizador com o mesmo username ou email
        if (utilizadorRepo.existsByUsername(utilizadorDTO.username())) {
            throw new RuntimeException("Já existe um utilizador com este username.");
        }
        if (utilizadorRepo.existsByEmail(utilizadorDTO.email())) {
            throw new RuntimeException("Já existe um utilizador com este email.");
        }

        TiposUtilizador tipoUtilizador = tiposUtilizadorRepo.findById(utilizadorDTO.tipoUtilizadorId())
                .orElseThrow(() -> new RuntimeException("Tipo de Utilizador não encontrado"));

        Utilizador utilizador = new Utilizador();
        utilizador.setUsername(utilizadorDTO.username());
        utilizador.setEmail(utilizadorDTO.email());
        utilizador.setHash(utilizadorDTO.hash());
        utilizador.setTipoUtilizador(tipoUtilizador);

        return convertToDTO(utilizadorRepo.save(utilizador));
    }


    public Optional<UtilizadorDTO.UtilizadorResponseDTO> updateUtilizador(Integer id, UtilizadorDTO.UtilizadorUpdateDTO utilizadorDTO) {
        return utilizadorRepo.findById(id).map(utilizador -> {
            TiposUtilizador tipoUtilizador = tiposUtilizadorRepo.findById(utilizadorDTO.tipoUtilizadorId())
                    .orElseThrow(() -> new RuntimeException("Tipo de Utilizador não encontrado"));

            utilizador.setTipoUtilizador(tipoUtilizador);
            var dto = convertToDTO(utilizadorRepo.save(utilizador));

            // Atualizar o cargo do funcionário, caso necessário
            funcionarioService.updateFuncionarioCargo(id, utilizadorDTO.tipoUtilizadorId());

            return dto;
        });
    }


    public void deleteUtilizador(Integer id) {
        utilizadorRepo.deleteById(id);
    }

    private UtilizadorDTO.UtilizadorResponseDTO convertToDTO(Utilizador utilizador) {
        return new UtilizadorDTO.UtilizadorResponseDTO(
                utilizador.getId(),
                utilizador.getUsername(),
                utilizador.getEmail(),
                utilizador.getTipoUtilizador().getId(),
                utilizador.getTipoUtilizadorNome()
        );
    }
}
