package com.ipvc.desktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ipvc.desktop.models.Cliente;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

public class GestaoClientesController {

    // Registra o módulo para suporte a tipos de data/hora do Java 8
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @FXML
    private TableView<Cliente> tabelaUtilizadores;
    @FXML
    private TableColumn<Cliente, String> colNome;
    @FXML
    private TableColumn<Cliente, String> colEmail;
    @FXML
    private TableColumn<Cliente, String> colTelemovel;
    @FXML
    private TableColumn<Cliente, String> colMorada;
    @FXML
    private TableColumn<Cliente, String> colCodPostal;

    @FXML
    private TextField campoPesquisa;

    private final String apiUtilizadores = "http://localhost:8080/api/clientes";

    private List<Cliente> todosUtilizadores = new ArrayList<>();

    @FXML
    private BorderPane border;

    @FXML
    public void initialize() {
        FadeTransition ft = new FadeTransition(Duration.millis(800), border);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        configurarTabela();
        carregarUtilizadores();

    }

    private PainelAdminController parentController;

    public void setParentController(PainelAdminController parentController) {
        this.parentController = parentController;
    }

    private void configurarTabela() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelemovel.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colMorada.setCellValueFactory(new PropertyValueFactory<>("morada"));
        colCodPostal.setCellValueFactory(new PropertyValueFactory<>("codigo_postal"));
    }

    private void carregarUtilizadores() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUtilizadores))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    try {
                        todosUtilizadores = mapper.readValue(response, new TypeReference<>() {
                        });
                        atualizarTabela(todosUtilizadores);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void atualizarTabela(List<Cliente> lista) {
        ObservableList<Cliente> obs = FXCollections.observableArrayList(lista);
        Platform.runLater(() -> tabelaUtilizadores.setItems(obs));
    }

    @FXML
    private void filtrarUtilizadores() {
        String pesquisa = campoPesquisa.getText().toLowerCase();

        List<Cliente> filtrados = todosUtilizadores.stream()
                .filter(c -> (c.getNome() != null && c.getNome().toLowerCase().contains(pesquisa)) ||
                        (c.getEmail() != null && c.getEmail().toLowerCase().contains(pesquisa)) ||
                        (c.getCodigo_postal() != null && c.getCodigo_postal().toLowerCase().contains(pesquisa)))
                .collect(Collectors.toList());

        atualizarTabela(filtrados);
    }

    @FXML
    public void verEncomendas() {
        Cliente selecionado = tabelaUtilizadores.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Por favor selecione um cliente da tabela.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/views/ver-detalhes-encomenda.fxml"));
            Parent root = loader.load();

            VerDetalhesEncomendaController controller = loader.getController();
            String apiUrl = "http://localhost:8080/api/encomendas-clientes/" + 2 + "/encomendas";
            controller.carregarDetalhes(apiUrl);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/ipvc/desktop/style/ver-detalhes-encomenda.css").toExternalForm());

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initStyle(StageStyle.UTILITY);
            modal.setTitle("Detalhes das Encomendas");
            modal.setScene(scene);
            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao carregar os detalhes das encomendas.");
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
