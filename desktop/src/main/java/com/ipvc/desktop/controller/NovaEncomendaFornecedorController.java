package com.ipvc.desktop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.models.EstadoEncomenda;
import com.ipvc.desktop.models.Fornecedor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

public class NovaEncomendaFornecedorController {

    @FXML private VBox rootVBox;
    @FXML private ComboBox<Fornecedor> campoFornecedorId;
    @FXML private DatePicker campoDataEncomenda;
    @FXML private ComboBox<EstadoEncomenda> campoEstadoId;
    @FXML private TextField campoValorTotal;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    public void initialize() {
        carregarFornecedores();
        carregarEstados();
    }

    private void carregarFornecedores() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/fornecedores"))
                .GET()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    try {
                        List<Fornecedor> fornecedores = mapper.readValue(
                                response.body(),
                                mapper.getTypeFactory().constructCollectionType(List.class, Fornecedor.class)
                        );

                        Platform.runLater(() -> campoFornecedorId.getItems().setAll(fornecedores));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void carregarEstados() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/estados-encomenda"))
                .GET()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    try {
                        List<EstadoEncomenda> estados = mapper.readValue(
                                response.body(),
                                mapper.getTypeFactory().constructCollectionType(List.class, EstadoEncomenda.class)
                        );

                        Platform.runLater(() -> campoEstadoId.getItems().setAll(estados));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @FXML
    private void criarEncomendaFornecedor() {
        try {
            Fornecedor fornecedor = campoFornecedorId.getValue();
            LocalDate data = campoDataEncomenda.getValue();
            EstadoEncomenda estado = campoEstadoId.getValue();
            String valorTexto = campoValorTotal.getText().replace(",", ".");

            if (fornecedor == null || data == null || estado == null || valorTexto.isBlank()) {
                mostrarAlerta("Preencha todos os campos.");
                return;
            }

            BigDecimal valor = new BigDecimal(valorTexto);

            var body = mapper.createObjectNode();
            body.put("fornecedorId", fornecedor.getId());
            body.put("dataEncomenda", data.toString());
            body.put("estadoId", estado.getId());
            body.put("valorTotal", valor);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/encomendas-fornecedores"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> Platform.runLater(() -> {
                        mostrarAlerta("Encomenda criada com sucesso!");
                        fecharJanela();
                    }));

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao criar encomenda.");
        }
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void fecharJanela() {
        Stage stage = (Stage) rootVBox.getScene().getWindow();
        stage.close();
    }
}
