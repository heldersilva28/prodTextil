package com.ipvc.bll.services;

import com.ipvc.bll.dto.CodigosPostaisDTO.*;
import com.ipvc.bll.models.CodigosPostais;
import com.ipvc.bll.repos.CodPostalRepo;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CodigosPostaisService {
    private final CodPostalRepo codigosPostaisRepo;

    public CodigosPostaisService(CodPostalRepo codigosPostaisRepo) {
        this.codigosPostaisRepo = codigosPostaisRepo;
    }

    public List<CodigoPostalResponseDTO> getAllCodigosPostais() {
        return codigosPostaisRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<CodigoPostalResponseDTO> getCodigoPostalByCodigo(String codigo) {
        return codigosPostaisRepo.findByCodigo(codigo).map(this::convertToDTO);
    }

    public CodigoPostalResponseDTO saveCodigoPostal(CodigoPostalCreateDTO codigosPostaisDTO) {
        CodigosPostais codigoPostal = new CodigosPostais();
        codigoPostal.setCodigo(codigosPostaisDTO.codigo());
        codigoPostal.setLocalidade(codigosPostaisDTO.localidade());
        codigoPostal.setDistrito(codigosPostaisDTO.distrito());
        codigoPostal.setPais(codigosPostaisDTO.pais());

        return convertToDTO(codigosPostaisRepo.save(codigoPostal));
    }

    public Optional<CodigoPostalResponseDTO> updateCodigoPostal(String codigo, CodigoPostalUpdateDTO codigosPostaisDTO) {
        return codigosPostaisRepo.findByCodigo(codigo).map(codigoPostal -> {
            codigoPostal.setLocalidade(codigosPostaisDTO.localidade());
            codigoPostal.setDistrito(codigosPostaisDTO.distrito());
            codigoPostal.setPais(codigosPostaisDTO.pais());

            return convertToDTO(codigosPostaisRepo.save(codigoPostal));
        });
    }

    public void deleteCodigoPostal(String codigo) {
        codigosPostaisRepo.deleteByCodigo(codigo);
    }

    private CodigoPostalResponseDTO convertToDTO(CodigosPostais codigoPostal) {
        return new CodigoPostalResponseDTO(
                codigoPostal.getCodigo(),
                codigoPostal.getLocalidade(),
                codigoPostal.getDistrito(),
                codigoPostal.getPais()
        );
    }
}
