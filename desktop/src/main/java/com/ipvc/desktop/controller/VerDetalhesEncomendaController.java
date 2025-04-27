package com.ipvc.desktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class VerDetalhesEncomendaController {

    @FXML private Label labelIdEncomenda;
    @FXML private Label labelCliente;
    @FXML private Label labelData;
    @FXML private Label labelEstado;
    @FXML private Label labelValorTotal;

    public void setDetalhes(String id, String cliente, String data, String estado, String valor) {
        labelIdEncomenda.setText(id);
        labelCliente.setText(cliente);
        labelData.setText(data);
        labelEstado.setText(estado);
        labelValorTotal.setText(valor + " €");
    }

    @FXML
    private void fechar() {
        Stage stage = (Stage) labelIdEncomenda.getScene().getWindow();
        stage.close();
    }
}
