package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.Utilizadore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UtilizadorRepo extends JpaRepository<Utilizadore, Integer> {
    List<Utilizadore> findByTipoUtilizadorContainingIgnoreCase(String tipo);
}
