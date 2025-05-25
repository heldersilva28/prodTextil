package com.ipvc.desktop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipvc.desktop.models.EncomendaDetalhes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DetalhesEncomendaModalController {

    @FXML private Label lblCliente;
    @FXML private Label lblData;
    @FXML private Label lblEstado;
    @FXML private Label lblValorTotal;
    @FXML private TextArea lblTarefas; // Alterado para TextArea
    @FXML private TextArea lblEtapas; // Alterado para TextArea
    @FXML private TextArea lblProdutos;

    public void carregarDetalhes(EncomendaDetalhes encomenda) {
        lblCliente.setText(encomenda.getClienteNome());
        lblData.setText(encomenda.getDataEncomenda().toString());
        lblEstado.setText(encomenda.getEstadoNome());
        lblValorTotal.setText(encomenda.getValorTotal() + " €");

        StringBuilder tarefas = new StringBuilder();
        encomenda.getTarefas().forEach(tarefa -> tarefas.append("- ")
                .append(tarefa.getDescricaoNome())
                .append(" (").append(tarefa.getEstado()).append(")")
                .append(" - Funcionário: ").append(tarefa.getFuncionarioNome()).append("\n"));
        lblTarefas.setText(tarefas.toString());

        StringBuilder etapas = new StringBuilder();
        encomenda.getEtapas().forEach(etapa -> etapas.append("- Nome: ")
                .append(buscarNomeEtapa(etapa.getTipoEtapaId()))
                .append(", Início: ").append(etapa.getDataInicio())
                .append(", Fim: ").append(etapa.getDataFim()).append("\n"));
        lblEtapas.setText(etapas.toString());

        StringBuilder produtos = new StringBuilder();
        encomenda.getItensEncomenda().forEach(item -> produtos.append("- ")
                .append(item.getProduto())
                .append(" (Quantidade: ").append(item.getQuantidade()).append(")")
                .append(" - Preço Unitário: ").append(item.getPrecoUnitario()).append(" €")
                .append(" - Total Do Item: ").append(item.getTotal()).append(" €\n"));
        lblProdutos.setText(produtos.toString());

        // Adicionar o CSS após a exibição da janela
        Platform.runLater(() -> {
            if (lblCliente.getScene() != null) {
                lblCliente.getScene().getStylesheets().add(getClass().getResource("/com/ipvc/desktop/style/detalhes-encomenda-modal.css").toExternalForm());
            }
        });
    }

    private String buscarNomeEtapa(int tipoEtapaId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/tipos-etapas/" + tipoEtapaId))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            var jsonNode = new ObjectMapper().readTree(response.body());

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

    @FXML
    private void fechar() {
        Stage stage = (Stage) lblCliente.getScene().getWindow();
        stage.close();
    }
}
