package com.ipvc.bll.services;

import com.ipvc.bll.dto.EncomendasClienteDTO;
import com.ipvc.bll.dto.EtapasProducaoDTO.*;
import com.ipvc.bll.dto.TarefasProducaoDTO;
import com.ipvc.bll.models.EncomendasCliente;
import com.ipvc.bll.models.EtapasProducao;
import com.ipvc.bll.models.TarefasProducao;
import com.ipvc.bll.models.TiposEtapasProducao;
import com.ipvc.bll.repos.EtapasProducaoRepo;
import com.ipvc.bll.repos.TarefasProducaoRepo;
import com.ipvc.bll.repos.TiposEtapasProducaoRepo;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EtapasProducaoService {
    private final EtapasProducaoRepo etapasProducaoRepo;
    private final TarefasProducaoRepo tarefasProducaoRepo;
    private final TiposEtapasProducaoRepo tiposEtapasProducaoRepo;
    EncomendasClienteService encomendasClienteService;
    TarefasProducaoService tarefasProducaoService;

    public EtapasProducaoService(EtapasProducaoRepo etapasProducaoRepo, TarefasProducaoRepo tarefasProducaoRepo, TiposEtapasProducaoRepo tiposEtapasProducaoRepo, EncomendasClienteService encomendasClienteService, TarefasProducaoService tarefasProducaoService) {
        this.etapasProducaoRepo = etapasProducaoRepo;
        this.tarefasProducaoRepo = tarefasProducaoRepo;
        this.tiposEtapasProducaoRepo = tiposEtapasProducaoRepo;
        this.encomendasClienteService = encomendasClienteService;
        this.tarefasProducaoService = tarefasProducaoService;
    }

    public List<EtapaProducaoResponseDTO> getAllEtapas() {
        return etapasProducaoRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<EtapaProducaoResponseDTO> getEtapaById(Integer id) {
        return etapasProducaoRepo.findById(id).map(this::convertToDTO);
    }

    public EtapaProducaoResponseDTO saveEtapa(EtapaProducaoCreateDTO etapaDTO) {
        TarefasProducao tarefa = tarefasProducaoRepo.findById(etapaDTO.tarefaId())
                .orElseThrow(() -> new RuntimeException("Tarefa de produção não encontrada"));

        TiposEtapasProducao tipoEtapa = tiposEtapasProducaoRepo.findById(etapaDTO.tipoEtapaId())
                .orElseThrow(() -> new RuntimeException("Tipo de etapa de produção não encontrado"));

        tiposEtapasProducaoRepo.findById(etapaDTO.tipoEtapaId());


        EtapasProducao etapa = new EtapasProducao();
        etapa.setTarefa(tarefa);
        etapa.setTipoEtapa(tipoEtapa);
        etapa.setDataInicio(etapaDTO.dataInicio());
        etapa.setDataFim(etapaDTO.dataFim());

        return convertToDTO(etapasProducaoRepo.save(etapa));
    }

    public Optional<EtapaProducaoResponseDTO> updateEtapa(Integer id, EtapaProducaoUpdateDTO etapaDTO) {
        return etapasProducaoRepo.findById(id).map(etapa -> {
            etapa.setDataInicio(etapaDTO.dataInicio());
            etapa.setDataFim(etapaDTO.dataFim());
            Optional<TiposEtapasProducao> tipo = tiposEtapasProducaoRepo.findById(3);
            TiposEtapasProducao tipoEtapa = tipo.get();
            if(etapa.getTipoEtapa() == tipoEtapa){
                TarefasProducao tarefa = etapa.getTarefa();
                int idEncomenda = tarefa.getEncomenda().getId();
                EncomendasCliente encomenda = tarefa.getEncomenda();
                EncomendasClienteDTO.EncomendaClienteUpdateDTO encomendaDto = new EncomendasClienteDTO.EncomendaClienteUpdateDTO(
                        encomenda.getDataEncomenda(),2,
                        encomenda.getValorTotal()
                );
                TarefasProducaoDTO.TarefasProducaoUpdateDTO tarefaDto = new TarefasProducaoDTO.TarefasProducaoUpdateDTO(
                        tarefa.getTipoEvento().getId(),
                        tarefa.getFuncionario().getId(),
                        tarefa.getDataInicio(),
                        etapa.getDataFim(),
                        "CONCLUIDO"
                );
                encomendasClienteService.updateEncomenda(idEncomenda,encomendaDto);
                tarefasProducaoService.updateTarefa(etapa.getTarefa().getId(),tarefaDto);

            }
            return convertToDTO(etapasProducaoRepo.save(etapa));
        });
    }

    public void deleteEtapa(Integer id) {
        etapasProducaoRepo.deleteById(id);
    }

    public List<EtapaProducaoResponseDTO2> getEtapasByTarefaId(Integer tarefaId) {
        return etapasProducaoRepo.findByTarefa_Id(tarefaId).stream()
                .map(this::convertToDTO2)
                .collect(Collectors.toList());
    }

    private EtapaProducaoResponseDTO convertToDTO(EtapasProducao etapa) {
        return new EtapaProducaoResponseDTO(
                etapa.getId(),
                etapa.getTarefa().getId(),
                etapa.getTipoEtapa().getId(),
                etapa.getDataInicio(),
                etapa.getDataFim()
        );
    }

    private EtapaProducaoResponseDTO2 convertToDTO2(EtapasProducao etapa) {
        return new EtapaProducaoResponseDTO2(
                etapa.getId(),
                etapa.getTarefa().getId(),
                etapa.getTipoEtapa().getDescricao(),
                etapa.getDataInicio(),
                etapa.getDataFim()
        );
    }
}
