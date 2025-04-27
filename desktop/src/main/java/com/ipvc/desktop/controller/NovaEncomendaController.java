package com.ipvc.desktop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class NovaEncomendaController {

    @FXML private TextField campoClienteId;
    @FXML private DatePicker campoDataEncomenda;
    @FXML private TextField campoEstadoId;
    @FXML private TextField campoValorTotal;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    private void criarEncomenda() {
        try {
            if (campoClienteId.getText().isEmpty() || campoDataEncomenda.getValue() == null ||
                    campoEstadoId.getText().isEmpty() || campoValorTotal.getText().isEmpty()) {
                mostrarAlerta("Preencha todos os campos obrigatórios!");
                return;
            }

            var body = mapper.createObjectNode();
            body.put("clienteId", Integer.parseInt(campoClienteId.getText()));
            body.put("dataEncomenda", campoDataEncomenda.getValue().toString());
            body.put("estadoId", Integer.parseInt(campoEstadoId.getText()));
            body.put("valorTotal", new BigDecimal(campoValorTotal.getText()));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/encomendas-clientes"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        System.out.println("Encomenda criada!");
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String mensagem) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
