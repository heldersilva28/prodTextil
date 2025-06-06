package com.ipvc.bll.services;

import com.ipvc.bll.dto.FornecedorDTO;
import com.ipvc.bll.models.CodigosPostais;
import com.ipvc.bll.models.Fornecedor;
import com.ipvc.bll.repos.FornecedorRepo;
import com.ipvc.bll.repos.CodPostalRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FornecedorService {
    private final FornecedorRepo fornecedorRepo;
    private final CodPostalRepo codigoPostalRepo;

    public FornecedorService(FornecedorRepo fornecedorRepo, CodPostalRepo codigoPostalRepo) {
        this.fornecedorRepo = fornecedorRepo;
        this.codigoPostalRepo = codigoPostalRepo;
    }

    public List<FornecedorDTO.FornecedorResponseDTO> getAllFornecedores() {
        return fornecedorRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<FornecedorDTO.FornecedorResponseDTO> getFornecedorById(Integer id) {
        return fornecedorRepo.findById(id).map(this::convertToDTO);
    }

    public FornecedorDTO.FornecedorResponseDTO saveFornecedor(FornecedorDTO.FornecedorCreateDTO fornecedorDTO) {
        CodigosPostais codigoPostal = codigoPostalRepo.findById(Integer.valueOf(fornecedorDTO.codigoPostalId()))
                .orElseThrow(() -> new RuntimeException("Código postal não encontrado"));

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(fornecedorDTO.nome());
        fornecedor.setEmail(fornecedorDTO.email());
        fornecedor.setTelefone(fornecedorDTO.telefone());
        fornecedor.setMorada(fornecedorDTO.morada());
        fornecedor.setCodigoPostal(codigoPostal);

        return convertToDTO(fornecedorRepo.save(fornecedor));
    }

    public Optional<FornecedorDTO.FornecedorResponseDTO> updateFornecedor(Integer id, FornecedorDTO.FornecedorUpdateDTO fornecedorDTO) {
        return fornecedorRepo.findById(id).map(fornecedor -> {
            CodigosPostais codigoPostal = codigoPostalRepo.findById(Integer.valueOf(fornecedorDTO.codigoPostalId()))
                    .orElseThrow(() -> new RuntimeException("Código postal não encontrado"));

            fornecedor.setNome(fornecedorDTO.nome());
            fornecedor.setTelefone(fornecedorDTO.telefone());
            fornecedor.setMorada(fornecedorDTO.morada());
            fornecedor.setCodigoPostal(codigoPostal);

            return convertToDTO(fornecedorRepo.save(fornecedor));
        });
    }

    public void deleteFornecedor(Integer id) {
        fornecedorRepo.deleteById(id);
    }

    private FornecedorDTO.FornecedorResponseDTO convertToDTO(Fornecedor fornecedor) {
        return new FornecedorDTO.FornecedorResponseDTO(
                fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getEmail(),
                fornecedor.getTelefone(),
                fornecedor.getMorada(),
                fornecedor.getCodigoPostal().getCodigo()
        );
    }
}
