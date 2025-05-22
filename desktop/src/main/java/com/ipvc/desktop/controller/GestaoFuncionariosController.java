package com.ipvc.desktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ipvc.desktop.models.Funcionario;
import com.ipvc.desktop.models.Utilizador;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GestaoFuncionariosController {

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @FXML
    private TableView<Funcionario> tabelaFuncionarios;
    @FXML
    private TableColumn<Funcionario, String> colNome;
    @FXML
    private TableColumn<Funcionario, String> colTelefone;
    @FXML
    private TableColumn<Funcionario, String> colCargo;
    @FXML
    private TableColumn<Funcionario, String> colDataAdmissao;

    @FXML private TextField campoPesquisa;
    @FXML private ComboBox<String> comboCargo;

    private final String apiFuncionarios = "http://localhost:8080/api/funcionarios";
    private final String apiCargos = "http://localhost:8080/api/tipos-utilizadores";
    private List<Funcionario> todosFuncionarios = new ArrayList<>();

    @FXML
    private BorderPane border;
    private PainelAdminController parentController;

    public void setParentController(PainelAdminController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void initialize() {
        FadeTransition ft = new FadeTransition(Duration.millis(800), border);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        configurarTabela();
        carregarCargos();
        carregarFuncionarios();
    }

    private void configurarTabela() {
        colNome.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNome()));
        colTelefone.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTelefone()));
        colCargo.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCargoNome()));
        colDataAdmissao.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                cellData.getValue().getDataAdmissao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
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

    private void carregarFuncionarios() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiFuncionarios))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    try {
                        todosFuncionarios = mapper.readValue(response, new TypeReference<>() {});
                        atualizarTabela(todosFuncionarios);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void atualizarTabela(List<Funcionario> lista) {
        ObservableList<Funcionario> obs = FXCollections.observableArrayList(lista);
        Platform.runLater(() -> tabelaFuncionarios.setItems(obs));
    }

    @FXML
    private void filtrarFuncionarios() {
        String pesquisa = campoPesquisa.getText().toLowerCase();
        String cargoSelecionado = comboCargo.getValue();

        List<Funcionario> filtrados = todosFuncionarios.stream()
                .filter(u -> u.getNome().toLowerCase().contains(pesquisa) ||
                        u.getTelefone().toLowerCase().contains(pesquisa))
                .filter(u -> cargoSelecionado == null || cargoSelecionado.equals("Todos") ||
                        (u.getCargoNome() != null && u.getCargoNome().equals(cargoSelecionado)))
                .filter(u -> {
                    u.getDataAdmissao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    return u.getDataAdmissao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).contains(pesquisa);
                })
                .collect(Collectors.toList());

        atualizarTabela(filtrados);
    }



   @FXML
     public void abrirFormularioNovoFuncionario() {
         parentController.carregarConteudo("/com/ipvc/desktop/views/novo-funcionario.fxml", "/com/ipvc/desktop/style/novo-funcionario.css");
     }
    @FXML
    public void abrirModalEditarFuncionario() {
        if(tabelaFuncionarios.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Seleção Inválida");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecione um funcionário para editar.");
            alert.showAndWait();
            return;
        }
        System.out.println("Funcionário selecionado: " + tabelaFuncionarios.getSelectionModel().getSelectedItem().getId());
        System.out.println("Funcionario user id: " + tabelaFuncionarios.getSelectionModel().getSelectedItem().getUtilizadorId());
         try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/views/editar-funcionario.fxml"));
             Parent root = loader.load();

             EditarFuncionarioController controller = loader.getController();
             controller.setUtilizadorId(tabelaFuncionarios.getSelectionModel().getSelectedItem().getUtilizadorId());
                controller.setFuncionarioId(tabelaFuncionarios.getSelectionModel().getSelectedItem().getId());

             Scene scene = new Scene(root);
             scene.getStylesheets().add(getClass().getResource("/com/ipvc/desktop/style/editar-funcionario.css").toExternalForm());

             Stage modal = new Stage();
             modal.initModality(Modality.APPLICATION_MODAL);
             modal.initStyle(StageStyle.UTILITY);
             modal.setTitle("Editar Cargo");
             modal.setScene(scene);
             modal.showAndWait();

             carregarFuncionarios();

         } catch (IOException e) {
             e.printStackTrace();
         }
     }
}
