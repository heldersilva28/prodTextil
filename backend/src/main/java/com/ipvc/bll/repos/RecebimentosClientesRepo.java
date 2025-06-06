package com.ipvc.bll.repos;

import com.ipvc.bll.models.RecebimentosCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecebimentosClientesRepo extends JpaRepository<RecebimentosCliente, Integer> {
    List<RecebimentosCliente> findByEncomenda_Id(Integer encomendaId);
    List<RecebimentosCliente> findByDataRecebimentoBetween(LocalDate inicio, LocalDate fim);
}
