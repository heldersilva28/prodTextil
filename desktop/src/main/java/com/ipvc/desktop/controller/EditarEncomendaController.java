package com.ipvc.desktop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EditarEncomendaController {

    @FXML private DatePicker campoDataEncomenda;
    @FXML private TextField campoEstadoId;
    @FXML private TextField campoValorTotal;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private Integer encomendaId; // Vai ser passado do controller principal

    public void setEncomendaId(Integer id) {
        this.encomendaId = id;
    }

    @FXML
    private void atualizarEncomenda() {
        try {
            var body = mapper.createObjectNode();
            body.put("dataEncomenda", campoDataEncomenda.getValue().toString());
            body.put("estadoId", Integer.parseInt(campoEstadoId.getText()));
            body.put("valorTotal", new BigDecimal(campoValorTotal.getText()));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/encomendas-clientes/" + encomendaId))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        System.out.println("Encomenda atualizada!");
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
