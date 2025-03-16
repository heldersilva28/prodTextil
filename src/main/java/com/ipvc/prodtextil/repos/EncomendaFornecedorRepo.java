package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.EncomendasFornecedore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EncomendaFornecedorRepo extends JpaRepository<EncomendasFornecedore, Integer> {
    List<EncomendasFornecedore> findByFornecedor_Id(Integer fornecedorId);
    List<EncomendasFornecedore> findByEstado_Id(Integer estadoId);
}
