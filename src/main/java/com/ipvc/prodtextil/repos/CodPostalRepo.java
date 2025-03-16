package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.CodigosPostai;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodPostalRepo extends JpaRepository<CodigosPostai,Integer> {

    Optional<CodigosPostai> findByCodigo(String postalCode);
}
