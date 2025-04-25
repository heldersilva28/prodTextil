package com.ipvc.desktop.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EstatisticasMateriaisController {

    @FXML private Label labelTotalMateriais;
    @FXML private Label labelBaixoStock;
    @FXML private BarChart<String, Number> graficoStockPorCategoria;
    @FXML private ComboBox<String> comboBoxGrafico;
    @FXML private NumberAxis eixoY;


    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String url = "http://localhost:8080/api/materiais/estatisticas";
    private JsonNode porCategoria;
    private JsonNode valorPorCategoria;

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
                        labelTotalMateriais.setText("Erro");
                        labelBaixoStock.setText("Erro");
                    });
                    return null;
                });
    }

    private void atualizarUI(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode data = mapper.readTree(json);

            int total = data.has("total") ? data.get("total").asInt() : 0;
            int baixoStock = data.has("baixoStock") ? data.get("baixoStock").asInt() : 0;
            porCategoria = data.has("porCategoria") ? data.get("porCategoria") : mapper.createObjectNode();
            valorPorCategoria = data.has("valorPorCategoria") ? data.get("valorPorCategoria") : mapper.createObjectNode();

            Platform.runLater(() -> {
                labelTotalMateriais.setText(String.valueOf(total));
                labelBaixoStock.setText(String.valueOf(baixoStock));

                // Agora que as estatísticas estão carregadas, podemos definir a seleção do ComboBox
                comboBoxGrafico.getSelectionModel().selectFirst(); // Seleciona "Stock" por padrão
                preencherGrafico(); // Atualiza o gráfico com o valor padrão (Stock)
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preencherGrafico() {
        String selectedOption = comboBoxGrafico.getSelectionModel().getSelectedItem();
        if (selectedOption == null) return;

        graficoStockPorCategoria.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<String> categorias = new ArrayList<>();

        // Alterar o título do eixo Y com base na seleção do ComboBox
        if (selectedOption.equals("Stock")) {
            series.setName("Stock disponível");

            // Alterar título do eixo Y para "Quantidade" para o gráfico de stock
            eixoY.setLabel("Stock");

            // Itera pelos dados da categoria e assegura que cada categoria é bem representada
            porCategoria.fields().forEachRemaining(entrada -> {
                String categoria = entrada.getKey();
                int quantidade = entrada.getValue().asInt();
                series.getData().add(new XYChart.Data<>(categoria, quantidade));
                categorias.add(categoria);
            });

        } else if (selectedOption.equals("Valor")) {
            series.setName("Valor por Categoria");

            // Alterar título do eixo Y para "Valor" para o gráfico de valor
            eixoY.setLabel("Valor");

            // Itera pelos dados de valor por categoria
            valorPorCategoria.fields().forEachRemaining(entrada -> {
                String categoria = entrada.getKey();
                BigDecimal valor = new BigDecimal(entrada.getValue().asText());
                series.getData().add(new XYChart.Data<>(categoria, valor));
                categorias.add(categoria);
            });
        }

        graficoStockPorCategoria.getData().add(series);

        // Ajustar o gráfico para garantir que ele se redimensione adequadamente
        int numCategorias = categorias.size();
        graficoStockPorCategoria.setMinWidth(Math.max(800, numCategorias * 100)); // Definindo largura mínima

        // Ajustar o espaço entre as barras
        graficoStockPorCategoria.setCategoryGap(20);  // Aumenta o espaço entre as categorias
        graficoStockPorCategoria.setBarGap(10); // Reduz a distância entre as barras

        // Ajustar rotação dos labels do eixo X para evitar sobreposição
        if (graficoStockPorCategoria.getXAxis() instanceof CategoryAxis eixoX) {
            eixoX.setTickLabelRotation(45);  // Rotaciona os labels do eixo X para facilitar a leitura
            eixoX.setTickLabelFont(new javafx.scene.text.Font("System", 10)); // Diminui a fonte dos labels para melhor ajuste
        }

        // Adicionar os valores visuais acima de cada barra para mostrar o total
        Platform.runLater(() -> {
            for (XYChart.Series<String, Number> s : graficoStockPorCategoria.getData()) {
                for (XYChart.Data<String, Number> data : s.getData()) {
                    Node node = data.getNode();
                    if (node != null) {
                        // Cria um label com o valor correspondente à altura da barra
                        Label label = new Label(String.valueOf(data.getYValue()));
                        label.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: black;");
                        label.setTranslateY(-20);  // Ajusta o posicionamento do label acima da barra

                        StackPane stackPane = (StackPane) node;
                        stackPane.getChildren().add(label);
                    }
                }
            }
        });
    }

    // Método para alternar entre Stock e Valor
    @FXML
    private void alternarGrafico() {
        preencherGrafico();
    }
}