package com.ipvc.bll.repos;

import com.ipvc.bll.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepo extends JpaRepository<Cliente, Integer> {

    // Buscar cliente por ID
    Optional<Cliente> findById(Integer id);

    // Buscar cliente por nome exato
    Optional<Cliente> findByNome(String nome);

    @Query("SELECT C.id FROM Cliente C WHERE C.email = :email")
    int findIdByEmail(String email);

    // Buscar clientes pelo código postal correto
    List<Cliente>findAllByCodigoPostal_Codigo(String codigoPostal);


    // Contar clientes que têm pelo menos uma encomenda
    @Query("SELECT COUNT(DISTINCT c) FROM Cliente c JOIN c.encomendasClientes e")
    long countClientesComEncomendas();

    // Top 5 clientes com mais encomendas
    @Query("SELECT c.nome, COUNT(e) FROM Cliente c JOIN c.encomendasClientes e " +
            "GROUP BY c.id, c.nome ORDER BY COUNT(e) DESC")
    List<Object[]> topClientesPorEncomendas();
}

