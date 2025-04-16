package com.ipvc.desktop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.Response.AuthResponse;
import com.ipvc.desktop.Request.RegisterRequest;
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

public class RegistoController {
    @FXML private TextField inputNome;
    @FXML private TextField inputEmail;
    @FXML private PasswordField inputPassword;
    @FXML private ComboBox<TipoItem> tipoComboBox;
    @FXML private VBox registoVBox;
    @FXML private Label errorLabel;

    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    public void initialize() {
        tipoComboBox.getItems().addAll(
                new TipoItem(1, "Admin"),
                new TipoItem(2, "Funcionário")
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
        TipoItem tipo = tipoComboBox.getValue();

        if (nome.isEmpty() || email.isEmpty() || password.isEmpty() || tipo == null) {
            errorLabel.setText("Preenche todos os campos");
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
                    showToast(resp.getMessage(), this::irParaLogin);
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

    private void irParaLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/views/login.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 800, 500);
            scene.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/com/ipvc/desktop/style/login.css")).toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.setResizable(false);
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
