package com.ipvc.prodtextil.repos;

import com.ipvc.prodtextil.models.ItensEncomendaFornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemEncomendaFornecedorRepo extends JpaRepository<ItensEncomendaFornecedor, Integer> {
    Optional<ItensEncomendaFornecedor> findByEncomendaIdAndMaterialId(Integer encomendaId, Integer materialId);
    void deleteByEncomendaIdAndMaterialId(Integer encomendaId, Integer materialId);
}
