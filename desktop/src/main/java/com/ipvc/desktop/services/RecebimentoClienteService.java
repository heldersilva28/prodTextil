package com.ipvc.desktop.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.models.RecebimentoCliente;
import com.ipvc.desktop.utils.HttpUtils;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

/**
 * Serviço para gerenciar operações relacionadas a recebimentos de clientes.
 * Esta classe fornece métodos para buscar informações de recebimentos de clientes do backend.
 */
public class RecebimentoClienteService {

    private final String BASE_URL = "http://localhost:8080/api";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RecebimentoClienteService() {
        objectMapper.findAndRegisterModules(); // Para suporte a tipos como LocalDate
    }

    /**
     * Obtém todos os recebimentos de clientes.
     *
     * @return Lista com todos os recebimentos de clientes
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public List<RecebimentoCliente> getTodosRecebimentos() throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.get(BASE_URL + "/recebimentos-clientes");

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<RecebimentoCliente>>() {});
        } else {
            System.err.println("Erro ao buscar todos os recebimentos: " + response.statusCode());
            return Collections.emptyList();
        }
    }

    /**
     * Obtém recebimentos de clientes pelo estado de pagamento.
     *
     * @param estadoPagamento Estado de pagamento para filtrar os recebimentos ("Pendente", "Parcial", "Completo")
     * @return Lista de recebimentos filtrados pelo estado de pagamento
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public List<RecebimentoCliente> getRecebimentosPorEstado(String estadoPagamento) throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.get(BASE_URL + "/recebimentos-clientes/estado/" + estadoPagamento);

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<RecebimentoCliente>>() {});
        } else {
            System.err.println("Erro ao buscar recebimentos por estado: " + response.statusCode());
            return Collections.emptyList();
        }
    }

    /**
     * Obtém recebimentos de clientes por ID do cliente.
     *
     * @param clienteId ID do cliente para filtrar os recebimentos
     * @return Lista de recebimentos do cliente especificado
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public List<RecebimentoCliente> getRecebimentosPorCliente(Integer clienteId) throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.get(BASE_URL + "/recebimentos-clientes/cliente/" + clienteId);

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<RecebimentoCliente>>() {});
        } else {
            System.err.println("Erro ao buscar recebimentos por cliente: " + response.statusCode());
            return Collections.emptyList();
        }
    }

    /**
     * Registra um novo recebimento de cliente.
     *
     * @param recebimento Objeto RecebimentoCliente contendo os dados do recebimento
     * @return O recebimento registrado com o ID atribuído pelo servidor
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public RecebimentoCliente registrarRecebimento(RecebimentoCliente recebimento) throws IOException, InterruptedException {
        String jsonBody = objectMapper.writeValueAsString(recebimento);
        HttpResponse<String> response = HttpUtils.post(BASE_URL + "/recebimentos-clientes", jsonBody);

        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), RecebimentoCliente.class);
        } else {
            System.err.println("Erro ao registrar recebimento: " + response.statusCode());
            throw new IOException("Falha ao registrar recebimento. Código de status: " + response.statusCode());
        }
    }

    /**
     * Atualiza um recebimento de cliente existente.
     *
     * @param id ID do recebimento a ser atualizado
     * @param recebimento Objeto RecebimentoCliente contendo os dados atualizados
     * @return O recebimento atualizado
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public RecebimentoCliente atualizarRecebimento(Integer id, RecebimentoCliente recebimento) throws IOException, InterruptedException {
        String jsonBody = objectMapper.writeValueAsString(recebimento);
        HttpResponse<String> response = HttpUtils.put(BASE_URL + "/recebimentos-clientes/" + id, jsonBody);

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), RecebimentoCliente.class);
        } else {
            System.err.println("Erro ao atualizar recebimento: " + response.statusCode());
            throw new IOException("Falha ao atualizar recebimento. Código de status: " + response.statusCode());
        }
    }

    /**
     * Exclui um recebimento de cliente.
     *
     * @param id ID do recebimento a ser excluído
     * @return true se a exclusão foi bem-sucedida, false caso contrário
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public boolean excluirRecebimento(Integer id) throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.delete(BASE_URL + "/recebimentos-clientes/" + id);

        return response.statusCode() == 204 || response.statusCode() == 200;
    }
}
