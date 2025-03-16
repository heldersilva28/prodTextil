package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.MetodosPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetodosPagamentoRepo extends JpaRepository<MetodosPagamento, Integer> {
    MetodosPagamento findByNomeIgnoreCase(String nome);
}
