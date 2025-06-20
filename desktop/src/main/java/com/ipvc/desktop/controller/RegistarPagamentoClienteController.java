package com.ipvc.desktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ipvc.desktop.models.MetodoPagamento;
import com.ipvc.desktop.models.RecebimentoCliente;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistarPagamentoClienteController {

    @FXML private Label lblReferenciaEncomenda;
    @FXML private Label lblNomeCliente;
    @FXML private Label lblValorTotal;
    @FXML private ComboBox<MetodoPagamento> cbMetodoPagamento;
    @FXML private DatePicker dataPagamento;
    @FXML private Button btnRegistar;
    @FXML private Button btnCancelar;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String apiMetodosPagamentoUrl = "http://localhost:8080/api/metodos-pagamento";
    private final String apiRecebimentosUrl = "http://localhost:8080/api/recebimentos-clientes";

    private final ObjectMapper mapper;
    private RecebimentoCliente recebimento;

    public RegistarPagamentoClienteController() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @FXML
    public void initialize() {
        // Inicializar a data de pagamento com a data atual
        dataPagamento.setValue(LocalDate.now());

        // Carregar métodos de pagamento do backend
        carregarMetodosPagamento();
    }

    public void inicializarDados(RecebimentoCliente recebimento) {
        this.recebimento = recebimento;

        // Preencher os dados da encomenda selecionada
        lblReferenciaEncomenda.setText("Ref. #" + recebimento.getId());
        lblNomeCliente.setText(recebimento.getClienteNome());
        lblValorTotal.setText(recebimento.getValorTotalFormatado());

        // Calcular valor a pagar (valor total - valor já pago)
        double valorTotal = recebimento.getValorTotal();
        double valorPago = recebimento.getValorPago() != null ? recebimento.getValorPago() : 0.0;
        double valorAPagar = valorTotal - valorPago;

    }

    private void carregarMetodosPagamento() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiMetodosPagamentoUrl))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> {
                        try {
                            List<MetodoPagamento> metodosPagamento = mapper.readValue(response, new TypeReference<>() {});
                            Platform.runLater(() -> {
                                cbMetodoPagamento.setItems(FXCollections.observableArrayList(metodosPagamento));
                                // Definir conversor para exibir o nome do método de pagamento no ComboBox
                                cbMetodoPagamento.setConverter(new javafx.util.StringConverter<MetodoPagamento>() {
                                    @Override
                                    public String toString(MetodoPagamento metodo) {
                                        return metodo != null ? metodo.getNome() : "";
                                    }

                                    @Override
                                    public MetodoPagamento fromString(String string) {
                                        return null;
                                    }
                                });
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            mostrarAlerta("Erro ao carregar métodos de pagamento");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao conectar com o servidor");
        }
    }

    @FXML
    public void registarPagamento() {
        if (validarFormulario()) {
            try {
                // Obter o texto do valor e substituir vírgula por ponto
                String valorTexto = lblValorTotal.getText().replace(",", ".");
                // Converter para double primeiro para preservar os decimais
                double valorDouble = Double.parseDouble(valorTexto);
                // Converter para int se necessário (talvez seja melhor manter como double no objeto)
                int valorPagamento = (int) valorDouble;

                // Criar objeto com os dados do pagamento
                Map<String, Object> recebimentoData = new HashMap<>();
                recebimentoData.put("encomendaId", recebimento.getId());
                recebimentoData.put("valorRecebido", valorPagamento);
                recebimentoData.put("dataRecebimento", dataPagamento.getValue().toString());
                recebimentoData.put("metodoPagamentoId", cbMetodoPagamento.getValue().getId());

                String requestBody = mapper.writeValueAsString(recebimentoData);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(apiRecebimentosUrl))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenAccept(response -> {
                            Platform.runLater(() -> {
                                mostrarAlerta("Pagamento registrado com sucesso!");
                                fecharJanela();
                            });
                        })
                        .exceptionally(e -> {
                            Platform.runLater(() -> {
                                mostrarAlerta("Erro ao registrar pagamento: " + e.getMessage());
                            });
                            return null;
                        });

            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Erro ao processar requisição de pagamento");
            }
        }
    }

    @FXML
    public void cancelarPagamento() {
        fecharJanela();
    }

    private boolean validarFormulario() {
        if (cbMetodoPagamento.getValue() == null) {
            mostrarAlerta("Por favor, selecione um método de pagamento");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String mensagem) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informação");
            alert.setHeaderText(null);
            alert.setContentText(mensagem);
            alert.showAndWait();
        });
    }

    private void fecharJanela() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
