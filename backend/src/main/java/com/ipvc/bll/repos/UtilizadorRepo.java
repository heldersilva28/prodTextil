package com.ipvc.bll.repos;

import com.ipvc.bll.models.Utilizador;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UtilizadorRepo extends JpaRepository<Utilizador, Integer> {
    List<Utilizador> findAllByTipoUtilizador_Nome(String nome);
    Utilizador findTipoUtilizadorById(Integer id);
    Utilizador findCargoUtilizadorById(Integer id);
    Utilizador findCargoUtilizadorByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Utilizador> findByEmail(String email);

}
