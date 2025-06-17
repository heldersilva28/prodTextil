package com.ipvc.desktop.services;

import com.ipvc.desktop.models.EncomendaCliente;
import com.ipvc.desktop.models.EtapaDTO;
import com.ipvc.desktop.models.TarefaDTO;
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
        HttpResponse<String> response = HttpUtils.post(BASE_URL + "/tarefas", objectMapper.writeValueAsString(tarefa));

        if (response.statusCode() == 201 || response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), TarefaDTO.class);
        } else {
            throw new IOException("Erro ao criar tarefa: " + response.statusCode() + " - " + response.body());
        }
    }

    public TarefaDTO atualizarTarefa(TarefaDTO tarefa) throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.put(BASE_URL + "/tarefas/" + tarefa.getId(), objectMapper.writeValueAsString(tarefa));

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
        HttpResponse<String> response = HttpUtils.post(BASE_URL + "/etapas", objectMapper.writeValueAsString(etapa));

        if (response.statusCode() == 201 || response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), EtapaDTO.class);
        } else {
            throw new IOException("Erro ao criar etapa: " + response.statusCode() + " - " + response.body());
        }
    }

    public EtapaDTO atualizarEtapa(EtapaDTO etapa) throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.put(BASE_URL + "/etapas/" + etapa.getId(), objectMapper.writeValueAsString(etapa));

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
}
