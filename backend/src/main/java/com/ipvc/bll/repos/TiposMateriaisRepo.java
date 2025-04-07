package com.ipvc.bll.repos;

import com.ipvc.bll.models.TiposMateriais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiposMateriaisRepo extends JpaRepository<TiposMateriais, Integer> {
    TiposMateriais findByNomeIgnoreCase(String nome);
}
