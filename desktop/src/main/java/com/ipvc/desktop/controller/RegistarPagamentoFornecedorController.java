package com.ipvc.desktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ipvc.desktop.models.MetodoPagamento;
import com.ipvc.desktop.models.PagamentoFornecedor;
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

public class RegistarPagamentoFornecedorController {

    @FXML private Label lblReferenciaEncomenda;
    @FXML private Label lblNomeFornecedor;
    @FXML private Label lblValorTotal;
    @FXML private ComboBox<MetodoPagamento> cbMetodoPagamento;
    @FXML private DatePicker dataPagamento;
    @FXML private Button btnRegistar;
    @FXML private Button btnCancelar;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String apiMetodosPagamentoUrl = "http://localhost:8080/api/metodos-pagamento";
    private final String apiPagamentosUrl = "http://localhost:8080/api/pagamentos-fornecedores";

    private final ObjectMapper mapper;
    private PagamentoFornecedor pagamento;

    public RegistarPagamentoFornecedorController() {
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

    public void inicializarDados(PagamentoFornecedor pagamento) {
        this.pagamento = pagamento;

        // Preencher os dados da encomenda selecionada
        lblReferenciaEncomenda.setText("Ref. #" + pagamento.getId());
        lblNomeFornecedor.setText(pagamento.getFornecedorNome());
        lblValorTotal.setText(pagamento.getValorTotalFormatado());

        // Calcular valor a pagar (valor total - valor já pago)
        double valorTotal = pagamento.getValorTotal();
        double valorPago = pagamento.getValorPago() != null ? pagamento.getValorPago() : 0.0;
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
                // Converter para double (mantendo os decimais)
                double valorPagamento = Double.parseDouble(valorTexto);

                // Criar objeto com os dados do pagamento usando os nomes corretos dos campos
                Map<String, Object> pagamentoData = new HashMap<>();
                pagamentoData.put("encomendaId", pagamento.getId());
                pagamentoData.put("metodoPagamentoId", cbMetodoPagamento.getValue().getId());
                pagamentoData.put("valorPago", valorPagamento); // Alterado de valorPagamento para valorPago
                pagamentoData.put("dataPagamento", dataPagamento.getValue().toString());

                String requestBody = mapper.writeValueAsString(pagamentoData);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(apiPagamentosUrl))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(response -> {
                            // Verificar se a resposta foi bem-sucedida antes de mostrar mensagem de sucesso
                            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                                Platform.runLater(() -> {
                                    mostrarAlerta("Pagamento registrado com sucesso!");
                                    fecharJanela();
                                });
                            } else {
                                Platform.runLater(() -> {
                                    mostrarAlerta("Erro ao registrar pagamento. Código: " + response.statusCode());
                                    System.err.println("Resposta: " + response.body());
                                });
                            }
                            return response;
                        })
                        .exceptionally(e -> {
                            Platform.runLater(() -> {
                                mostrarAlerta("Erro ao registrar pagamento: " + e.getMessage());
                                e.printStackTrace();
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
