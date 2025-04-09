package com.ipvc.desktop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.models.AuthResponse;
import com.ipvc.desktop.models.RegisterRequest;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class RegistoController {
    @FXML private TextField inputNome;
    @FXML private TextField inputEmail;
    @FXML private PasswordField inputPassword;
    @FXML private ComboBox<TipoItem> tipoComboBox;

    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    public void initialize() {
        // Estes valores podem vir de uma chamada à API se quiseres, para já deixo hardcoded
        tipoComboBox.getItems().addAll(
                new TipoItem(1, "Admin"),
                new TipoItem(2, "Funcionário")
        );
    }

    @FXML
    public void criarUtilizador() {
        try {
            String nome = inputNome.getText();
            String email = inputEmail.getText();
            String password = inputPassword.getText();
            TipoItem tipo = tipoComboBox.getValue();

            if (nome.isEmpty() || email.isEmpty() || password.isEmpty() || tipo == null) {
                new Alert(Alert.AlertType.WARNING, "Preenche todos os campos").showAndWait();
                return;
            }

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

            Alert alert = new Alert(resp.isSuccess() ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR, resp.getMessage());
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erro ao criar utilizador").showAndWait();
        }
    }

    private record TipoItem(int id, String nome) {
        @Override
        public String toString() {
            return nome;
        }
    }
}
