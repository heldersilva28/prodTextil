package com.ipvc.bll.services;

import com.ipvc.bll.dto.TarefasProducaoDTO.*;
import com.ipvc.bll.models.TarefasProducao;
import com.ipvc.bll.models.EncomendasCliente;
import com.ipvc.bll.models.Funcionario;
import com.ipvc.bll.models.TiposEvento;
import com.ipvc.bll.repos.TarefasProducaoRepo;
import com.ipvc.bll.repos.EncomendaClienteRepo;
import com.ipvc.bll.repos.FuncionarioRepo;
import com.ipvc.bll.repos.TiposEventosRepo;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TarefasProducaoService {
    private final TarefasProducaoRepo tarefasProducaoRepo;
    private final EncomendaClienteRepo encomendasClienteRepo;
    private final FuncionarioRepo funcionarioRepo;
    private final TiposEventosRepo tiposEventoRepo;

    public TarefasProducaoService(TarefasProducaoRepo tarefasProducaoRepo, EncomendaClienteRepo encomendasClienteRepo, FuncionarioRepo funcionarioRepo, TiposEventosRepo tiposEventoRepo) {
        this.tarefasProducaoRepo = tarefasProducaoRepo;
        this.encomendasClienteRepo = encomendasClienteRepo;
        this.funcionarioRepo = funcionarioRepo;
        this.tiposEventoRepo = tiposEventoRepo;
    }

    public List<TarefasProducaoResponseDTO> getAllTarefas() {
        return tarefasProducaoRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<TarefasProducaoResponseDTO> getTarefaById(Integer id) {
        return tarefasProducaoRepo.findById(id).map(this::convertToDTO);
    }

    public TarefasProducaoResponseDTO saveTarefa(TarefasProducaoCreateDTO tarefaDTO) {
        EncomendasCliente encomenda = encomendasClienteRepo.findById(tarefaDTO.encomendaId())
                .orElseThrow(() -> new RuntimeException("Encomenda não encontrada"));

        Funcionario funcionario = funcionarioRepo.findById(tarefaDTO.funcionarioId())
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        TiposEvento tipoEvento = tiposEventoRepo.findById(Integer.parseInt(tarefaDTO.descricao()))
                .orElseThrow(() -> new RuntimeException("Tipo de Evento não encontrado"));

        TarefasProducao tarefa = new TarefasProducao();
        tarefa.setEncomenda(encomenda);
        tarefa.setFuncionario(funcionario);
        tarefa.setTipoEvento(tipoEvento);
        tarefa.setDataInicio(tarefaDTO.dataInicio());
        tarefa.setEstado(tarefaDTO.estado());

        return convertToDTO(tarefasProducaoRepo.save(tarefa));
    }

    public Optional<TarefasProducaoResponseDTO> updateTarefa(Integer id, TarefasProducaoUpdateDTO tarefaDTO) {
        return tarefasProducaoRepo.findById(id).map(tarefa -> {
            Funcionario funcionario = funcionarioRepo.findById(tarefaDTO.funcionarioId())
                    .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

            TiposEvento tipoEvento = tiposEventoRepo.findById(Integer.parseInt(tarefaDTO.descricao()))
                    .orElseThrow(() -> new RuntimeException("Tipo de Evento não encontrado"));

            tarefa.setFuncionario(funcionario);
            tarefa.setTipoEvento(tipoEvento);
            tarefa.setDataInicio(tarefaDTO.dataInicio());
            tarefa.setDataFim(tarefaDTO.dataFim());
            tarefa.setEstado(tarefaDTO.estado());

            return convertToDTO(tarefasProducaoRepo.save(tarefa));
        });
    }

    public void deleteTarefa(Integer id) {
        tarefasProducaoRepo.deleteById(id);
    }

    private TarefasProducaoResponseDTO convertToDTO(TarefasProducao tarefa) {
        return new TarefasProducaoResponseDTO(
                tarefa.getId(),
                tarefa.getEncomenda().getId(),
                String.valueOf(tarefa.getTipoEvento().getId()),
                tarefa.getFuncionario().getId(),
                tarefa.getDataInicio(),
                tarefa.getDataFim(),
                tarefa.getEstado()
        );
    }
}