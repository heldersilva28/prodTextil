package com.ipvc.bll.repos;

import com.ipvc.bll.models.EncomendasFornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EncomendaFornecedorRepo extends JpaRepository<EncomendasFornecedor, Integer> {
    List<EncomendasFornecedor> findByFornecedor_Id(Integer fornecedorId);
    List<EncomendasFornecedor> findByEstado_Id(Integer estadoId);
}
