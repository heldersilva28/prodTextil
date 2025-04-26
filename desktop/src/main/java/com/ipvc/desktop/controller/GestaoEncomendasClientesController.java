package com.ipvc.desktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.models.EncomendaCliente;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GestaoEncomendasClientesController {

    @FXML private TableView<EncomendaCliente> tabelaEncomendas;
    @FXML private TableColumn<EncomendaCliente, Integer> colId;
    @FXML private TableColumn<EncomendaCliente, String> colCliente;
    @FXML private TableColumn<EncomendaCliente, String> colData;
    @FXML private TableColumn<EncomendaCliente, String> colEstado;
    @FXML private TableColumn<EncomendaCliente, String> colValorTotal;

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String apiUrl = "http://localhost:8080/api/encomendas-clientes";

    @FXML
    public void initialize() {
        configurarTabela();
        carregarEncomendas();
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("clienteNome"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataEncomenda"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoNome"));
        colValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotalFormatado"));
    }

    private void carregarEncomendas() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> {
                        try {
                            List<EncomendaCliente> encomendas = mapper.readValue(response, new TypeReference<>() {});
                            Platform.runLater(() -> {
                                tabelaEncomendas.setItems(FXCollections.observableArrayList(encomendas));
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
    private void abrirFormularioNova() {
        System.out.println("Abrir formulário de nova encomenda");
        // Aqui carregavas o formulário de criar nova encomenda
    }

    @FXML
    private void editarEncomendaSelecionada() {
        EncomendaCliente selecionada = tabelaEncomendas.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            System.out.println("Editar encomenda ID: " + selecionada.getId());
            // Carrega o formulário de edição passando a encomenda selecionada
        } else {
            mostrarAlerta("Selecione uma encomenda para editar!");
        }
    }

    @FXML
    private void apagarEncomendaSelecionada() {
        EncomendaCliente selecionada = tabelaEncomendas.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "/" + selecionada.getId()))
                    .DELETE()
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        Platform.runLater(() -> {
                            carregarEncomendas();
                            mostrarAlerta("Encomenda apagada com sucesso!");
                        });
                    });
        } else {
            mostrarAlerta("Selecione uma encomenda para apagar!");
        }
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
