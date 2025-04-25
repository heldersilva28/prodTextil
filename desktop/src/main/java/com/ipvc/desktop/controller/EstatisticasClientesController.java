package com.ipvc.desktop.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;
import java.util.Map;

public class EstatisticasClientesController {

    @FXML private Label labelTotalClientes;
    @FXML private Label labelClientesAtivos;
    @FXML private BarChart<String, Number> graficoTopClientes;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String url = "http://localhost:8080/api/clientes/estatisticas";

    @FXML
    public void initialize() {
        carregarEstatisticas();
    }

    private void carregarEstatisticas() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::atualizarUI)
                .exceptionally(e -> {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        labelTotalClientes.setText("Erro");
                        labelClientesAtivos.setText("Erro");
                    });
                    return null;
                });
    }

    private void atualizarUI(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode data = mapper.readTree(json);

            int total = data.get("total").asInt();
            int ativos = data.get("ativos").asInt();
            JsonNode encomendasPorCliente = data.get("encomendasPorCliente");

            Platform.runLater(() -> {
                labelTotalClientes.setText(String.valueOf(total));
                labelClientesAtivos.setText(String.valueOf(ativos));

                preencherGrafico(graficoTopClientes, "Top Clientes", encomendasPorCliente);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preencherGrafico(BarChart<String, Number> chart, String title, JsonNode dataNode) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(title);

        Iterator<Map.Entry<String, JsonNode>> campos = dataNode.fields();
        while (campos.hasNext()) {
            Map.Entry<String, JsonNode> entrada = campos.next();
            series.getData().add(new XYChart.Data<>(entrada.getKey(), entrada.getValue().asInt()));
        }

        chart.getData().clear();
        chart.getData().add(series);
    }
}
