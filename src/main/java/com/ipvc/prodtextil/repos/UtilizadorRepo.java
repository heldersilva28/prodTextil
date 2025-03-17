package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UtilizadorRepo extends JpaRepository<Utilizador, Integer> {
    List<Utilizador> findAllByTipoUtilizador_Nome(String nome);
}
