package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.ConsumoMateriais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsumoMateriaisRepo extends JpaRepository<ConsumoMateriais, Integer> {
    List<ConsumoMateriais> findByMaterial_Id(Integer materialId);
    List<ConsumoMateriais> findByDataRegistoBetween(LocalDateTime inicio, LocalDateTime fim);
}
