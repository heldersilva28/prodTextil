package com.ipvc.prodtextil.services;

import com.ipvc.prodtextil.dto.*;
import com.ipvc.prodtextil.models.Cliente;
import com.ipvc.prodtextil.models.CodigosPostai;
import com.ipvc.prodtextil.repos.ClienteRepo;
import com.ipvc.prodtextil.repos.CodPostalRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    private final ClienteRepo clienteRepo;
    private final CodPostalRepo codpostalRepo;

    public ClienteService(ClienteRepo clienteRepo, CodPostalRepo codpostalRepo) {
        this.clienteRepo = clienteRepo;
        this.codpostalRepo = codpostalRepo;
    }

    public List<Cliente> getAllClientes() {
        return clienteRepo.findAll();  // Retorna uma lista de clientes
    }

    public List<ClienteDTO.ClienteResponseDTO> getAllClientesDTO() {
        // Suponha que clienteRepository seja seu repositório que retorna todos os clientes
        List<Cliente> clientes = clienteRepo.findAll();  // Retorna todos os clientes

        return clientes.stream()
                .map(cliente -> new ClienteDTO.ClienteResponseDTO(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getEmail(),
                        cliente.getMorada(),
                        cliente.getTelefone(),
                        cliente.getCodigoPostal().getCodigo() // Supondo que 'codpostal' seja um objeto e 'descricao' seja um campo
                ))
                .collect(Collectors.toList());
    }



    public Optional<ClienteDTO.ClienteResponseDTO> getClienteById(Integer id) {
        return clienteRepo.findById(id).map(this::convertToDTO);
    }

    public ClienteDTO.ClienteResponseDTO saveCliente(ClienteDTO.ClienteCreateDTO clienteDTO) {
        CodigosPostai codpostal = codpostalRepo.findByCodigo(clienteDTO.codpostalId())
                .orElseThrow(() -> new RuntimeException("Código postal não encontrado"));

        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.nome());
        cliente.setEmail(clienteDTO.email());
        cliente.setMorada(clienteDTO.morada());
        cliente.setTelefone(clienteDTO.telefone());
        cliente.setCodigoPostal(codpostal);

        Cliente saved = clienteRepo.save(cliente);
        return convertToDTO(saved);
    }

    public Optional<ClienteDTO.ClienteResponseDTO> updateCliente(Integer id, ClienteDTO.ClienteUpdateDTO clienteDTO) {
        return clienteRepo.findById(id).map(cliente -> {
            CodigosPostai codpostal = codpostalRepo.findByCodigo(clienteDTO.codpostalId())
                    .orElseThrow(() -> new RuntimeException("Código postal não encontrado"));

            cliente.setNome(clienteDTO.nome());
            cliente.setMorada(clienteDTO.morada());
            cliente.setTelefone(clienteDTO.telefone());
            cliente.setCodigoPostal(codpostal);

            return convertToDTO(clienteRepo.save(cliente));
        });
    }

    public void deleteCliente(Integer id) {
        clienteRepo.deleteById(id);
    }

    private ClienteDTO.ClienteResponseDTO convertToDTO(Cliente cliente) {
        return new ClienteDTO.ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getMorada(),
                cliente.getTelefone(),
                cliente.getCodigoPostal().getCodigo()
        );
    }
}
