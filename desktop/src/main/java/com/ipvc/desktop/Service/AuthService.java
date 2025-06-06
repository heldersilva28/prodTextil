package com.ipvc.desktop.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.Request.LoginRequest;
import com.ipvc.desktop.Response.AuthResponse;
import com.ipvc.desktop.models.Utilizador;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthService {
    private static final String BASE_URL = "http://localhost:8080/api/auth/login";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthResponse login(String email, String password) throws Exception {
        LoginRequest request = new LoginRequest(email, password);
        String json = objectMapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(response.body(), AuthResponse.class);
    }
    public boolean existemUtilizadores() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/auth/existem-utilizadores"))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Boolean.parseBoolean(response.body());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int obterCargoUtilizadorPorEmail(String email) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/utilizadores/cargo?email=" + email))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return Integer.parseInt(response.body()); // espera que a API devolva um número
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // fallback em caso de erro
        }
    }

    public Utilizador obterUtilizadorPorEmail(String email) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/utilizadores/email?email=" + email))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), Utilizador.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // fallback em caso de erro
        }
    }

}
