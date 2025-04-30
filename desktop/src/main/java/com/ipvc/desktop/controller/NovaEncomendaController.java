package com.ipvc.desktop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.models.EstadoEncomenda;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.util.List;

public class NovaEncomendaController {

    @FXML private VBox rootVBox;
    @FXML private ComboBox<String> campoClienteId;  // Alterado para ComboBox
    @FXML private DatePicker campoDataEncomenda;
    @FXML private ComboBox<EstadoEncomenda> campoEstadoId;   // Alterado para ComboBox
    @FXML private TextField campoValorTotal;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private int idCliente;

    @FXML
    public void initialize() {
        carregarClientes();
        campoDataEncomenda.setValue(LocalDate.now());
        carregarEstados();
    }

    private void carregarClientes() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/clientes/emails")) // Alterado para /emails
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> {
                        try {
                            // A resposta é uma lista de e-mails
                            List<String> clientes = mapper.readValue(response, List.class);
                            Platform.runLater(() -> campoClienteId.getItems().setAll(clientes)); // Preencher ComboBox com os e-mails
                            campoClienteId.getSelectionModel().select(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregarEstados() {
        httpClient.sendAsync(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/estados-encomenda"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        ).thenAccept(response -> {
            try {
                // Parse da resposta para a lista de objetos EstadoEncomenda
                List<EstadoEncomenda> estados = mapper.readValue(
                        response.body(),
                        mapper.getTypeFactory().constructCollectionType(List.class, EstadoEncomenda.class)
                );

                // Executa o código na thread de UI (JavaFX)
                Platform.runLater(() -> {
                    // Preenche a ComboBox com os estados
                    campoEstadoId.getItems().setAll(estados);
                    campoEstadoId.getSelectionModel().select(0);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



    @FXML
    private void criarEncomenda() {
        try {
            // Verificações de validação
            if (campoClienteId.getValue() == null) {
                shakeNode(campoClienteId);
                showToast("Por favor, selecione um cliente.");
                return;
            }

            if (campoDataEncomenda.getValue() == null) {
                shakeNode(campoDataEncomenda);
                showToast("Por favor, selecione a data da encomenda.");
                return;
            }

            if (campoEstadoId.getValue() == null) {
                shakeNode(campoEstadoId);
                showToast("Por favor, selecione um estado.");
                return;
            }

            String valorTexto = campoValorTotal.getText().trim();
            if (valorTexto.isEmpty()) {
                shakeNode(campoValorTotal);
                showToast("Por favor, insira o valor total.");
                return;
            }

            BigDecimal valorTotal;
            try {
                valorTotal = new BigDecimal(valorTexto.replace(",", "."));
            } catch (NumberFormatException e) {
                shakeNode(campoValorTotal);
                showToast("Valor total inválido. Ex: 123.45");
                return;
            }

            var body = mapper.createObjectNode();
            body.put("clienteId", idCliente); // Usar e-mail do cliente
            body.put("dataEncomenda", campoDataEncomenda.getValue().toString());
            body.put("estadoId", campoEstadoId.getValue().getId());   // Usar nome do estado
            body.put("valorTotal", valorTotal);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/encomendas-clientes"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        Platform.runLater(() -> {
                            showToast("Encomenda criada com sucesso!");
                            limparCampos();
                        });
                    });

        } catch (Exception e) {
            e.printStackTrace();
            showToast("Erro ao criar a encomenda.");
        }
    }

    private void showToast(String mensagem) {
        // Exibir uma mensagem de toast
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.setResizable(true);
        alert.showAndWait();
    }

    private void shakeNode(javafx.scene.Node node) {
        // Criar o efeito "shake"
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0), e -> node.setTranslateX(0)),
                new KeyFrame(Duration.millis(50), e -> node.setTranslateX(10)),
                new KeyFrame(Duration.millis(100), e -> node.setTranslateX(-10)),
                new KeyFrame(Duration.millis(150), e -> node.setTranslateX(10)),
                new KeyFrame(Duration.millis(200), e -> node.setTranslateX(-10)),
                new KeyFrame(Duration.millis(250), e -> node.setTranslateX(10)),
                new KeyFrame(Duration.millis(300), e -> node.setTranslateX(0))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void limparCampos() {
        campoClienteId.getSelectionModel().clearSelection();
        campoDataEncomenda.setValue(null);
        campoEstadoId.getSelectionModel().clearSelection();
        campoValorTotal.clear();
    }
}
