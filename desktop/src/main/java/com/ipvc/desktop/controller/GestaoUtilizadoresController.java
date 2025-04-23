package com.ipvc.desktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.models.Utilizador;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

public class GestaoUtilizadoresController {

    @FXML private TableView<Utilizador> tabelaUtilizadores;
    @FXML private TableColumn<Utilizador, String> colNome;
    @FXML private TableColumn<Utilizador, String> colEmail;
    @FXML private TableColumn<Utilizador, String> colTipo;

    @FXML private TextField campoPesquisa;
    @FXML private ComboBox<String> comboCargo;

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final String apiUtilizadores = "http://localhost:8080/api/utilizadores";
    private final String apiCargos = "http://localhost:8080/api/tipos-utilizadores"; // Corrigido plural

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

        // Adiciona um ContextMenu para clicar com o botão direito
        tabelaUtilizadores.setRowFactory(tv -> {
            TableRow<Utilizador> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            // Cria a opção de "Editar"
            MenuItem editItem = new MenuItem("Editar");
            editItem.setOnAction((ActionEvent e) -> {
                Utilizador selectedUtilizador = row.getItem();
                if (selectedUtilizador != null) {
                    // Chama o método para editar o utilizador (ou abrir um formulário, etc.)
                    //abrirFormularioEditar(selectedUtilizador);
                    parentController.carregarConteudo("/com/ipvc/desktop/views/novo-utilizador.fxml","/com/ipvc/desktop/style/login.css");
                }
            });

            contextMenu.getItems().add(editItem);

            contextMenu.setStyle("-fx-pref-width: 200px;");  // Tamanho do menu
            editItem.setStyle("-fx-pref-width: 180px;");     // Tamanho do item de menu
            editItem.setStyle("-fx-text-fill: black;");

            // Aplica o ContextMenu na linha
            row.setContextMenu(contextMenu);

            // Retorna a linha configurada
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && !row.isEmpty()) {
                    // Marca a linha como selecionada
                    for (int i = 0; i < tabelaUtilizadores.getItems().size(); i++) {
                        row.getStyleClass().remove("selected");
                    }
                    row.getStyleClass().add("selected");
                }
            });

            return row;
        });
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
    public void abrirFormularioNovo() {
        parentController.carregarConteudo("/com/ipvc/desktop/views/novo-utilizador.fxml","/com/ipvc/desktop/style/login.css");
    }
}
