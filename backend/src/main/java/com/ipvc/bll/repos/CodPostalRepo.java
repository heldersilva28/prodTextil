package com.ipvc.bll.repos;

import com.ipvc.bll.models.CodigosPostais;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodPostalRepo extends JpaRepository<CodigosPostais,Integer> {

    Optional<CodigosPostais> findByCodigo(String postalCode);

    CodigosPostais deleteByCodigo(String postalCode);
}
