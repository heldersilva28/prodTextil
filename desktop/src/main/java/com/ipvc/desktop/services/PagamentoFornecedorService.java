package com.ipvc.desktop.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.models.PagamentoFornecedor;
import com.ipvc.desktop.utils.HttpUtils;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

/**
 * Serviço para gerenciar operações relacionadas a pagamentos a fornecedores.
 * Esta classe fornece métodos para buscar e enviar informações de pagamentos a fornecedores para o backend.
 */
public class PagamentoFornecedorService {

    private final String BASE_URL = "http://localhost:8080/api";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PagamentoFornecedorService() {
        objectMapper.findAndRegisterModules(); // Para suporte a tipos como LocalDate
    }

    /**
     * Obtém todos os pagamentos de fornecedores do sistema.
     *
     * @return Lista com todos os pagamentos de fornecedores
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public List<PagamentoFornecedor> getTodosPagamentos() throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.get(BASE_URL + "/pagamentos-fornecedores");

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<PagamentoFornecedor>>() {});
        } else {
            System.err.println("Erro ao buscar pagamentos: " + response.statusCode());
            return Collections.emptyList();
        }
    }

    /**
     * Registra um novo pagamento para um fornecedor.
     *
     * @param pagamento O pagamento a ser registrado
     * @return true se o pagamento foi registrado com sucesso, false caso contrário
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public boolean registrarPagamento(PagamentoFornecedor pagamento) throws IOException, InterruptedException {
        String jsonRequest = objectMapper.writeValueAsString(pagamento);
        HttpResponse<String> response = HttpUtils.post(BASE_URL + "/pagamentos-fornecedores", jsonRequest);

        if (response.statusCode() == 201) {
            return true;
        } else {
            System.err.println("Erro ao registrar pagamento: " + response.statusCode());
            return false;
        }
    }

    /**
     * Atualiza um pagamento existente no sistema.
     *
     * @param pagamento O pagamento com as informações atualizadas
     * @return true se o pagamento foi atualizado com sucesso, false caso contrário
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public boolean atualizarPagamento(PagamentoFornecedor pagamento) throws IOException, InterruptedException {
        String jsonRequest = objectMapper.writeValueAsString(pagamento);
        HttpResponse<String> response = HttpUtils.put(BASE_URL + "/pagamentos-fornecedores/" + pagamento.getId(), jsonRequest);

        if (response.statusCode() == 200) {
            return true;
        } else {
            System.err.println("Erro ao atualizar pagamento: " + response.statusCode());
            return false;
        }
    }
}
