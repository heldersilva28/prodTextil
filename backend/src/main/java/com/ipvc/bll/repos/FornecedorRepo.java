package com.ipvc.bll.repos;

import com.ipvc.bll.models.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FornecedorRepo extends JpaRepository<Fornecedor, Integer> {
    List<Fornecedor> findByCodigoPostal_Codigo(String codigoPostal);
    Optional<Fornecedor> findByNome(String nome);
}