package com.ipvc.desktop.services;

import com.ipvc.desktop.models.EncomendaCliente;
import com.ipvc.desktop.utils.HttpUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

/**
 * Serviço para gerenciar operações relacionadas a encomendas de clientes.
 * Esta classe fornece métodos para buscar informações de encomendas de clientes do backend.
 */
public class EncomendaClienteService {

    private final String BASE_URL = "http://localhost:8080/api";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EncomendaClienteService() {
        objectMapper.findAndRegisterModules(); // Para suporte a tipos como LocalDate
    }

    /**
     * Obtém a lista de encomendas de clientes que não possuem pagamento.
     *
     * @return Lista de encomendas sem pagamento
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public List<EncomendaCliente> getEncomendasSemPagamento() throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.get(BASE_URL + "/encomendas-clientes/encomendas-sem-pagamento");

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<EncomendaCliente>>() {});
        } else {
            System.err.println("Erro ao buscar encomendas sem pagamento: " + response.statusCode());
            return Collections.emptyList();
        }
    }

    /**
     * Obtém todas as encomendas de clientes.
     *
     * @return Lista com todas as encomendas de clientes
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public List<EncomendaCliente> getTodasEncomendas() throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.get(BASE_URL + "/encomendas-clientes");

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<EncomendaCliente>>() {});
        } else {
            System.err.println("Erro ao buscar todas as encomendas: " + response.statusCode());
            return Collections.emptyList();
        }
    }
}
