package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.PagamentosFornecedores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagamentosFornecedoresRepo extends JpaRepository<PagamentosFornecedores, Integer> {
    List<PagamentosFornecedores> findByEncomenda_Id(Integer encomendaId);
    List<PagamentosFornecedores> findByDataPagamentoBetween(LocalDate inicio, LocalDate fim);
}
