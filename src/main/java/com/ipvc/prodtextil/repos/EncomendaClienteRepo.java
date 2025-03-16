package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.EncomendasCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EncomendaClienteRepo extends JpaRepository<EncomendasCliente, Integer> {
    List<EncomendasCliente> findByCliente_Id(Integer clienteId);
    List<EncomendasCliente> findByEstado_Id(Integer estadoId);
    List<EncomendasCliente> findByDataEncomendaBetween(LocalDate startDate, LocalDate endDate);
}
