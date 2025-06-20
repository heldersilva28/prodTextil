package com.ipvc.desktop.services;

import com.ipvc.desktop.models.EncomendaCliente;
import com.ipvc.desktop.models.EtapaDTO;
import com.ipvc.desktop.models.TarefaDTO;
import com.ipvc.desktop.models.TipoEvento;
import com.ipvc.desktop.models.TipoEtapa;
import com.ipvc.desktop.utils.HttpUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class ProducaoService {
    private final String BASE_URL = "http://localhost:8080/api";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProducaoService() {
        objectMapper.findAndRegisterModules(); // Para suporte a LocalDate
    }

    // Métodos para Tarefas
    public List<TarefaDTO> getTarefasDaEncomenda(Integer encomendaId) throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.get(BASE_URL + "/tarefas-producao/encomendas/" + encomendaId);

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<TarefaDTO>>() {});
        } else {
            return Collections.emptyList();
        }
    }

    public TarefaDTO criarTarefa(TarefaDTO tarefa) throws IOException, InterruptedException {
        // Corrigir o endpoint para /api/tarefas-producao conforme esperado pelo backend
        // e ajustar o formato dos dados enviados com o campo descricao como inteiro
        String requestBody = String.format(
            "{\"encomendaId\": %d, \"descricao\": %s, \"funcionarioId\": %d, \"dataInicio\": \"%s\", \"estado\": \"%s\"}",
            tarefa.getEncomendaId(),
            tarefa.getDescricao(), // Descricao como inteiro, sem aspas
            tarefa.getFuncionarioId(),
            tarefa.getDataInicio().toString(),
            tarefa.getEstado()
        );

        System.out.println("Enviando para API: " + requestBody);

        HttpResponse<String> response = HttpUtils.post(BASE_URL + "/tarefas-producao", requestBody);

        if (response.statusCode() == 201 || response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), TarefaDTO.class);
        } else {
            throw new IOException("Erro ao criar tarefa: " + response.statusCode() + " - " + response.body());
        }
    }

    public TarefaDTO atualizarTarefa(TarefaDTO tarefa) throws IOException, InterruptedException {
        // Formata o corpo da requisição para corresponder ao formato esperado pelo endpoint
        String requestBody = String.format(
            "{\"descricao\": %d, \"funcionarioId\": %d, \"dataInicio\": \"%s\", \"dataFim\": %s, \"estado\": \"%s\"}",
            tarefa.getDescricao(),
            tarefa.getFuncionarioId(),
            tarefa.getDataInicio().toString(),
            tarefa.getDataFim() != null ? "\"" + tarefa.getDataFim().toString() + "\"" : "null",
            tarefa.getEstado()
        );

        System.out.println("Atualizando tarefa: " + requestBody);

        HttpResponse<String> response = HttpUtils.put(BASE_URL + "/tarefas-producao/" + tarefa.getId(), requestBody);

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), TarefaDTO.class);
        } else {
            throw new IOException("Erro ao atualizar tarefa: " + response.statusCode() + " - " + response.body());
        }
    }

    /**
     * Obtém todas as tarefas associadas a um funcionário pelo ID do funcionário.
     *
     * @param funcionarioId ID do funcionário para buscar as tarefas associadas
     * @return Lista de tarefas do funcionário
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public List<TarefaDTO> getTarefasPorFuncionario(Integer funcionarioId) throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.get(BASE_URL + "/tarefas-producao/funcionario/" + funcionarioId);

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<TarefaDTO>>() {});
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Verifica se um funcionário tem tarefas associadas.
     *
     * @param funcionarioId ID do funcionário para verificar
     * @return True se o funcionário tem tarefas, false caso contrário
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public boolean funcionarioTemTarefas(Integer funcionarioId) throws IOException, InterruptedException {
        List<TarefaDTO> tarefas = getTarefasPorFuncionario(funcionarioId);
        return !tarefas.isEmpty();
    }

    // Métodos para Etapas
    public List<EtapaDTO> getEtapasDaTarefa(Integer tarefaId) throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.get(BASE_URL + "/etapas-producao/tarefas/" + tarefaId);

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<EtapaDTO>>() {});
        } else {
            return Collections.emptyList();
        }
    }

    public EtapaDTO criarEtapa(EtapaDTO etapa) throws IOException, InterruptedException {
        // Formata o corpo da requisição para seguir o formato esperado pela API
        String requestBody = String.format(
            "{\"tarefaId\": %d, \"tipoEtapaId\": %d, \"dataInicio\": \"%s\", \"dataFim\": %s}",
            etapa.getTarefaId(),
            etapa.getTipoEtapaId(),
            etapa.getDataInicio().toString(),
            etapa.getDataFim() != null ? "\"" + etapa.getDataFim().toString() + "\"" : "null"
        );

        System.out.println("Enviando para API: " + requestBody);

        HttpResponse<String> response = HttpUtils.post(BASE_URL + "/etapas-producao", requestBody);

        if (response.statusCode() == 201 || response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), EtapaDTO.class);
        } else {
            throw new IOException("Erro ao criar etapa: " + response.statusCode() + " - " + response.body());
        }
    }

    public EtapaDTO atualizarEtapa(EtapaDTO etapa) throws IOException, InterruptedException {
        // Incluindo tipoEtapaId para preservar a descrição
        String requestBody = String.format(
            "{\"tipoEtapaId\": %d, \"dataInicio\": \"%s\", \"dataFim\": %s}",
            etapa.getTipoEtapaId(),
            etapa.getDataInicio().toString(),
            etapa.getDataFim() != null ? "\"" + etapa.getDataFim().toString() + "\"" : "null"
        );

        System.out.println("Atualizando etapa: " + requestBody);

        HttpResponse<String> response = HttpUtils.put(BASE_URL + "/etapas-producao/" + etapa.getId(), requestBody);

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), EtapaDTO.class);
        } else {
            throw new IOException("Erro ao atualizar etapa: " + response.statusCode() + " - " + response.body());
        }
    }

    // Métodos para Encomendas relacionadas ao funcionário
    public List<EncomendaCliente> getEncomendasDoFuncionario(Integer funcionarioId) throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.get(BASE_URL + "/encomendas-clientes/funcionario/" + funcionarioId);
        System.out.println("Funcionario ID: " + funcionarioId);
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<EncomendaCliente>>() {});
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Obtém a lista de encomendas de clientes que ainda não possuem tarefas associadas.
     *
     * @return Lista de encomendas sem tarefas
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public List<EncomendaCliente> getEncomendasSemTarefas() throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.get(BASE_URL + "/encomendas-clientes/encomendas-sem-tarefas");

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<EncomendaCliente>>() {});
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Obtém a lista de tipos de eventos disponíveis no sistema.
     *
     * @return Lista de tipos de eventos
     * @throws IOException Em caso de falha na comunicaç��o com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public List<TipoEvento> getTiposEventos() throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.get(BASE_URL + "/tipos-eventos");

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<TipoEvento>>() {});
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Obtém a lista de tipos de etapas disponíveis no sistema.
     *
     * @return Lista de tipos de etapas
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public List<TipoEtapa> getTiposEtapas() throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.get(BASE_URL + "/tipos-etapas");

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<TipoEtapa>>() {});
        } else {
            return Collections.emptyList();
        }
    }
}


