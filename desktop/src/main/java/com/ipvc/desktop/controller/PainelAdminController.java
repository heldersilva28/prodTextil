package com.ipvc.desktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.models.Utilizador;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class PainelAdminController {
    @FXML private TableView<Utilizador> tabelaUtilizadores;
    @FXML private TableColumn<Utilizador, String> colNome;
    @FXML private TableColumn<Utilizador, String> colEmail;
    @FXML private TableColumn<Utilizador, String> colTipo;

    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoUtilizadorNome"));

        carregarUtilizadores();
    }

    private void carregarUtilizadores() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/utilizadores"))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            List<Utilizador> lista = mapper.readValue(response.body(), new TypeReference<>() {});
            tabelaUtilizadores.setItems(FXCollections.observableArrayList(lista));

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erro ao carregar utilizadores").showAndWait();
        }
    }

    @FXML

    public void abrirFormularioNovoUtilizador(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/novo-utilizador.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = new Stage();
            stage.setTitle("Criar Novo Utilizador");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
