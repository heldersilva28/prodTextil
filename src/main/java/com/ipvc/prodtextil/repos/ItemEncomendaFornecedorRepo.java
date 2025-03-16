package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.ItemEncomendaFornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemEncomendaFornecedorRepo extends JpaRepository<ItemEncomendaFornecedor, Integer> {
    List<ItemEncomendaFornecedor> findByEncomenda_Id(Integer encomendaId);
}
