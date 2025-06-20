package com.ipvc.desktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.models.Utilizador;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;

public class PainelAdminController {
    @FXML private StackPane contentPane; // Área de conteúdo principal onde as telas serão carregadas

    @FXML
    public void initialize() {
        Platform.runLater(this::abrirDashboard);
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
        carregarConteudo("/com/ipvc/desktop/views/gestao-utilizadores.fxml","/com/ipvc/desktop/style/gestao-utilizadores.css"); // Carrega a página de gestão de utilizadores
    }

    @FXML
    public void abrirGestaoEncomendasClientes() {
        carregarConteudo("/com/ipvc/desktop/views/gestao-encomendas-clientes.fxml","/com/ipvc/desktop/style/gestao-encomendas-clientes.css"); // Carrega a página de encomendas de clientes
    }

    @FXML
    public void abrirGestaoClientes() {
        carregarConteudo("/com/ipvc/desktop/views/gestao-clientes.fxml","/com/ipvc/desktop/style/gestao-utilizadores.css"); // Carrega a página de encomendas de clientes
    }
//
    @FXML
    public void abrirGestaoEncomendasFornecedores() {
        carregarConteudo("/com/ipvc/desktop/views/gestao-encomendas-fornecedores.fxml",
                "/com/ipvc/desktop/style/gestao-encomendas-clientes.css");
    }

    @FXML
    public void abrirGestaoMateriais() {
        carregarConteudo("/com/ipvc/desktop/views/gestao-materiais.fxml",
                "/com/ipvc/desktop/style/gestao-materiais.css"); // Carrega a página de gestão de materiais
    }

    @FXML
    public void abrirGestaoFuncionarios() {
        carregarConteudo(
                "/com/ipvc/desktop/views/gestao-funcionarios.fxml",
                "/com/ipvc/desktop/style/gestao-utilizadores.css"
        );
    }

    @FXML
    public void abrirTarefas() {
        carregarConteudo(
                "/com/ipvc/desktop/views/tarefas-producao.fxml",
                "/com/ipvc/desktop/styles/tarefas-producao.css"
        );
    }

    @FXML
    public void recebimentos() {
        carregarConteudo(
                "/com/ipvc/desktop/views/recebimentos-clientes.fxml",
                "/com/ipvc/desktop/style/recebimentos-clientes.css"
        );
    }
    @FXML
    public void pagamentos() {
        carregarConteudo(
                "/com/ipvc/desktop/views/pagamentos-fornecedores.fxml",
                "/com/ipvc/desktop/style/recebimentos-clientes.css"
        );
    }
//
//    @FXML
//    public void abrirHistoricoEventos() {
//        carregarConteudo("/com/ipvc/desktop/historico-eventos.fxml"); // Carrega a página de histórico de eventos
//    }

    public void carregarConteudo(String fxmlPath, String cssPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Tenta injetar o controlador no novo, se ele tiver o método 'setParentController'
            Object controller = loader.getController();
            if (controller != null) {
                injetarController(controller, this.getClass());
            }

            // Aplica CSS se fornecido
            if (cssPath != null && !cssPath.isEmpty()) {
                root.getStylesheets().clear();
                root.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            }

            // Adiciona o conteúdo carregado ao contentPane
            contentPane.getChildren().setAll(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void injetarController(Object controller, Class<?> parentControllerType) {
        if (controller != null) {
            try {
                // Tenta encontrar o método 'setParentController' no controlador
                Method method = controller.getClass().getMethod("setParentController", parentControllerType);

                // Invoca o método e passa 'this' (o controlador atual) como parâmetro
                method.invoke(controller, this);
            } catch (NoSuchMethodException e) {
                // Caso o método não exista, você pode tratar isso como necessário
                System.out.println("O método 'setParentController' não existe no controlador " + controller.getClass().getName());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
    public void setContent(Node node) {
        contentPane.getChildren().setAll(node);
    }

}
