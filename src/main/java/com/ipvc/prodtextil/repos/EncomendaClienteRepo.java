package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.EncomendaCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EncomendaClienteRepo extends JpaRepository<EncomendaCliente, Integer> {
    List<EncomendaCliente> findByCliente_Id(Integer clienteId);
    List<EncomendaCliente> findByEstado_Id(Integer estadoId);
    List<EncomendaCliente> findByDataEncomendaBetween(LocalDate startDate, LocalDate endDate);
}
