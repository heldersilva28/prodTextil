package com.ipvc.desktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ipvc.desktop.models.EncomendaCliente;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    //private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String apiUrl = "http://localhost:8080/api/encomendas-clientes";

    private final ObjectMapper mapper;

    public GestaoEncomendasClientesController() {
        // Registra o módulo para lidar com datas do Java 8
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);  // Desabilita a escrita de datas como timestamps
    }

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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/views/nova-encomenda.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            // Carregar CSS
            scene.getStylesheets().add(getClass().getResource("/com/ipvc/desktop/style/nova-encomenda.css").toExternalForm());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Adicionar Encomenda");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            carregarEncomendas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editarEncomendaSelecionada() {
        EncomendaCliente selecionada = tabelaEncomendas.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/views/editar-encomenda.fxml"));
                Parent root = loader.load();

                // Obter controller e passar dados
                EditarEncomendaController controller = loader.getController();
                controller.setEncomenda(selecionada);

                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/com/ipvc/desktop/style/editar-encomenda.css").toExternalForm());

                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Edição de Encomenda");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                carregarEncomendas();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Selecione uma encomenda para editar!");
        }
    }


    @FXML
    private void apagarEncomendaSelecionada() {
        EncomendaCliente selecionada = tabelaEncomendas.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarAlerta("Selecione uma encomenda para apagar!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/views/confirmacao.fxml"));
            Parent root = loader.load();
            ConfirmacaoController controller = loader.getController();
            controller.setMensagem("Tem a certeza que pretende apagar esta encomenda?");
            controller.setOnConfirm(() -> confirmarApagarEncomenda(selecionada.getId()));

            Scene scene = new Scene(root);

            // Carregar CSS
            scene.getStylesheets().add(getClass().getResource("/com/ipvc/desktop/style/confirmacao.css").toExternalForm());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Confirmação");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void confirmarApagarEncomenda(int encomendaId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/encomendas-clientes/" + encomendaId))
                    .DELETE()
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        System.out.println("Encomenda apagada com sucesso!");
                        carregarEncomendas(); // Atualizar tabela
                    });

        } catch (Exception e) {
            e.printStackTrace();
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
