package com.ipvc.desktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.models.Material;
import com.ipvc.desktop.models.TipoMaterial;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class EditarMaterialModalController {

    @FXML private TextField campoNome;
    @FXML private ComboBox<TipoMaterial> campoTipo;
    @FXML private TextField campoStockDisponivel;
    @FXML private TextField campoPrecoUnidade;

    private Material material;
    private Material materialTemp; // Variável temporária para armazenar o material
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    public void initialize() {
        carregarTipos();

        // Adicionar filtro para permitir apenas números, "," e "." no campo preço
        campoPrecoUnidade.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9.,]*")) {
                campoPrecoUnidade.setText(oldValue);
            }
        });

        // Adicionar filtro para permitir apenas números, "," e "." no campo stock
        campoStockDisponivel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9.,]*")) {
                campoStockDisponivel.setText(oldValue);
            }
        });
    }

    private void carregarTipos() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/tipos-materiais"))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> {
                        try {
                            List<TipoMaterial> tipos = mapper.readValue(response, new TypeReference<>() {});
                            Platform.runLater(() -> {
                                campoTipo.setItems(FXCollections.observableArrayList(tipos));
                                
                                // Se houver um material carregado, seleciona o tipo correto
                                if (materialTemp != null) {
                                    campoTipo.getItems().stream()
                                        .filter(tipo -> tipo.getId() == materialTemp.getTipoId())
                                        .findFirst()
                                        .ifPresent(tipo -> campoTipo.setValue(tipo));
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void carregarMaterial(Material material) {
        this.material = material;
        this.materialTemp = material; // Guarda o material temporariamente
        campoNome.setText(material.getNome());
        campoStockDisponivel.setText(String.valueOf(material.getStockDisponivel()));
        campoPrecoUnidade.setText(String.valueOf(material.getPrecoUnidade()));

        // Se a ComboBox já tiver itens, seleciona o tipo correto
        if (!campoTipo.getItems().isEmpty()) {
            campoTipo.getItems().stream()
                .filter(tipo -> tipo.getId() == material.getTipoId())
                .findFirst()
                .ifPresent(tipo -> campoTipo.setValue(tipo));
        }
    }

    @FXML
    private void salvarMaterial() {
        try {
            String precoTexto = campoPrecoUnidade.getText().trim();
            String stockTexto = campoStockDisponivel.getText().trim();

            if (campoNome.getText().trim().isEmpty()) {
                showAlert("Por favor, insira o nome do material.");
                return;
            }

            if (campoTipo.getValue() == null) {
                showAlert("Por favor, selecione um tipo.");
                return;
            }

            if (precoTexto.isEmpty()) {
                showAlert("Por favor, insira o preço por unidade.");
                return;
            }

            if (stockTexto.isEmpty()) {
                showAlert("Por favor, insira o stock disponível.");
                return;
            }

            // Verificar se os valores estão no formato correto
            if (!precoTexto.matches("\\d+(,\\d{1,2})?|\\d+(\\.\\d{1,2})?")) {
                showAlert("Preço inválido. Use o formato 123,45 ou 123.45.");
                return;
            }

            if (!stockTexto.matches("\\d+(,\\d{1,2})?|\\d+(\\.\\d{1,2})?")) {
                showAlert("Stock inválido. Use o formato 123,45 ou 123.45.");
                return;
            }

            BigDecimal preco;
            BigDecimal stock;
            try {
                preco = new BigDecimal(precoTexto.replace(",", "."));
                stock = new BigDecimal(stockTexto.replace(",", "."));
            } catch (NumberFormatException e) {
                showAlert("Valores inválidos. Use o formato 123,45 ou 123.45");
                return;
            }

            // Criar o DTO no formato correto
            var materialDTO = mapper.createObjectNode()
                .put("nome", campoNome.getText())
                .put("tipoId", campoTipo.getValue().getId())
                .put("precoUnidade", preco.doubleValue())
                .put("stockDisponivel", stock.doubleValue());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/materiais/" + material.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(materialDTO.toString()))
                    .build();

            // Fazer a chamada de forma síncrona
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                Stage stage = (Stage) campoNome.getScene().getWindow();
                stage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText(null);
                alert.setContentText("Erro ao atualizar material: " + response.body());
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro ao atualizar material: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
