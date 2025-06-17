package com.ipvc.bll.repos;

import com.ipvc.bll.models.TarefasProducao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TarefasProducaoRepo extends JpaRepository<TarefasProducao, Integer> {
    List<TarefasProducao> findByFuncionario_Id(Integer funcionarioId);
    List<TarefasProducao> findByEstadoIgnoreCase(String estado);
    List<TarefasProducao> findByEncomenda_Id(Integer encomendaId);
}
