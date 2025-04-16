package com.ipvc.desktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.models.Utilizador;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;

public class PainelAdminController {

    @FXML private TableView<Utilizador> tabelaUtilizadores;
    @FXML private TableColumn<Utilizador, String> colNome;
    @FXML private TableColumn<Utilizador, String> colEmail;
    @FXML private TableColumn<Utilizador, String> colTipo;

    @FXML private Button btnLogout; // Botão de Logout
    @FXML private Button btnNovoUtilizador; // Botão para abrir formulário de novo utilizador
    @FXML private StackPane contentPane; // Área de conteúdo principal onde as telas serão carregadas

    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    public void initialize() {
//        // Definir como as colunas irão apresentar os dados da classe Utilizador
//        colNome.setCellValueFactory(new PropertyValueFactory<>("username"));
//        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
//        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoUtilizadorNome"));
//
//        // Carregar a lista de utilizadores da API
//        carregarUtilizadores();
        // só é seguro chamar aqui, porque o FXML já está na Scene
        Platform.runLater(this::abrirDashboard);
    }

    private void carregarUtilizadores() {
        try {
            // Criando a requisição HTTP para buscar os utilizadores
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/utilizadores"))
                    .build();

            // Enviar a requisição e pegar a resposta
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            // Converter a resposta JSON para uma lista de Utilizadores
            List<Utilizador> lista = mapper.readValue(response.body(), new TypeReference<>() {});
            tabelaUtilizadores.setItems(FXCollections.observableArrayList(lista)); // Atualizar a tabela com os dados

        } catch (Exception e) {
            // Exibir mensagem de erro se ocorrer algum problema na requisição ou parsing
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erro ao carregar utilizadores: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void abrirFormularioNovoUtilizador(ActionEvent event) {
        try {
            // Carregar a interface do formulário para adicionar um novo utilizador
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/views/novo-utilizador.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Criar uma nova janela para o formulário
            Stage stage = new Stage();
            stage.setTitle("Criar Novo Utilizador");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); // Modal, bloqueia o acesso à janela principal até ser fechado
            stage.showAndWait(); // Aguarda a interação do utilizador

        } catch (IOException e) {
            // Exibir mensagem de erro se ocorrer algum problema ao carregar o FXML
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erro ao abrir o formulário de novo utilizador").showAndWait();
        }
    }

    @FXML
    public void onLogout(ActionEvent actionEvent) {
        // Lógica de Logout, pode ser feito redirecionando para a tela de login ou fechando a sessão do utilizador
        try {
            // Aqui você pode redirecionar para a tela de login ou encerrar a sessão
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("Você tem certeza que deseja sair?");
            alert.setContentText("Ao sair, sua sessão será encerrada.");

            if (alert.showAndWait().get() == ButtonType.OK) {
                // Encerrar a sessão ou redirecionar para o login
                System.out.println("Sessão encerrada!");

                // Por exemplo, carregar a tela de login novamente:
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/views/login.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
                scene.getStylesheets().add(Objects.requireNonNull(
                        getClass().getResource("/com/ipvc/desktop/style/login.css")).toExternalForm());
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Login");
                stage.setResizable(false);
                stage.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erro ao realizar o logout").showAndWait();
        }
    }

    // Métodos para carregar diferentes seções no painel

    @FXML
    public void abrirDashboard() {
        carregarConteudo("/com/ipvc/desktop/views/dashboard.fxml","/com/ipvc/desktop/style/dashboard.css"); // Carrega o conteúdo do Dashboard
    }

    @FXML
    public void abrirGestaoUtilizadores() {
        carregarConteudo("/com/ipvc/desktop/views/novo-utilizador.fxml","/com/ipvc/desktop/style/login.css"); // Carrega a página de gestão de utilizadores

    }

//    @FXML
//    public void abrirGestaoEncomendasClientes() {
//        carregarConteudo("/com/ipvc/desktop/gestao-encomendas-clientes.fxml"); // Carrega a página de encomendas de clientes
//    }
//
//    @FXML
//    public void abrirGestaoEncomendasFornecedores() {
//        carregarConteudo("/com/ipvc/desktop/gestao-encomendas-fornecedores.fxml"); // Carrega a página de encomendas de fornecedores
//    }
//
//    @FXML
//    public void abrirGestaoMateriais() {
//        carregarConteudo("/com/ipvc/desktop/gestao-materiais.fxml"); // Carrega a página de gestão de materiais
//    }
//
//    @FXML
//    public void abrirGestaoFuncionarios() {
//        carregarConteudo("/com/ipvc/desktop/gestao-funcionarios.fxml"); // Carrega a página de gestão de funcionários
//    }
//
//    @FXML
//    public void abrirHistoricoEventos() {
//        carregarConteudo("/com/ipvc/desktop/historico-eventos.fxml"); // Carrega a página de histórico de eventos
//    }

    // Método para carregar o conteúdo dinâmico na StackPane
    private void carregarConteudo(String fxmlPath, String css) {
        try {
            // Carregar o FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent newContent = loader.load();

            // Limpar o conteúdo atual
            contentPane.getChildren().clear();
            contentPane.getChildren().add(newContent);  // Adicionar o novo conteúdo

            // Carregar e aplicar o CSS à cena
            Scene scene = newContent.getScene(); // Obtém a cena do novo conteúdo
            if (scene != null) {
                // Adicionar o arquivo CSS à cena, se ela já existir
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(css)).toExternalForm());
            } else {
                // Caso o conteúdo não tenha uma cena associada, crie uma nova cena
                Stage stage = (Stage) contentPane.getScene().getWindow();
                Scene newScene = new Scene(newContent);
                newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(css)).toExternalForm());
                stage.setScene(newScene);
            }

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erro ao carregar conteúdo").showAndWait();
        }
    }

}
