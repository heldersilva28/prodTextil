package com.ipvc.bll.repos;

import com.ipvc.bll.models.TiposEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiposEventosRepo extends JpaRepository<TiposEvento, Integer> {
    TiposEvento findByNomeIgnoreCase(String nome);
}
