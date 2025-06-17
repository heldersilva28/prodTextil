package com.ipvc.desktop.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Utilitário para realizar requisições HTTP.
 * Esta classe fornece métodos para realizar operações HTTP comuns como GET, POST, PUT e DELETE.
 */
public class HttpUtils {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    /**
     * Realiza uma requisição GET para a URL especificada.
     *
     * @param url URL para a qual fazer a requisição GET
     * @return Resposta HTTP da requisição
     * @throws IOException Em caso de falha de I/O
     * @throws InterruptedException Se a operação for interrompida
     */
    public static HttpResponse<String> get(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Realiza uma requisição POST para a URL especificada com o corpo fornecido.
     *
     * @param url URL para a qual fazer a requisição POST
     * @param jsonBody Corpo da requisição em formato JSON
     * @return Resposta HTTP da requisição
     * @throws IOException Em caso de falha de I/O
     * @throws InterruptedException Se a operação for interrompida
     */
    public static HttpResponse<String> post(String url, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Realiza uma requisição PUT para a URL especificada com o corpo fornecido.
     *
     * @param url URL para a qual fazer a requisição PUT
     * @param jsonBody Corpo da requisição em formato JSON
     * @return Resposta HTTP da requisição
     * @throws IOException Em caso de falha de I/O
     * @throws InterruptedException Se a operação for interrompida
     */
    public static HttpResponse<String> put(String url, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Realiza uma requisição DELETE para a URL especificada.
     *
     * @param url URL para a qual fazer a requisição DELETE
     * @return Resposta HTTP da requisição
     * @throws IOException Em caso de falha de I/O
     * @throws InterruptedException Se a operação for interrompida
     */
    public static HttpResponse<String> delete(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Realiza uma requisição PATCH para a URL especificada com o corpo fornecido.
     *
     * @param url URL para a qual fazer a requisição PATCH
     * @param jsonBody Corpo da requisição em formato JSON
     * @return Resposta HTTP da requisição
     * @throws IOException Em caso de falha de I/O
     * @throws InterruptedException Se a operação for interrompida
     */
    public static HttpResponse<String> patch(String url, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody))
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Adiciona cabeçalho de autenticação nas requisições.
     *
     * @param token Token de autenticação
     * @return Builder do HttpRequest com o cabeçalho de autenticação
     */
    public static HttpRequest.Builder withAuth(String token) {
        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + token);
    }
}
