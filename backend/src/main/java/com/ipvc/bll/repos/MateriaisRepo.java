package com.ipvc.bll.repos;

import com.ipvc.bll.models.Materiais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MateriaisRepo extends JpaRepository<Materiais, Integer> {
    List<Materiais> findByNomeContainingIgnoreCase(String nome);
    List<Materiais> findByStockDisponivelGreaterThan(BigDecimal quantidade);

    int countByStockDisponivelLessThanEqual(int limite);
    int countByTipo_Id(Integer tipoId);
    List<Materiais> findByTipo_Id(Integer id);

}
