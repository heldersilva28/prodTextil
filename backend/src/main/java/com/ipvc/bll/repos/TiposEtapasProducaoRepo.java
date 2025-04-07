package com.ipvc.bll.repos;

import com.ipvc.bll.models.TarefasProducao;
import com.ipvc.bll.models.TiposEtapasProducao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TiposEtapasProducaoRepo extends JpaRepository<TiposEtapasProducao, Integer> {

}
