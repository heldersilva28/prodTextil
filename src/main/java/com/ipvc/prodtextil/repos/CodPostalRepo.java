package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.CodigosPostais;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodPostalRepo extends JpaRepository<CodigosPostais,Integer> {

    Optional<CodigosPostais> findByCodigo(String postalCode);

}
