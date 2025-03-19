package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.Cliente;
import com.ipvc.prodtextil.models.Funcionario;
import com.ipvc.prodtextil.models.TiposUtilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepo extends JpaRepository<Funcionario, Integer> {
    List<Funcionario> findAllByCargo_TipoUtilizador(TiposUtilizador tipoUtilizador);
    Optional<Funcionario> findById(Integer id);
    Optional<Funcionario> findByUtilizadorId(Integer utilizadorId);
    Optional<Funcionario> findFuncionariosByUtilizadorId(Integer utilizadorId);
    Optional<Funcionario> findByUtilizador_Id(Integer utilizadorId);
}
