package com.ipvc.desktop.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.function.UnaryOperator;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

public class EditarFuncionarioController {
    @FXML private TextField inputTelefone;
    @FXML private ComboBox<String> cargoComboBox;
    @FXML private DatePicker inputDataAdmissao;
    @FXML private Label errorLabel;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private Integer funcionarioId;
    private Integer utilizadorId;

    @FXML
    public void initialize() {
        // Oculta a label de erro inicialmente
        errorLabel.setVisible(false);

        // 1) filtro do telefone: só dígitos, até 9 chars
        UnaryOperator<Change> filter = change -> {
            String novo = change.getControlNewText();
            return (novo.matches("\\d{0,9}")) ? change : null;
        };
        inputTelefone.setTextFormatter(new TextFormatter<>(filter));

        // 2) carrega the lista de cargos first
        carregarCargos();
    }

    /**
     * Deve ser chamado pelo controlador que abre este diálogo,
     * antes de mostrar a janela, para definir qual é o funcionário.
     */
    public void setFuncionarioId(Integer id) {
        this.funcionarioId = id;
        // Se o combo já estiver populado, dispara o load dos dados:
        if (!cargoComboBox.getItems().isEmpty()) {
            carregarDadosFuncionario();
        }
    }
    public void setUtilizadorId(Integer id) {
        this.utilizadorId = id;
    }

    private void carregarCargos() {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/tipos-utilizadores"))
                .build();

        httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        JsonNode lista = mapper.readTree(json);
                        Platform.runLater(() -> {
                            cargoComboBox.getItems().clear();
                            lista.forEach(node ->
                                    cargoComboBox.getItems().add(node.path("nome").asText(""))
                            );
                            // Assim que tivermos cargos e um funcionarioId, carregamos os dados
                            if (funcionarioId != null) {
                                carregarDadosFuncionario();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        mostrarErro("Erro ao carregar lista de cargos");
                    }
                });
    }

    private void carregarDadosFuncionario() {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/funcionarios/" + funcionarioId))
                .build();

        httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        JsonNode f = mapper.readTree(json);
                        Platform.runLater(() -> {
                            // Telefone
                            inputTelefone.setText(f.path("telefone").asText(""));

                            // Data de admissão
                            String dataAdm = f.path("dataAdmissao").asText(null);
                            if (dataAdm != null && !dataAdm.isEmpty()) {
                                inputDataAdmissao.setValue(LocalDate.parse(dataAdm));
                            }

                            // Cargo (nome)
                            String nomeCargo = f.path("cargoNome").asText("");
                            if (!nomeCargo.isEmpty()) {
                                cargoComboBox.getSelectionModel().select(nomeCargo);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        mostrarErro("Erro ao carregar dados do funcionário");
                    }
                });
    }

    @FXML
    private void atualizarFuncionario() {
        try {
            // 1) validações
            String tel = inputTelefone.getText().trim();
            if (tel.isEmpty()) {
                mostrarErro("Por favor, preencha o telefone");
                return;
            }
            if (tel.length() != 9) {
                mostrarErro("Telefone deve ter exatamente 9 dígitos");
                return;
            }
            if (cargoComboBox.getValue() == null) {
                mostrarErro("Por favor, selecione um cargo");
                return;
            }
            if (inputDataAdmissao.getValue() == null) {
                mostrarErro("Por favor, selecione a data de admissão");
                return;
            }

            // 2) monta o DTO para envio
            ObjectNode updateDTO = mapper.createObjectNode()
                    .put("telefone", tel)
                    .put("cargo", getCargoId(cargoComboBox.getValue()))
                    .put("dataAdmissao", inputDataAdmissao.getValue().toString());

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/funcionarios/" + utilizadorId))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(updateDTO)))
                    .build();

            httpClient.send(req, HttpResponse.BodyHandlers.ofString());

            // Fecha a janela
            Stage stage = (Stage) inputTelefone.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao atualizar funcionário");
        }
    }

    /**
     * Busca o ID do cargo selecionado, consultando a API de tipos de utilizadores.
     */
    private Integer getCargoId(String cargoNome) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/tipos-utilizadores"))
                    .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            JsonNode lista = mapper.readTree(resp.body());
            for (JsonNode node : lista) {
                if (node.path("nome").asText("").equals(cargoNome)) {
                    return node.path("id").asInt();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void mostrarErro(String mensagem) {
        errorLabel.setText(mensagem);
        errorLabel.setVisible(true);
    }
}
