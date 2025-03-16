package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.CodigoPostal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodPostalRepo extends JpaRepository<CodigoPostal,Integer> {

    Optional<CodigoPostal> findByCodigo(String postalCode);
}
