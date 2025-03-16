package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepo extends JpaRepository<Funcionario, Integer> {
    Funcionario findByEmail(String email);
    List<Funcionario> findByCargoContainingIgnoreCase(String cargo);
}
