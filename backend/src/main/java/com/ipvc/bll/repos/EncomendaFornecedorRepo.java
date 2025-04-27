package com.ipvc.bll.repos;

import com.ipvc.bll.models.EncomendasFornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EncomendaFornecedorRepo extends JpaRepository<EncomendasFornecedor, Integer> {
    List<EncomendasFornecedor> findByFornecedor_Id(Integer fornecedorId);
    List<EncomendasFornecedor> findByEstado_Id(Integer estadoId);
    @Query("SELECT COUNT(e) FROM EncomendasFornecedor e WHERE e.estado.id = :estadoId")
    long contarPorEstado(@Param("estadoId") int estadoId);

    @Query("SELECT MONTH(e.dataPedido), COUNT(e) FROM EncomendasFornecedor e GROUP BY MONTH(e.dataPedido) ORDER BY MONTH(e.dataPedido)")
    List<Object[]> contarPorMes();
}
