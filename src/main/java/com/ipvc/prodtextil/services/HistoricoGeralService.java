package com.ipvc.prodtextil.services;

import com.ipvc.prodtextil.dto.HistoricoGeralDTO.*;
import com.ipvc.prodtextil.models.HistoricoGeral;
import com.ipvc.prodtextil.models.TiposEvento;
import com.ipvc.prodtextil.repos.HistoricoGeralRepo;
import com.ipvc.prodtextil.repos.TiposEventosRepo;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoricoGeralService {
    private final HistoricoGeralRepo historicoGeralRepo;
    private final TiposEventosRepo tiposEventoRepo;

    public HistoricoGeralService(HistoricoGeralRepo historicoGeralRepo, TiposEventosRepo tiposEventoRepo) {
        this.historicoGeralRepo = historicoGeralRepo;
        this.tiposEventoRepo = tiposEventoRepo;
    }

    public List<HistoricoGeralResponseDTO> getAllHistorico() {
        return historicoGeralRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<HistoricoGeralResponseDTO> getHistoricoById(Integer id) {
        return historicoGeralRepo.findById(id).map(this::convertToDTO);
    }

    public HistoricoGeralResponseDTO saveHistorico(HistoricoGeralCreateDTO historicoDTO) {
        TiposEvento tipoEvento = tiposEventoRepo.findById(historicoDTO.tipoEventoId())
                .orElseThrow(() -> new RuntimeException("Tipo de evento não encontrado"));

        HistoricoGeral historico = new HistoricoGeral();
        historico.setTipoEvento(tipoEvento);
        historico.setDescricao(historicoDTO.descricao());
        historico.setDataEvento(historicoDTO.dataEvento() != null ? historicoDTO.dataEvento() : Instant.now());

        return convertToDTO(historicoGeralRepo.save(historico));
    }

    public Optional<HistoricoGeralResponseDTO> updateHistorico(Integer id, HistoricoGeralUpdateDTO historicoDTO) {
        return historicoGeralRepo.findById(id).map(historico -> {
            historico.setDescricao(historicoDTO.descricao());
            historico.setDataEvento(historicoDTO.dataEvento());
            return convertToDTO(historicoGeralRepo.save(historico));
        });
    }

    public void deleteHistorico(Integer id) {
        historicoGeralRepo.deleteById(id);
    }

    private HistoricoGeralResponseDTO convertToDTO(HistoricoGeral historico) {
        return new HistoricoGeralResponseDTO(
                historico.getId(),
                historico.getTipoEvento().getId(),
                historico.getDescricao(),
                historico.getDataEvento()
        );
    }
}