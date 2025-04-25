package com.ipvc.desktop.controller;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
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
    @FXML private Label lucroLabel;
    @FXML private Label clientesLabel;

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
    private PainelAdminController parentController;
    public void setParentController(PainelAdminController parentController) {
        this.parentController = parentController;
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
                        clientesLabel.setText("Erro");
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
            long cliente = data.get("totalClientes").asLong();

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
                clientesLabel.setText(String.valueOf(cliente));

                BigDecimal lucro = rendimentos.subtract(despesas);
                BigDecimal percentagem = despesas.compareTo(BigDecimal.ZERO) != 0
                        ? lucro.multiply(BigDecimal.valueOf(100)).divide(despesas, 2, BigDecimal.ROUND_HALF_UP)
                        : BigDecimal.ZERO;

                String corLucro = lucro.compareTo(BigDecimal.ZERO) >= 0 ? "#4CAF50" : "#FF5252";
                String lucroTexto = String.format("%+.2f€ (%.1f%%)", lucro.doubleValue(), percentagem.doubleValue());

                lucroLabel.setText(lucroTexto);
                lucroLabel.setStyle("-fx-text-fill: " + corLucro + "; -fx-font-weight: bold;");


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
            double totalFinal = total;
            for (PieChart.Data data : dados) {
                Node slice = data.getNode();
                if (slice == null) continue;

                String nomeBase = data.getName().split(":")[0];
                double valor = data.getPieValue();

                if (totalFinal != 0) {
                    double percentagem = (valor / totalFinal) * 100;
                    data.setName(String.format("%s: %.2f€ (%.1f%%)", nomeBase, valor, percentagem));

                    if (nomeBase.equals("Rendimentos")) {
                        slice.setStyle("-fx-pie-color: #4CAF50;");
                    } else if (nomeBase.equals("Despesas")) {
                        slice.setStyle("-fx-pie-color: #FF5252;");
                    }
                } else {
                    slice.setStyle("-fx-pie-color: #BDBDBD;");
                }
            }

            // Nova pausa para garantir que a legenda foi renderizada
            PauseTransition legendaDelay = new PauseTransition(Duration.millis(100));
            legendaDelay.setOnFinished(ev -> Platform.runLater(() -> {
                for (Node legendItem : pieChart.lookupAll(".chart-legend-item")) {
                    Node symbol = legendItem.lookup(".chart-legend-item-symbol");
                    if (symbol != null && legendItem instanceof Label label) {
                        String text = label.getText();
                        if (text.contains("Rendimentos")) {
                            label.setStyle("-fx-text-fill: #4CAF50;");
                            symbol.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 10;");
                        } else if (text.contains("Despesas")) {
                            label.setStyle("-fx-text-fill: #FF5252;");
                            symbol.setStyle("-fx-background-color: #FF5252; -fx-background-radius: 10;");
                        } else {
                            label.setStyle("-fx-text-fill: #9E9E9E;");
                            symbol.setStyle("-fx-background-color: #BDBDBD;");
                        }
                    }
                }
            }));
            legendaDelay.play();

        }));
        pause.play();
    }

    @FXML
    private void abrirStatsEncomendasClientes() {
        parentController.carregarConteudo("/com/ipvc/desktop/views/estatisticas-encomendas-clientes.fxml","/com/ipvc/desktop/style/estatisticas-encomendas-clientes.css");
    }

    @FXML
    private void abrirStatsEncomendasFornecedores() {
        parentController.carregarConteudo("/com/ipvc/desktop/views/estatisticas-encomendas-fornecedores.fxml","/com/ipvc/desktop/style/estatisticas-encomendas-clientes.css");
    }

    @FXML
    private void abrirStatsMateriais() {
        parentController.carregarConteudo("/com/ipvc/desktop/views/estatisticas-materiais.fxml","/com/ipvc/desktop/style/estatisticas-materiais.css");
    }

}
