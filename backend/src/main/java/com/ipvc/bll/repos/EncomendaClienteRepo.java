package com.ipvc.bll.repos;

import com.ipvc.bll.models.EncomendasCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EncomendaClienteRepo extends JpaRepository<EncomendasCliente, Integer> {
    List<EncomendasCliente> findByCliente_Id(Integer clienteId);
    List<EncomendasCliente> findByEstado_Id(Integer estadoId);
    List<EncomendasCliente> findByDataEncomendaBetween(LocalDate startDate, LocalDate endDate);
    @Query("SELECT COUNT(e) FROM EncomendasCliente e WHERE e.estado.id = :estadoId")
    long contarPorEstado(@Param("estadoId") int estadoId);

    @Query("SELECT MONTH(e.dataEncomenda), COUNT(e) FROM EncomendasCliente e GROUP BY MONTH(e.dataEncomenda) ORDER BY MONTH(e.dataEncomenda)")
    List<Object[]> contarPorMes();

    long countByCliente_Id(Integer clienteId);

    @Query("SELECT COUNT(e) FROM EncomendasCliente e WHERE e.estado.id = :estadoId AND e.cliente.id = :clienteId")
    long contarPorEstadoECliente(Integer estadoId, Integer clienteId);

    @Query("SELECT MONTH(e.dataEncomenda) as mes, COUNT(e) as quantidade FROM EncomendasCliente e WHERE e.cliente.id = :clienteId GROUP BY MONTH(e.dataEncomenda)")
    List<Object[]> contarPorMesECliente(Integer clienteId);

}
