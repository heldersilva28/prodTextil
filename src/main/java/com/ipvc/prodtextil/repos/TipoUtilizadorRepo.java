package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.Cliente;
import com.ipvc.prodtextil.models.TiposUtilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoUtilizadorRepo extends JpaRepository<TiposUtilizador, Integer> {

    // Buscar cliente por ID
    Optional<TiposUtilizador> findById(Integer id);
}
