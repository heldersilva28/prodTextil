package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.EncomendaFornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EncomendaFornecedorRepo extends JpaRepository<EncomendaFornecedor, Integer> {
    List<EncomendaFornecedor> findByFornecedor_Id(Integer fornecedorId);
    List<EncomendaFornecedor> findByEstado_Id(Integer estadoId);
}
