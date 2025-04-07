package com.ipvc.desktop;

import com.ipvc.desktop.models.AuthResponse;
import com.ipvc.desktop.models.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private final AuthService authService = new AuthService();

    @FXML
    private void onLoginClick() {
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            AuthResponse response = authService.login(email, password);
            Alert alert = new Alert(response.isSuccess() ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            alert.setContentText(response.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erro na ligação ao servidor.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
