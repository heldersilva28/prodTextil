package com.ipvc.desktop.controller;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DashboardController {

    @FXML private VBox dashboardVbox;
    @FXML private Label totalUtilizadoresLabel;
    @FXML private Label encomendasClientesLabel;
    @FXML private Label encomendasFornecedoresLabel;
    @FXML private Label materiaisStockLabel;
    @FXML private Label funcionariosLabel;
    @FXML private PieChart pieChart;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String baseUrl = "http://localhost:8080/api/dashboard"; // Endpoint único

    @FXML
    public void initialize() {
        FadeTransition ft = new FadeTransition(Duration.millis(800), dashboardVbox);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        carregarDadosDashboard();
    }

    private void carregarDadosDashboard() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .GET()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::atualizarDashboard)
                .exceptionally(e -> {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        totalUtilizadoresLabel.setText("Erro");
                        encomendasClientesLabel.setText("Erro");
                        encomendasFornecedoresLabel.setText("Erro");
                        materiaisStockLabel.setText("Erro");
                        funcionariosLabel.setText("Erro");
                    });
                    return null;
                });
    }

    private void atualizarDashboard(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode data = mapper.readTree(json);

            long totalUtilizadores = data.get("totalUtilizadores").asLong();
            long encomendasClientes = data.get("totalEncomendasClientes").asLong();
            long encomendasFornecedores = data.get("totalEncomendasFornecedores").asLong();
            long materiaisStock = data.get("totalMateriais").asLong();
            long funcionarios = data.get("totalFuncionarios").asLong();

            BigDecimal rendimentos = data.has("rendimentos") ?
                    new BigDecimal(data.get("rendimentos").asText()) : BigDecimal.ZERO;

            BigDecimal despesas = data.has("despesas") ?
                    new BigDecimal(data.get("despesas").asText()) : BigDecimal.ZERO;

            Platform.runLater(() -> {
                totalUtilizadoresLabel.setText(String.valueOf(totalUtilizadores));
                encomendasClientesLabel.setText(String.valueOf(encomendasClientes));
                encomendasFornecedoresLabel.setText(String.valueOf(encomendasFornecedores));
                materiaisStockLabel.setText(String.valueOf(materiaisStock));
                funcionariosLabel.setText(String.valueOf(funcionarios));

                carregarGraficoPie(rendimentos, despesas);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregarGraficoPie(BigDecimal rendimentos, BigDecimal despesas) {
        double valorRendimentos = rendimentos != null ? rendimentos.doubleValue() : 0;
        double valorDespesas = despesas != null ? despesas.doubleValue() : 0;
        double total = valorRendimentos + valorDespesas;

        ObservableList<PieChart.Data> dados = FXCollections.observableArrayList();

        if (total == 0) {
            dados.add(new PieChart.Data("Sem dados disponíveis", 1));
        } else {
            dados.add(new PieChart.Data("Rendimentos", valorRendimentos));
            dados.add(new PieChart.Data("Despesas", valorDespesas));
        }

        pieChart.setData(dados);
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);

        // Pequena pausa para garantir que os nodes foram criados
        PauseTransition pause = new PauseTransition(Duration.millis(150));
        pause.setOnFinished(e -> Platform.runLater(() -> {
            double totalFinal = total; // Para usar dentro da lambda
            for (PieChart.Data data : dados) {
                Node slice = data.getNode();
                if (slice == null) continue;

                String nome = data.getName();
                double valor = data.getPieValue();

                if (totalFinal != 0) {
                    double percentagem = (valor / totalFinal) * 100;
                    data.setName(String.format("%s: %.2f€ (%.1f%%)", nome, valor, percentagem));

                    if (nome.startsWith("Rendimentos")) {
                        slice.setStyle("-fx-pie-color: #4CAF50;");
                    } else if (nome.startsWith("Despesas")) {
                        slice.setStyle("-fx-pie-color: #FF5252;");
                    }
                } else {
                    slice.setStyle("-fx-pie-color: #BDBDBD;");
                }
            }

            // Coloração da legenda
            for (Node legendItem : pieChart.lookupAll(".chart-legend-item")) {
                if (legendItem instanceof Label label) {
                    String text = label.getText();
                    if (text.contains("Rendimentos")) {
                        label.setStyle("-fx-text-fill: #4CAF50;");
                    } else if (text.contains("Despesas")) {
                        label.setStyle("-fx-text-fill: #FF5252;");
                    } else {
                        label.setStyle("-fx-text-fill: #9E9E9E;");
                    }
                }
            }
        }));
        pause.play();
    }


}
