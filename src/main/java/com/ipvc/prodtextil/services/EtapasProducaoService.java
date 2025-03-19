package com.ipvc.prodtextil.services;

import com.ipvc.prodtextil.dto.EtapasProducaoDTO.*;
import com.ipvc.prodtextil.models.EtapasProducao;
import com.ipvc.prodtextil.models.TarefasProducao;
import com.ipvc.prodtextil.models.TiposEtapasProducao;
import com.ipvc.prodtextil.repos.EtapasProducaoRepo;
import com.ipvc.prodtextil.repos.TarefasProducaoRepo;
import com.ipvc.prodtextil.repos.TiposEtapasProducaoRepo;
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

    public EtapasProducaoService(EtapasProducaoRepo etapasProducaoRepo, TarefasProducaoRepo tarefasProducaoRepo, TiposEtapasProducaoRepo tiposEtapasProducaoRepo) {
        this.etapasProducaoRepo = etapasProducaoRepo;
        this.tarefasProducaoRepo = tarefasProducaoRepo;
        this.tiposEtapasProducaoRepo = tiposEtapasProducaoRepo;
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
            return convertToDTO(etapasProducaoRepo.save(etapa));
        });
    }

    public void deleteEtapa(Integer id) {
        etapasProducaoRepo.deleteById(id);
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
}
