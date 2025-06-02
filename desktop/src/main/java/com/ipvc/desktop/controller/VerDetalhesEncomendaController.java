package com.ipvc.desktop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ipvc.desktop.models.EncomendaDetalhes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ArrayList;

public class VerDetalhesEncomendaController {

    @FXML private ListView<EncomendaDetalhes> listaEncomendas;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()); // Registra o módulo

    @FXML
    public void initialize() {
        // Configurar evento de clique na lista
        listaEncomendas.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) { // Clique duplo com botão esquerdo
                EncomendaDetalhes selecionada = listaEncomendas.getSelectionModel().getSelectedItem();
                if (selecionada != null) {
                    abrirModalDetalhes(selecionada);
                }
            }
        });

        // Desativar seleção visual para manter o texto preto
        listaEncomendas.setStyle("-fx-selection-bar: transparent; -fx-selection-bar-text: black;");
    }

    public void carregarDetalhes(String apiUrl) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    try {
                        // Verificar se a resposta é um erro
                        JsonNode rootNode = mapper.readTree(response);

                        // Verificar se a resposta contém um objeto de erro
                        if (rootNode.has("status") && (rootNode.get("status").asInt() == 404 || rootNode.get("status").asInt() == 500)) {
                            String errorMessage = "Nenhuma encomenda encontrada para este cliente.";
                            Platform.runLater(() -> mostrarAlerta(errorMessage));
                            return;
                        }

                        // Se chegou aqui, é porque a resposta deve ser uma lista de encomendas
                        List<EncomendaDetalhes> encomendas;

                        // Verificar se é um array ou objeto único
                        if (rootNode.isArray()) {
                            encomendas = mapper.readValue(response, new TypeReference<List<EncomendaDetalhes>>() {});
                        } else {
                            // Se for um objeto único, criar uma lista com esse único objeto
                            EncomendaDetalhes encomenda = mapper.readValue(response, EncomendaDetalhes.class);
                            encomendas = new ArrayList<>();
                            encomendas.add(encomenda);
                        }

                        if (encomendas.isEmpty()) {
                            Platform.runLater(() -> mostrarAlerta("Nenhuma encomenda encontrada para este cliente."));
                            return;
                        }

                        Platform.runLater(() -> listaEncomendas.getItems().setAll(encomendas));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> mostrarAlerta("Erro ao processar os detalhes das encomendas: " + e.getMessage()));
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    Platform.runLater(() -> mostrarAlerta("Erro na comunicação com o servidor: " + ex.getMessage()));
                    return null;
                });

        configureListCellFactory();
    }

    private void configureListCellFactory() {
        listaEncomendas.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(EncomendaDetalhes encomenda, boolean empty) {
                super.updateItem(encomenda, empty);
                if (empty || encomenda == null) {
                    setText(null);
                } else {
                    StringBuilder detalhes = new StringBuilder();
                    detalhes.append("Cliente: ").append(encomenda.getClienteNome()).append("\n")
                            .append("Data: ").append(encomenda.getDataEncomenda()).append("\n")
                            .append("Estado: ").append(encomenda.getEstadoNome()).append("\n")
                            .append("Valor Total: ").append(encomenda.getValorTotal()).append(" €\n")
                            .append("Tarefas:\n");

                    if (encomenda.getTarefas() != null) {
                        encomenda.getTarefas().forEach(tarefa -> detalhes.append("  - ")
                                .append(tarefa.getDescricaoNome())
                                .append(" (").append(tarefa.getEstado()).append(")")
                                .append(" - Funcionário: ").append(tarefa.getFuncionarioNome()).append("\n"));
                    } else {
                        detalhes.append("  Sem tarefas\n");
                    }

                    detalhes.append("Etapas:\n");
                    if (encomenda.getEtapas() != null) {
                        encomenda.getEtapas().forEach(etapa -> {
                            String nomeEtapa = buscarNomeEtapa(etapa.getTipoEtapaId());
                            detalhes.append("  - ").append(nomeEtapa).append("\n");
                        });
                    } else {
                        detalhes.append("  Sem etapas\n");
                    }

                    detalhes.append("Itens Encomenda:");
                    if (encomenda.getItensEncomenda() != null) {
                        encomenda.getItensEncomenda().forEach(itensEncomenda -> {
                            detalhes.append("\n  - ")
                                    .append(itensEncomenda.getProduto())
                                    .append(" (Quantidade: ").append(itensEncomenda.getQuantidade()).append(")")
                                    .append(" - Preço Unitário: ").append(itensEncomenda.getPrecoUnitario()).append(" €");

                            if (itensEncomenda.getTotal() != null && itensEncomenda.getTotal() > 0) {
                                detalhes.append(" - Total Do Item: ").append(itensEncomenda.getTotal()).append(" €");
                            } else {
                                // Calcular o total manualmente
                                double total = itensEncomenda.getQuantidade() * itensEncomenda.getPrecoUnitario();
                                detalhes.append(" - Total Do Item: ").append(total).append(" €");
                            }
                        });
                    } else {
                        detalhes.append("\n  Sem itens");
                    }

                    setText(detalhes.toString());
                }
            }
        });
    }

    private String buscarNomeEtapa(int tipoEtapaId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/tipos-etapas/" + tipoEtapaId))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            var jsonNode = mapper.readTree(response.body());

            // Verifica se o nó "nome" existe e não é nulo
            if (jsonNode != null && jsonNode.has("descricao")) {
                return jsonNode.get("descricao").asText();
            } else {
                return "Nome da etapa não encontrado";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao buscar nome da etapa";
        }
    }

    private void abrirModalDetalhes(EncomendaDetalhes encomenda) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/views/detalhes-encomenda-modal.fxml"));
            Parent root = loader.load();

            DetalhesEncomendaModalController controller = loader.getController();
            controller.carregarDetalhes(encomenda);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initStyle(StageStyle.UTILITY);
            modal.setTitle("Detalhes da Encomenda");
            modal.setScene(new Scene(root));
            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao abrir os detalhes da encomenda.");
        }
    }

    // Adicione este método para exibir alertas
    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void fechar() {
        Stage stage = (Stage) listaEncomendas.getScene().getWindow();
        stage.close();
    }
}
