package com.ipvc.desktop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.models.EncomendaFornecedor;
import com.ipvc.desktop.models.EstadoEncomenda;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import java.util.List;

public class EditarEncomendaFornecedorController {

    @FXML private DatePicker campoDataEncomenda;
    @FXML private ComboBox<EstadoEncomenda> campoEstadoId;
    @FXML private TextField campoValorTotal;
    @FXML private VBox rootVBox;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private EncomendaFornecedor encomendaAtual;

    public void setEncomenda(EncomendaFornecedor encomenda) {
        this.encomendaAtual = encomenda;
        campoDataEncomenda.setValue(encomenda.getDataEncomenda());
        campoValorTotal.setText(String.format("%.2f", encomenda.getValorTotal()));

        httpClient.sendAsync(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/estados-encomenda"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        ).thenAccept(response -> {
            try {
                List<EstadoEncomenda> estados = mapper.readValue(
                        response.body(),
                        mapper.getTypeFactory().constructCollectionType(List.class, EstadoEncomenda.class)
                );

                Platform.runLater(() -> {
                    campoEstadoId.getItems().setAll(estados);
                    for (EstadoEncomenda estado : estados) {
                        if (estado.getId().equals(encomenda.getEstadoId())) {
                            campoEstadoId.setValue(estado);
                            break;
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void initialize() {
        FadeTransition ft = new FadeTransition(Duration.millis(800), rootVBox);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        // Adicionar filtro para permitir apenas números, "," e "."
        campoValorTotal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9.,]*")) {
                campoValorTotal.setText(oldValue);
            }
        });
    }

    @FXML
    private void atualizarEncomenda() {
        try {
            if (campoDataEncomenda.getValue() == null) {
                showToast("Por favor, selecione a data do pedido.");
                shakeNode(rootVBox);
                return;
            }

            LocalDate dataPedido = campoDataEncomenda.getValue();

            if (campoEstadoId.getValue() == null) {
                showToast("Por favor, selecione um estado.");
                shakeNode(rootVBox);
                return;
            }

            String valorTexto = campoValorTotal.getText().trim();
            if (valorTexto.isEmpty()) {
                showToast("Por favor, insira o valor total.");
                shakeNode(rootVBox);
                return;
            }

            // Verificar se o valor está no formato correto
            if (!valorTexto.matches("\\d+(,\\d{1,2})?|\\d+(\\.\\d{1,2})?")) {
                showToast("Valor total inválido. Use o formato 123,45 ou 123.45.");
                shakeNode(rootVBox);
                return;
            }

            BigDecimal valorTotal;
            try {
                valorTotal = new BigDecimal(valorTexto.replace(",", "."));
            } catch (NumberFormatException e) {
                showToast("Valor total inválido. Ex: 123,45 ou 123.45");
                shakeNode(rootVBox);
                return;
            }

            var body = mapper.createObjectNode();
            body.put("data_pedido", dataPedido.toString());
            body.put("estado_id", campoEstadoId.getValue().getId());
            body.put("valor_total", valorTotal);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/encomendas-fornecedores/" + encomendaAtual.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> Platform.runLater(() -> {
                        showToast("Encomenda atualizada com sucesso!");

                        new Thread(() -> {
                            try {
                                Thread.sleep(2000);
                                Platform.runLater(() -> {
                                    Stage stage = (Stage) rootVBox.getScene().getWindow();
                                    stage.close();
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }));

        } catch (Exception e) {
            e.printStackTrace();
            showToast("Erro ao atualizar encomenda.");
            shakeNode(rootVBox);
        }
    }

    private void shakeNode(VBox node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(70), node);
        tt.setFromX(-10);
        tt.setByX(20);
        tt.setCycleCount(4);
        tt.setAutoReverse(true);
        tt.play();
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
}
