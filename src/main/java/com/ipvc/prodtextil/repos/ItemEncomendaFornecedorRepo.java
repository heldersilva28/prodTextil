package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.ItensEncomendaFornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemEncomendaFornecedorRepo extends JpaRepository<ItensEncomendaFornecedor, Integer> {
    List<ItensEncomendaFornecedor> findByEncomenda_Id(Integer encomendaId);
}
