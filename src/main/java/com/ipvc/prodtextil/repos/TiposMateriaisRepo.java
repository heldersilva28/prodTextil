package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.TiposMateriai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiposMateriaisRepo extends JpaRepository<TiposMateriai, Integer> {
    TiposMateriai findByNomeIgnoreCase(String nome);
}
