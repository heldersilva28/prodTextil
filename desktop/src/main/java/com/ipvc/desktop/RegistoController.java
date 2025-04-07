package com.ipvc.desktop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.models.AuthResponse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RegistoController {
    @FXML
    private TextField nomeField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    private void onRegistarClick() {
        try {
            String nome = nomeField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            var json = String.format("""
            {
                "nome": "%s",
                "email": "%s",
                "password": "%s",
                "tipoUtilizadorId": 1
            }
            """, nome, email, password);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/auth/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            AuthResponse authResponse = mapper.readValue(response.body(), AuthResponse.class);

            Alert alert = new Alert(authResponse.isSuccess() ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            alert.setContentText(authResponse.getMessage());
            alert.showAndWait();

            // Se o registo foi bem-sucedido, redireciona para login
            if (authResponse.isSuccess()) {
                // Fecha a janela atual
                nomeField.getScene().getWindow().hide();

                // Abre o login.fxml
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
                Scene loginScene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("Iniciar Sessão");
                stage.setScene(loginScene);
                stage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao registar utilizador.");
            alert.showAndWait();
        }
    }

}
