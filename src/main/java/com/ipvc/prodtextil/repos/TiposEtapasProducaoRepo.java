package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.TarefasProducao;
import com.ipvc.prodtextil.models.TiposEtapasProducao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TiposEtapasProducaoRepo extends JpaRepository<TiposEtapasProducao, Integer> {

}
