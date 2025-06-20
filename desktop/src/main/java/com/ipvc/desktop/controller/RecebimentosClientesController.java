package com.ipvc.desktop.controller;

import com.ipvc.desktop.models.RecebimentoCliente;
import com.ipvc.desktop.services.EncomendaClienteService;
import com.ipvc.desktop.services.RecebimentoClienteService;
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

public class RecebimentosClientesController {

    @FXML private TableView<RecebimentoCliente> tabelaEncomendas;
    @FXML private TableColumn<RecebimentoCliente, Integer> colId;
    @FXML private TableColumn<RecebimentoCliente, String> colCliente;
    @FXML private TableColumn<RecebimentoCliente, String> colData;
    @FXML private TableColumn<RecebimentoCliente, String> colValorTotal;
    @FXML private Button btnRegistarPagamento;

    // Serviços para acesso à API
    private final RecebimentoClienteService recebimentoService;
    private final EncomendaClienteService encomendaService;

    public RecebimentosClientesController() {
        // Inicializa os serviços
        this.recebimentoService = new RecebimentoClienteService();
        this.encomendaService = new EncomendaClienteService();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarEncomendasSemPagamento();
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("clienteNome"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataEncomendaFormatada"));
        colValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotalFormatado"));

        // Configuração simples sem estilização baseada no estado de pagamento
        tabelaEncomendas.setRowFactory(tv -> new javafx.scene.control.TableRow<>());
    }

    private void carregarRecebimentos() {
        // Usando o serviço para carregar os recebimentos
        new Thread(() -> {
            try {
                List<RecebimentoCliente> recebimentos = recebimentoService.getTodosRecebimentos();
                Platform.runLater(() -> {
                    tabelaEncomendas.setItems(FXCollections.observableArrayList(recebimentos));
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                mostrarAlerta("Erro ao carregar os recebimentos de clientes: " + e.getMessage());
            }
        }).start();
    }

    @FXML
    public void abrirModalPagamento() {
        try {
            RecebimentoCliente recebimentoSelecionado = tabelaEncomendas.getSelectionModel().getSelectedItem();

            if (recebimentoSelecionado == null) {
                mostrarAlerta("Por favor, selecione uma encomenda para registrar o pagamento");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/views/registar-pagamento-cliente-modal.fxml"));
            Parent root = loader.load();

            RegistarPagamentoClienteController controller = loader.getController();
            controller.inicializarDados(recebimentoSelecionado);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/ipvc/desktop/style/recebimentos-clientes.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Registar Pagamento de Cliente");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Atualiza a tabela após fechar o modal
            carregarRecebimentos();
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
                List<RecebimentoCliente> encomendasSemPagamento = encomendaService.getEncomendasSemPagamento()
                        .stream()
                        .map(this::converterParaRecebimento)
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

    // Método auxiliar para converter EncomendaCliente em RecebimentoCliente
    private RecebimentoCliente converterParaRecebimento(com.ipvc.desktop.models.EncomendaCliente encomenda) {
        RecebimentoCliente recebimento = new RecebimentoCliente();
        recebimento.setId(encomenda.getId());
        recebimento.setClienteId(encomenda.getClienteId());
        recebimento.setClienteNome(encomenda.getClienteNome());
        recebimento.setDataEncomenda(encomenda.getDataEncomenda());
        recebimento.setValorTotal(encomenda.getValorTotal().doubleValue());
        recebimento.setValorPago(0.0);
        recebimento.setMetodoPagamentoId(null);
        recebimento.setMetodoPagamentoNome("Não definido");
        return recebimento;
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
