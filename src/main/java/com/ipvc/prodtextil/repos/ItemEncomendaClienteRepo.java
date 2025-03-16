package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.ItemEncomendaCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemEncomendaClienteRepo extends JpaRepository<ItemEncomendaCliente, Integer> {
    List<ItemEncomendaCliente> findByEncomenda_Id(Integer encomendaId);
}
