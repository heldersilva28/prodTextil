package com.ipvc.bll.repos;

import com.ipvc.bll.models.EstadosEncomenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadosEncomendaRepo extends JpaRepository<EstadosEncomenda, Integer> {
    EstadosEncomenda findByNomeIgnoreCase(String nome);
}
