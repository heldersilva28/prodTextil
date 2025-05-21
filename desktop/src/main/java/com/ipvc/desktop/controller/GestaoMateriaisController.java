package com.ipvc.desktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.models.Material;
import com.ipvc.desktop.models.TipoMaterial;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

public class GestaoMateriaisController {

    @FXML
    private TableView<Material> tabelaMateriais;
    @FXML
    private TableColumn<Material, Integer> colId;
    @FXML
    private TableColumn<Material, String> colNome;
    @FXML
    private TableColumn<Material, String> colTipo;
    @FXML
    private TableColumn<Material, BigDecimal> colPrecoUnidade;
    @FXML
    private TableColumn<Material, BigDecimal> colStockDisponivel;
    @FXML
    private TextField filtroNome;
    @FXML
    private ComboBox<String> filtroTipo;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private ObservableList<Material> materiais = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colNome.setCellValueFactory(data -> data.getValue().nomeProperty());
        colTipo.setCellValueFactory(data -> data.getValue().tipoNomeProperty());
        colPrecoUnidade.setCellValueFactory(data -> data.getValue().precoUnidadeProperty());
        colStockDisponivel.setCellValueFactory(data -> data.getValue().stockDisponivelProperty());

        carregarMateriais();
        carregarTipos();

        // Adicionar listeners para filtros
        filtroNome.textProperty().addListener((observable, oldValue, newValue) -> filtrarMateriais());
        filtroTipo.valueProperty().addListener((observable, oldValue, newValue) -> filtrarMateriais());
    }

    private void carregarMateriais() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/materiais"))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> {
                        try {
                            List<Material> lista = mapper.readValue(response, new TypeReference<>() {});
                            lista.forEach(material -> material.setTipoNome(buscarNomeTipo(material.getTipoId())));
                            
                            // Ordenar a lista por ID
                            lista.sort((m1, m2) -> Integer.compare(m1.getId(), m2.getId()));
                            
                            Platform.runLater(() -> {
                                materiais.clear();
                                materiais.addAll(lista);
                                tabelaMateriais.setItems(materiais);
                                tabelaMateriais.refresh();
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                                ObservableList<String> opcoes = FXCollections.observableArrayList("Todos");
                                opcoes.addAll(tipos.stream().map(TipoMaterial::getNome).collect(Collectors.toList()));
                                filtroTipo.setItems(opcoes);
                                filtroTipo.setValue("Todos");
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buscarNomeTipo(int tipoId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/tipos-materiais/" + tipoId)) // Endpoint correto para buscar nome do tipo
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readTree(response.body()).get("nome").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Desconhecido";
        }
    }

    @FXML
    private void filtrarMateriais() {
        String nomeFiltro = filtroNome.getText().toLowerCase();
        String tipoFiltro = filtroTipo.getValue();

        List<Material> filtrados = materiais.stream()
                .filter(material -> material.getNome().toLowerCase().contains(nomeFiltro))
                .filter(material -> "Todos".equals(tipoFiltro) || material.getTipoNome().equals(tipoFiltro))
                .collect(Collectors.toList());

        tabelaMateriais.setItems(FXCollections.observableArrayList(filtrados));
    }

    @FXML
    private void abrirFormularioNovo() {
        Material selecionado = tabelaMateriais.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Aviso");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, selecione um material na tabela antes de editar.");
            alerta.showAndWait();
            return;
        }
        abrirModalEdicao(selecionado);
    }

    private void abrirModalEdicao(Material material) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/views/editar-material-modal.fxml"));
            Parent root = loader.load();

            EditarMaterialModalController controller = loader.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/ipvc/desktop/style/editar-material-modal.css").toExternalForm());

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Editar Material");
            modal.setMinWidth(500);
            modal.setMinHeight(400);
            modal.setScene(scene);

            if (material != null) {
                controller.carregarMaterial(material);
            }

            // Aguardar o fechamento da modal e recarregar os dados
            modal.showAndWait();
            
            // Força a recarga dos materiais após o fechamento da modal
            carregarMateriais();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao abrir modal de edição: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
