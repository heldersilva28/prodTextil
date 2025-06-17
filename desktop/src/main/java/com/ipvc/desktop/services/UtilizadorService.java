package com.ipvc.desktop.services;

import com.ipvc.desktop.models.FuncionarioProducaoDTO;
import com.ipvc.desktop.utils.HttpUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciar operações relacionadas a utilizadores e funcionários.
 * Esta classe fornece métodos para buscar informações de utilizadores e funcionários do backend.
 */
public class UtilizadorService {

    private final String BASE_URL = "http://localhost:8080/api";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UtilizadorService() {
        objectMapper.findAndRegisterModules(); // Para suporte a tipos como LocalDate
    }

    /**
     * Obtém a lista de funcionários pelo ID do cargo.
     *
     * @param cargo ID do cargo para filtrar os funcionários
     * @return Lista de funcionários com o cargo especificado
     * @throws IOException Em caso de falha na comunicação com o servidor
     * @throws InterruptedException Se a operação for interrompida
     */
    public List<FuncionarioProducaoDTO> getFuncionariosPorCargo(Integer cargo) throws IOException, InterruptedException {
        // Usando o endpoint correto do FuncionarioController
        HttpResponse<String> response = HttpUtils.get(BASE_URL + "/funcionarios?cargo=" + cargo);

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<FuncionarioProducaoDTO>>() {});
        } else {
            // Se não existir o endpoint com filtro por cargo, podemos obter todos e filtrar localmente
            List<FuncionarioProducaoDTO> todosFuncionarios = getTodosFuncionarios();
            return todosFuncionarios.stream()
                    .filter(func -> cargo.equals(func.getCargo()))
                    .collect(Collectors.toList());
        }
    }
    public List<FuncionarioProducaoDTO> getTodosFuncionarios() throws IOException, InterruptedException {
        HttpResponse<String> response = HttpUtils.get(BASE_URL + "/utilizadores/funcionarios");

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<FuncionarioProducaoDTO>>() {});
        } else {
            return Collections.emptyList();
        }
    }
}
