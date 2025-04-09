package com.ipvc.desktop.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Utilizador {
    private Integer id;
    private String username;
    private String email;
    private Integer tipoUtilizadorId;  // Agora é um Integer para corresponder ao backend

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Integer getTipoUtilizadorId() {
        return tipoUtilizadorId;
    }

    // Método para retornar o nome do tipo de utilizador através de uma requisição HTTP
    public String getTipoUtilizadorNome() {
        // Chama a API do backend para buscar o nome do tipo de utilizador
        return getTipoUtilizadorNomeFromApi(tipoUtilizadorId);
    }

    // Faz uma requisição HTTP para buscar o nome do tipo de utilizador
    private String getTipoUtilizadorNomeFromApi(int tipoUtilizadorId) {
        try {
            // Faz a requisição HTTP para buscar o nome do tipo de utilizador
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/tipos-utilizadores/" + tipoUtilizadorId))
                    .build();

            // Envia a requisição e obtém a resposta
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            // Se a resposta for bem-sucedida (status 200), retorna o nome do tipo de utilizador
            if (response.statusCode() == 200) {
                // Aqui, vamos assumir que a resposta é um JSON com a chave "nome"
                String jsonResponse = response.body();
                // Vamos tentar fazer o parsing para extrair o nome
                // Isso pode ser feito usando uma biblioteca como Jackson ou Gson
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                return jsonNode.get("nome").asText();  // Pega o valor do campo "nome"
            } else {
                return "Desconhecido";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao buscar nome";  // Se ocorrer um erro, retorna uma mensagem de erro
        }
    }
}
