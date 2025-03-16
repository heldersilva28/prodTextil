package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.Fornecedore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FornecedorRepo extends JpaRepository<Fornecedore, Integer> {
    List<Fornecedore> findByCodigoPostal_Codigo(String codigoPostal);
    Optional<Fornecedore> findByNome(String nome);
}