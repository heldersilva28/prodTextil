package com.ipvc.bll.repos;

import com.ipvc.bll.models.PagamentosFornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagamentosFornecedoresRepo extends JpaRepository<PagamentosFornecedor, Integer> {
    List<PagamentosFornecedor> findByEncomenda_Id(Integer encomendaId);
    List<PagamentosFornecedor> findByDataPagamentoBetween(LocalDate inicio, LocalDate fim);
}
