package com.ipvc.bll.services;

import com.ipvc.bll.dto.*;
import com.ipvc.bll.models.Cliente;
import com.ipvc.bll.models.CodigosPostais;
import com.ipvc.bll.repos.ClienteRepo;
import com.ipvc.bll.repos.CodPostalRepo;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
        CodigosPostais codpostal = codpostalRepo.findByCodigo(clienteDTO.codpostalId())
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
            CodigosPostais codpostal = codpostalRepo.findByCodigo(clienteDTO.codpostalId())
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

    public ClientesStatsDTO obterEstatisticasClientes() {
        ClientesStatsDTO dto = new ClientesStatsDTO();

        dto.setTotal(clienteRepo.count());
        dto.setAtivos(clienteRepo.countClientesComEncomendas());

        Map<String, Integer> encomendasPorCliente = new LinkedHashMap<>();

        // Popular encomendasPorCliente
        List<Object[]> top = clienteRepo.topClientesPorEncomendas();
        for (Object[] row : top) {
            String nome = (String) row[0];
            int qtd = ((Number) row[1]).intValue();
            encomendasPorCliente.put(nome, qtd);
        }

        dto.setEncomendasPorCliente(encomendasPorCliente);
        return dto;
    }

}
