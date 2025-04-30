package com.ipvc.desktop.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.models.EstadoEncomenda;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NovaEncomendaController {

    @FXML private VBox rootVBox;
    @FXML private ComboBox<String> campoClienteId;  // Alterado para ComboBox
    @FXML private DatePicker campoDataEncomenda;
    @FXML private ComboBox<EstadoEncomenda> campoEstadoId;   // Alterado para ComboBox
    @FXML private TextField campoValorTotal;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private Map<String, Integer> emailParaIdMap = new HashMap<>();

    @FXML
    public void initialize() {
        carregarClientes();
        campoDataEncomenda.setValue(LocalDate.now());
        carregarEstados();
    }

    private void carregarClientes() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/clientes/emails"))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> {
                        try {
                            JsonNode jsonNode = mapper.readTree(response);
                            List<String> emails = new ArrayList<>();

                            // Percorrer as chaves (IDs) do JSON
                            jsonNode.fieldNames().forEachRemaining(idStr -> {
                                String email = jsonNode.get(idStr).asText();
                                int id = Integer.parseInt(idStr);
                                emails.add(email);
                                emailParaIdMap.put(email, id); // Associa email ao ID
                            });

                            Platform.runLater(() -> {
                                campoClienteId.getItems().setAll(emails);
                                campoClienteId.getSelectionModel().select(0);
                            });

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
                shakeNode(rootVBox);
                showToast("Por favor, selecione um cliente.");
                return;
            }

            if (campoDataEncomenda.getValue() == null) {
                shakeNode(rootVBox);
                showToast("Por favor, selecione a data da encomenda.");
                return;
            }
            String dataTexto = campoDataEncomenda.getEditor().getText().trim();

            LocalDate dataEncomenda;
            try {
                dataEncomenda = LocalDate.parse(dataTexto);
            } catch (Exception e) {
                shakeNode(rootVBox);
                showToast("Data inválida. Use o formato AAAA-MM-DD.");
                return;
            }


            if (campoEstadoId.getValue() == null) {
                shakeNode(rootVBox);
                showToast("Por favor, selecione um estado.");
                return;
            }

            String valorTexto = campoValorTotal.getText().trim();
            if (valorTexto.isEmpty()) {
                shakeNode(rootVBox);
                showToast("Por favor, insira o valor total.");
                return;
            }

            BigDecimal valorTotal;
            try {
                valorTotal = new BigDecimal(valorTexto.replace(",", "."));
            } catch (NumberFormatException e) {
                shakeNode(rootVBox);
                showToast("Valor total inválido. Ex: 123.45");
                return;
            }

            String emailSelecionado = campoClienteId.getValue();
            Integer clienteId = emailParaIdMap.get(emailSelecionado);

            if (clienteId == null) {
                showToast("Cliente inválido selecionado.");
                return;
            }

            var body = mapper.createObjectNode();
            body.put("clienteId", clienteId);
            body.put("dataEncomenda", dataEncomenda.toString());
            body.put("estadoId", campoEstadoId.getValue().getId());
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
                            // Esperar antes de fechar para mostrar o toast
                            new Thread(() -> {
                                try {
                                    Thread.sleep(2000); // espera 2 segundos
                                    Platform.runLater(() -> {
                                        // Fecha a janela atual
                                        Stage stage = (Stage) rootVBox.getScene().getWindow();
                                        stage.close();
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        });
                    });

        } catch (Exception e) {
            e.printStackTrace();
            showToast("Erro ao criar a encomenda.");
        }
    }

    private void showToast(String message) {
        Label toast = new Label(message);
        toast.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 5;");
        StackPane container = new StackPane(toast);
        container.setStyle("-fx-alignment: center;");

        rootVBox.getChildren().add(container);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), toast);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), toast);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(2));

        fadeOut.setOnFinished(e -> rootVBox.getChildren().remove(container));

        fadeIn.play();
        fadeOut.play();
    }

    private void shakeNode(VBox node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(70), node);
        tt.setFromX(-10);
        tt.setByX(20);
        tt.setCycleCount(4);
        tt.setAutoReverse(true);
        tt.play();
    }
}
