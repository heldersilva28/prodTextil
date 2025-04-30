package com.ipvc.desktop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.models.TipoItem;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class EditarCargoUtilizadorController {

    @FXML
    private ComboBox<TipoItem> tipoComboBox;

    @FXML
    private VBox rootVBox;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private int utilizadorId; // Este ID deve ser passado ao abrir este controller

    public void setUtilizadorId(int id) {
        this.utilizadorId = id;
        System.out.println(">>> ID do utilizador definido no controller: " + utilizadorId);
    }

    @FXML
    public void initialize() {
        carregarTiposDeUtilizador();
    }

    private void carregarTiposDeUtilizador() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/tipos-utilizadores"))
                    .GET()
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> {
                        try {
                            List<TipoItem> tipos = mapper.readValue(
                                    response,
                                    mapper.getTypeFactory().constructCollectionType(List.class, TipoItem.class)
                            );
                            Platform.runLater(() -> {
                                tipoComboBox.getItems().setAll(tipos);
                                tipoComboBox.getSelectionModel().selectFirst();
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void atualizarCargo() {
        TipoItem tipoSelecionado = tipoComboBox.getValue();

        if (tipoSelecionado == null) {
            mostrarAlerta("Selecione um cargo válido.");
            return;
        }

        try {
            System.out.println(">>> Enviando atualização de cargo:");
            System.out.println("ID do utilizador: " + utilizadorId);
            System.out.println("ID do novo cargo: " + tipoSelecionado.getId());
            System.out.println("Nome do novo cargo: " + tipoSelecionado.getNome());

            var body = mapper.createObjectNode();
            body.put("tipoUtilizadorId", tipoSelecionado.getId());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/utilizadores/" + utilizadorId))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> Platform.runLater(() -> {
                        mostrarAlerta("Cargo atualizado com sucesso!");
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
                    }));

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao atualizar o cargo.");
        }
    }

    private void mostrarAlerta(String message) {
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
