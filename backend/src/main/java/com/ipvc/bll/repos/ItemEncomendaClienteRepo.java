package com.ipvc.bll.repos;

import com.ipvc.bll.models.ItensEncomendaCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemEncomendaClienteRepo extends JpaRepository<ItensEncomendaCliente, Integer> {
    Optional<ItensEncomendaCliente> findByEncomendaIdAndProduto(Integer encomendaId, String produto);

    void deleteByEncomendaIdAndProduto(Integer encomendaId, String produto);

}
