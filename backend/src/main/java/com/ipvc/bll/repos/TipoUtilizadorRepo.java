package com.ipvc.bll.repos;

import com.ipvc.bll.models.Cliente;
import com.ipvc.bll.models.TiposUtilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoUtilizadorRepo extends JpaRepository<TiposUtilizador, Integer> {

    // Buscar cliente por ID
    Optional<TiposUtilizador> findById(Integer id);
}
