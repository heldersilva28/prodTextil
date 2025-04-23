package com.ipvc.desktop.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

public class EstatisticasEncomendasFornecedoresController {

    @FXML
    private Label labelTotal;

    @FXML
    private Label labelPendentes;

    @FXML
    private Label labelConcluidas;

    @FXML
    private BarChart<String, Number> graficoMensal;


    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String url = "http://localhost:8080/api/encomendas-fornecedores/estatisticas";

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
                        labelTotal.setText("Erro");
                        labelPendentes.setText("Erro");
                        labelConcluidas.setText("Erro");
                    });
                    return null;
                });
    }

    private void atualizarUI(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode data = mapper.readTree(json);

            int total = data.has("total") ? data.get("total").asInt() : 0;
            int pendentes = data.has("pendentes") ? data.get("pendentes").asInt() : 0;
            int concluidas = data.has("concluidas") ? data.get("concluidas").asInt() : 0;
            JsonNode porMes = data.get("porMes") != null ? data.get("porMes") : mapper.createObjectNode();

            Platform.runLater(() -> {
                labelTotal.setText(String.valueOf(total));
                labelPendentes.setText(String.valueOf(pendentes));
                labelConcluidas.setText(String.valueOf(concluidas));

                preencherGraficoPorMes(porMes);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private CategoryAxis eixox;

    private void preencherGraficoPorMes(JsonNode porMes) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Encomendas por Mês");

        Locale localePt = new Locale("pt", "PT");

        List<String> mesesOrdenados = new ArrayList<>();
        for (Month mes : Month.values()) {
            String nomeMes = mes.getDisplayName(TextStyle.FULL, localePt);
            mesesOrdenados.add(nomeMes);
        }

        eixox.setCategories(FXCollections.observableArrayList(mesesOrdenados));

        for (String mes : mesesOrdenados) {
            int quantidade = porMes.has(mes) ? porMes.get(mes).asInt() : 0;
            XYChart.Data<String, Number> data = new XYChart.Data<>(mes, quantidade);
            series.getData().add(data);
        }

        graficoMensal.getData().clear();
        graficoMensal.getData().add(series);
        eixox.setTickLabelRotation(45);

        // Adicionar os labels depois do gráfico ser desenhado
        Platform.runLater(() -> {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node node = data.getNode();
                if (node != null) {
                    Label label = new Label(String.valueOf(data.getYValue()));
                    label.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

                    // Posicionar o label acima da barra
                    StackPane stackPane = (StackPane) node;
                    stackPane.getChildren().add(label);
                    label.setTranslateY(-20);
                }
            }
        });
    }
}
