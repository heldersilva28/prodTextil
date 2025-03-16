package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.ConsumoMateriai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsumoMateriaisRepo extends JpaRepository<ConsumoMateriai, Integer> {
    List<ConsumoMateriai> findByMaterial_Id(Integer materialId);
    List<ConsumoMateriai> findByDataRegistoBetween(LocalDateTime inicio, LocalDateTime fim);
}
