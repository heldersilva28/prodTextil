package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.TiposEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiposEventosRepo extends JpaRepository<TiposEvento, Integer> {
    TiposEvento findByNomeIgnoreCase(String nome);
}
