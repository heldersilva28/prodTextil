package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.PagamentosFornecedore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagamentosFornecedoresRepo extends JpaRepository<PagamentosFornecedore, Integer> {
    List<PagamentosFornecedore> findByEncomenda_Id(Integer encomendaId);
    List<PagamentosFornecedore> findByDataPagamentoBetween(LocalDate inicio, LocalDate fim);
}
