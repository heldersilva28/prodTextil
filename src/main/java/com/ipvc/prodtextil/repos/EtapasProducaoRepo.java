package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.EtapasProducao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EtapasProducaoRepo extends JpaRepository<EtapasProducao, Integer> {
    List<EtapasProducao> findByTarefa_Id(Integer tarefaId);
}
