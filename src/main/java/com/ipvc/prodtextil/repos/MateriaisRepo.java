package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.Materiai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MateriaisRepo extends JpaRepository<Materiai, Integer> {
    List<Materiai> findByNomeContainingIgnoreCase(String nome);
    List<Materiai> findByTipo_Id(Integer tipoId);
    List<Materiai> findByStockDisponivelGreaterThan(BigDecimal quantidade);
}
