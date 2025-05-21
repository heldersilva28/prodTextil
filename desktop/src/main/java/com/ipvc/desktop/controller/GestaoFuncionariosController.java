package com.ipvc.desktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ipvc.desktop.models.Funcionario;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

    @FXML
    private TextField campoPesquisa;

    private final String apiFuncionarios = "http://localhost:8080/api/funcionarios";
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
        carregarFuncionarios();
    }

    private void configurarTabela() {
        colNome.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNome()));
        colTelefone.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTelefone()));
        colCargo.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCargo()));
        colDataAdmissao.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                cellData.getValue().getDataAdmissao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
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

        List<Funcionario> filtrados = todosFuncionarios.stream()
                .filter(f -> f.getNome() != null && f.getNome().toLowerCase().contains(pesquisa))
                .collect(Collectors.toList());

        atualizarTabela(filtrados);
    }



   @FXML
     public void abrirFormularioNovoFuncionario() {
         parentController.carregarConteudo("/com/ipvc/desktop/views/novo-utilizador.fxml", "/com/ipvc/desktop/style/login.css");
     }
//
//    private void abrirModalEditarCargo(int funcionarioId) {
//         try {
//             FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/views/editar-cargo-utilizador.fxml"));
//             Parent root = loader.load();
//
//             EditarCargoUtilizadorController controller = loader.getController();
//             controller.setUtilizadorId(funcionarioId);
//
//             Scene scene = new Scene(root);
//             scene.getStylesheets().add(getClass().getResource("/com/ipvc/desktop/style/editar-cargo-utilizador.css").toExternalForm());
//
//             Stage modal = new Stage();
//             modal.initModality(Modality.APPLICATION_MODAL);
//             modal.initStyle(StageStyle.UTILITY);
//             modal.setTitle("Editar Cargo");
//             modal.setScene(scene);
//             modal.showAndWait();
//
//             carregarFuncionarios();
//
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
}
