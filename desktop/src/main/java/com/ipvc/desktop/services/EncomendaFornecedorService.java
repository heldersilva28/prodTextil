package com.ipvc.desktop.services;

import com.ipvc.desktop.models.EncomendaFornecedor;
import com.ipvc.desktop.utils.HttpUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

/**
 * Serviço para gerenciar operações relacionadas a encomendas de fornecedores.
 * Esta classe fornece métodos para buscar informações de encomendas de fornecedores do backend.
 */
public class EncomendaFornecedorService {

    private final String BASE_URL = "http://localhost:8080/api";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EncomendaFornecedorService() {
        objectMapper.findAndRegisterModules(); // Para suporte a tipos como LocalDate
    }

    /**
     * Obtém a lista de encomendas de fornecedores que não possuem pagamento.
     *
     * @return Lista de encomendas sem pagamento
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public List<EncomendaFornecedor> getEncomendasSemPagamento() throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.get(BASE_URL + "/encomendas-fornecedores/encomendas-sem-pagamento");

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<EncomendaFornecedor>>() {});
        } else {
            System.err.println("Erro ao buscar encomendas sem pagamento: " + response.statusCode());
            return Collections.emptyList();
        }
    }

    /**
     * Obtém todas as encomendas de fornecedores.
     *
     * @return Lista com todas as encomendas de fornecedores
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public List<EncomendaFornecedor> getTodasEncomendas() throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.get(BASE_URL + "/encomendas-fornecedores");

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<EncomendaFornecedor>>() {});
        } else {
            System.err.println("Erro ao buscar todas as encomendas: " + response.statusCode());
            return Collections.emptyList();
        }
    }
}
