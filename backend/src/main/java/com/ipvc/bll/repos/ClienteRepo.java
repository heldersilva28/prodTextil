package com.ipvc.bll.repos;

import com.ipvc.bll.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepo extends JpaRepository<Cliente, Integer> {

    // Buscar cliente por ID
    Optional<Cliente> findById(Integer id);

    // Buscar cliente por nome exato
    Optional<Cliente> findByNome(String nome);

    // Buscar clientes pelo código postal correto
    List<Cliente>findAllByCodigoPostal_Codigo(String codigoPostal);
}
