package com.ipvc.desktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ipvc.desktop.models.EncomendaFornecedor;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GestaoEncomendasFornecedoresController {

    @FXML private TableView<EncomendaFornecedor> tabelaEncomendas;
    @FXML private TableColumn<EncomendaFornecedor, Integer> colId;
    @FXML private TableColumn<EncomendaFornecedor, String> colFornecedor;
    @FXML private TableColumn<EncomendaFornecedor, String> colData;
    @FXML private TableColumn<EncomendaFornecedor, String> colEstado;
    @FXML private TableColumn<EncomendaFornecedor, String> colValorTotal;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String apiUrl = "http://localhost:8080/api/encomendas-fornecedores";

    private final ObjectMapper mapper;

    public GestaoEncomendasFornecedoresController() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarEncomendas();
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFornecedor.setCellValueFactory(new PropertyValueFactory<>("fornecedorNome"));
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
                            List<EncomendaFornecedor> encomendas = mapper.readValue(response, new TypeReference<>() {});
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/views/nova-encomenda-fornecedor.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/ipvc/desktop/style/nova-encomenda.css").toExternalForm());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Adicionar Encomenda de Fornecedor");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            carregarEncomendas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editarEncomendaSelecionada() {
        EncomendaFornecedor selecionada = tabelaEncomendas.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/views/editar-encomenda-fornecedor.fxml"));
                Parent root = loader.load();

                EditarEncomendaFornecedorController controller = loader.getController();
                controller.setEncomenda(selecionada);

                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/com/ipvc/desktop/style/editar-encomenda.css").toExternalForm());

                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Edição de Encomenda de Fornecedor");
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
        EncomendaFornecedor selecionada = tabelaEncomendas.getSelectionModel().getSelectedItem();
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
                    .uri(URI.create(apiUrl + "/" + encomendaId))
                    .DELETE()
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        System.out.println("Encomenda apagada com sucesso!");
                        carregarEncomendas();
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
