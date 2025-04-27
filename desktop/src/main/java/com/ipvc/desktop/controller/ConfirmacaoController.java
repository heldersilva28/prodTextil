package com.ipvc.desktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ConfirmacaoController {

    @FXML private Label labelMensagem;

    private Runnable onConfirm;

    public void setMensagem(String mensagem) {
        labelMensagem.setText(mensagem);
    }

    public void setOnConfirm(Runnable onConfirm) {
        this.onConfirm = onConfirm;
    }

    @FXML
    private void cancelar() {
        ((Stage) labelMensagem.getScene().getWindow()).close();
    }

    @FXML
    private void confirmar() {
        if (onConfirm != null) onConfirm.run();
        ((Stage) labelMensagem.getScene().getWindow()).close();
    }
}
