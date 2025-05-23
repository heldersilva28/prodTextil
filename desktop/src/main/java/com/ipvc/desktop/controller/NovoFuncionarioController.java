package com.ipvc.desktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ipvc.desktop.models.Utilizador;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class NovoFuncionarioController {

    @FXML private TableView<Utilizador> tabelaUtilizadores;
    @FXML private TableColumn<Utilizador, String> colNome;
    @FXML private TableColumn<Utilizador, String> colEmail;
    @FXML private TableColumn<Utilizador, String> colTipo;

    @FXML private TextField numeroTelefone;
    @FXML private TextField campoPesquisa;
    @FXML private ComboBox<String> comboCargo;

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final String apiUtilizadores = "http://localhost:8080/api/utilizadores/sem-funcionario";
    private final String apiCargos = "http://localhost:8080/api/tipos-utilizadores";

    private List<Utilizador> todosUtilizadores = new ArrayList<>();

    @FXML private BorderPane border;

    @FXML
    public void initialize() {
        FadeTransition ft = new FadeTransition(Duration.millis(800), border);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        configurarTabela();
        carregarCargos();
        carregarUtilizadores();
        numeroTelefone.setTextFormatter(new TextFormatter<>(change -> {
            String novo = change.getControlNewText();
            return (novo.matches("\\d{0,9}")) ? change : null;
        }));
    }

    private PainelAdminController parentController;
    public void setParentController(PainelAdminController parentController) {
        this.parentController = parentController;
    }

    private void configurarTabela() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTipo.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getTipoUtilizadorNome())
        );
    }

    private void carregarUtilizadores() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUtilizadores))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    try {
                        todosUtilizadores = mapper.readValue(response, new TypeReference<>() {});
                        atualizarTabela(todosUtilizadores);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void carregarCargos() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiCargos))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    try {
                        // Supondo que o backend devolve [{ "id": 1, "nome": "Admin" }, ...]
                        List<Map<String, Object>> cargos = mapper.readValue(response, new TypeReference<>() {});
                        List<String> nomesCargos = cargos.stream()
                                .map(c -> c.get("nome").toString())
                                .collect(Collectors.toList());

                        Platform.runLater(() -> {
                            comboCargo.getItems().add("Todos");
                            comboCargo.getItems().addAll(nomesCargos);
                            comboCargo.getSelectionModel().selectFirst();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void atualizarTabela(List<Utilizador> lista) {
        ObservableList<Utilizador> obs = FXCollections.observableArrayList(lista);
        Platform.runLater(() -> tabelaUtilizadores.setItems(obs));
    }

    @FXML
    private void filtrarUtilizadores() {
        String pesquisa = campoPesquisa.getText().toLowerCase();
        String cargoSelecionado = comboCargo.getValue();

        List<Utilizador> filtrados = todosUtilizadores.stream()
                .filter(u -> u.getUsername().toLowerCase().contains(pesquisa) ||
                        u.getEmail().toLowerCase().contains(pesquisa))
                .filter(u -> cargoSelecionado == null || cargoSelecionado.equals("Todos") ||
                        (u.getTipoUtilizadorNome() != null && u.getTipoUtilizadorNome().equals(cargoSelecionado)))
                .collect(Collectors.toList());

        atualizarTabela(filtrados);
    }

    @FXML
    public void criarFuncionario() {
        if(tabelaUtilizadores.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Seleção Inválida");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecione um utilizador.");
            alert.showAndWait();
            return;
        }
        try {
            String telefone = numeroTelefone.getText();
            Utilizador u = tabelaUtilizadores.getSelectionModel().getSelectedItem();
            LocalDate dataAdmissao = LocalDate.now();
            int utilizadorId = u.getId();

            if (telefone.isEmpty()) {
                //mostrarErro("O telefone não pode estar vazio.");
                shakeNode(numeroTelefone);
                shakeNode1(numeroTelefone);
                return;
            }else
            if(telefone.length() != 9) {
                mostrarErro("O telefone deve ter 9 dígitos.");
                return;
            }
            ObjectNode novoFuncionario = mapper.createObjectNode()
                    .put("telefone", telefone)
                    .put("dataAdmissao", dataAdmissao.toString())
                    .put("utilizadorId", utilizadorId);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/funcionarios"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(novoFuncionario)))
                    .build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> {
                        try {
                            if (response.contains("error")) {
                                mostrarErro("Erro ao criar funcionário: " + response);
                            } else {
                                Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Sucesso");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Funcionário criado com sucesso!");
                                    alert.initStyle(StageStyle.UTILITY);
                                    alert.initModality(Modality.APPLICATION_MODAL);
                                    alert.showAndWait();
                                    parentController.carregarConteudo("/com/ipvc/desktop/views/gestao-funcionarios.fxml", "/com/ipvc/desktop/style/gestao-utilizadores.css");
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.initStyle(StageStyle.UTILITY);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
    private void shakeNode(TextField node) {
        // Implementar animação de shake
        node.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        FadeTransition ft = new FadeTransition(Duration.millis(500), node);
        ft.setFromValue(1.0);
        ft.setToValue(0.5);
        ft.setCycleCount(4);
        ft.setAutoReverse(true);
        ft.play();
    }

    private void shakeNode1(TextField node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(70), node);
        tt.setFromX(0);
        tt.setByX(20);
        tt.setCycleCount(4);
        tt.setAutoReverse(true);
        tt.play();
    }
}
