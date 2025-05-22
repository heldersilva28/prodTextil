package com.ipvc.desktop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.Response.AuthResponse;
import com.ipvc.desktop.Request.RegisterRequest;
import com.ipvc.desktop.models.Utilizador;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class NovoUtilizadorController {
    @FXML private TextField inputNome;
    @FXML private TextField inputEmail;
    @FXML private PasswordField inputPassword;
    @FXML private PasswordField inputPasswordConfirm;
    @FXML private ComboBox<TipoItem> tipoComboBox;
    @FXML private VBox registoVBox;
    @FXML private Label errorLabel;

    private final ObjectMapper mapper = new ObjectMapper();
    private final Utilizador utilizador = new Utilizador();

    @FXML
    public void initialize() {
        tipoComboBox.getItems().addAll(
                new TipoItem(1, utilizador.getTipoUtilizadorNomeFromApi(1)),
                new TipoItem(2, utilizador.getTipoUtilizadorNomeFromApi(2)),
                new TipoItem(3,utilizador.getTipoUtilizadorNomeFromApi(3))
        );

        FadeTransition ft = new FadeTransition(Duration.millis(800), registoVBox);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    @FXML
    public void criarUtilizador() {
        String nome = inputNome.getText();
        String email = inputEmail.getText();
        String password = inputPassword.getText();
        String passwordConfirm = inputPasswordConfirm.getText();
        TipoItem tipo = tipoComboBox.getValue();

        if (nome.isEmpty() || email.isEmpty() || password.isEmpty() || tipo == null || passwordConfirm.isEmpty()) {
            errorLabel.setText("Preenche todos os campos");
            errorLabel.setVisible(true);
            shakeNode(registoVBox);
            return;
        } else if (!password.equals(passwordConfirm)) {
            errorLabel.setText("Palavras-Passe diferentes");
            errorLabel.setVisible(true);
            shakeNode(registoVBox);
            return;
        }
        try {
            RegisterRequest req = new RegisterRequest(email, password, nome, tipo.id());
            String json = mapper.writeValueAsString(req);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/auth/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            AuthResponse resp = mapper.readValue(response.body(), AuthResponse.class);

            if (resp.isSuccess()) {
                errorLabel.setVisible(false);
                showToast(resp.getMessage(), this::irParaPainelAdmin);
            } else {
                errorLabel.setText(resp.getMessage());
                errorLabel.setVisible(true);
                shakeNode(registoVBox);
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erro ao criar utilizador");
            errorLabel.setVisible(true);
            shakeNode(registoVBox);
        }
    }

    private void irParaPainelAdmin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/views/painel-admin.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/com/ipvc/desktop/style/painel-admin.css")).toExternalForm());

            Stage stage = new Stage();
            stage.isFullScreen();
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();

            Stage currentStage = (Stage) registoVBox.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
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

    private void showToast(String message, Runnable onToastDismissed) {
        Label toast = new Label(message);
        toast.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-text-fill: white; -fx-padding: 10;");

        StackPane root = new StackPane();
        root.getChildren().add(toast);
        registoVBox.getChildren().add(root);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), toast);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), toast);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(2));

        fadeOut.setOnFinished(e -> {
            registoVBox.getChildren().remove(root);
            onToastDismissed.run();
        });

        fadeIn.play();
        fadeOut.play();
    }

    private record TipoItem(int id, String nome) {
        @Override
        public String toString() {
            return nome;
        }
    }
}
