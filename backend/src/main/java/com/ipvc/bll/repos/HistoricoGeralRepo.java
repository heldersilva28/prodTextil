package com.ipvc.bll.repos;

import com.ipvc.bll.models.HistoricoGeral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoricoGeralRepo extends JpaRepository<HistoricoGeral, Integer> {
    List<HistoricoGeral> findByTipoEvento_Id(Integer tipoEventoId);
    List<HistoricoGeral> findByDataEventoBetween(LocalDateTime inicio, LocalDateTime fim);
}
