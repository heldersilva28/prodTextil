package com.ipvc.bll.repos;

import com.ipvc.bll.models.MetodosPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetodosPagamentoRepo extends JpaRepository<MetodosPagamento, Integer> {
    MetodosPagamento findByNomeIgnoreCase(String nome);
}
