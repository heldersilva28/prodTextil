package com.ipvc.desktop.controller;

import com.ipvc.desktop.models.PagamentoFornecedor;
import com.ipvc.desktop.services.EncomendaFornecedorService;
import com.ipvc.desktop.services.PagamentoFornecedorService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PagamentosFornecedoresController {

    @FXML private TableView<PagamentoFornecedor> tabelaEncomendas;
    @FXML private TableColumn<PagamentoFornecedor, Integer> colId;
    @FXML private TableColumn<PagamentoFornecedor, String> colCliente; // Será renomeada para Fornecedor na interface
    @FXML private TableColumn<PagamentoFornecedor, String> colData;
    @FXML private TableColumn<PagamentoFornecedor, String> colValorTotal;
    @FXML private Button btnRegistarPagamento;

    // Serviços para acesso à API
    private final PagamentoFornecedorService pagamentoService;
    private final EncomendaFornecedorService encomendaService;

    public PagamentosFornecedoresController() {
        // Inicializa os serviços
        this.pagamentoService = new PagamentoFornecedorService();
        this.encomendaService = new EncomendaFornecedorService();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarEncomendasSemPagamento();
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("fornecedorNome"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataEncomendaFormatada"));
        colValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotalFormatado"));

        // Configuração simples sem estilização baseada no estado de pagamento
        tabelaEncomendas.setRowFactory(tv -> new javafx.scene.control.TableRow<>());
    }

    private void carregarPagamentos() {
        // Usando o serviço para carregar os pagamentos
        new Thread(() -> {
            try {
                List<PagamentoFornecedor> pagamentos = pagamentoService.getTodosPagamentos();
                Platform.runLater(() -> {
                    tabelaEncomendas.setItems(FXCollections.observableArrayList(pagamentos));
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                mostrarAlerta("Erro ao carregar os pagamentos de fornecedores: " + e.getMessage());
            }
        }).start();
    }

    @FXML
    public void abrirModalPagamento() {
        try {
            PagamentoFornecedor pagamentoSelecionado = tabelaEncomendas.getSelectionModel().getSelectedItem();

            if (pagamentoSelecionado == null) {
                mostrarAlerta("Por favor, selecione uma encomenda para registrar o pagamento");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/views/registar-pagamento-fornecedor-modal.fxml"));
            Parent root = loader.load();

            RegistarPagamentoFornecedorController controller = loader.getController();
            controller.inicializarDados(pagamentoSelecionado);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/ipvc/desktop/style/pagamentos-fornecedores.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Registar Pagamento a Fornecedor");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Atualiza a tabela após fechar o modal
            carregarPagamentos();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao abrir o formulário de pagamento");
        }
    }

    @FXML
    public void carregarEncomendasSemPagamento() {
        // Usando o serviço para carregar encomendas sem pagamento
        new Thread(() -> {
            try {
                List<PagamentoFornecedor> encomendasSemPagamento = encomendaService.getEncomendasSemPagamento()
                        .stream()
                        .map(this::converterParaPagamento)
                        .toList();

                Platform.runLater(() -> {
                    if (encomendasSemPagamento.isEmpty()) {
                        mostrarAlerta("Não existem encomendas sem pagamento no momento.");
                    } else {
                        tabelaEncomendas.setItems(FXCollections.observableArrayList(encomendasSemPagamento));
                    }
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                mostrarAlerta("Erro ao carregar encomendas sem pagamento: " + e.getMessage());
            }
        }).start();
    }

    // Método auxiliar para converter EncomendaFornecedor em PagamentoFornecedor
    private PagamentoFornecedor converterParaPagamento(com.ipvc.desktop.models.EncomendaFornecedor encomenda) {
        PagamentoFornecedor pagamento = new PagamentoFornecedor();
        pagamento.setId(encomenda.getId());
        pagamento.setFornecedorId(encomenda.getFornecedorId());
        pagamento.setFornecedorNome(encomenda.getFornecedorNome());
        pagamento.setDataEncomenda(encomenda.getDataEncomenda());
        pagamento.setValorTotal(encomenda.getValorTotal().doubleValue());
        pagamento.setValorPago(0.0);
        pagamento.setEstadoPagamento("Pendente");
        pagamento.setMetodoPagamentoId(null);
        pagamento.setMetodoPagamentoNome("Não definido");
        return pagamento;
    }

    private void mostrarAlerta(String mensagem) {
        Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Informação");
            alert.setHeaderText(null);
            alert.setContentText(mensagem);
            alert.showAndWait();
        });
    }
}
