package com.ipvc.desktop.controller;

import com.ipvc.desktop.Response.AuthResponse;
import com.ipvc.desktop.Service.AuthService;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private VBox loginVBox;
    @FXML
    private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void onLoginClick() {
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            AuthResponse response = authService.login(email, password);

            if (response.isSuccess()) {
                errorLabel.setVisible(false);
                // Exibe o toast antes de mudar para a próxima página
                showToast(response.getMessage(), () -> {
                    if(authService.obterCargoUtilizadorPorEmail(email) == 1){
                        // Carrega a próxima tela (painel admin)
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ipvc/desktop/views/painel-admin.fxml"));
                            Parent root = fxmlLoader.load();

                            Scene scene = new Scene(root, 1000, 650);
                            scene.getStylesheets().add(Objects.requireNonNull(
                                    getClass().getResource("/com/ipvc/desktop/style/painel-admin.css")).toExternalForm());
                            Stage stage = new Stage();
                            stage.setTitle("Painel do Administrador");
                            stage.setScene(scene);

                            stage.show();

                            // Fecha a janela atual
                            Stage currentStage = (Stage) loginVBox.getScene().getWindow();
                            currentStage.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            } else {
                // Feedback de erro com animação shake
                errorLabel.setText(response.getMessage());
                errorLabel.setVisible(true);
                shakeNode(loginVBox);
            }

        } catch (Exception e) {
            errorLabel.setText("Erro na ligação ao servidor.");
            errorLabel.setVisible(true);
            shakeNode(loginVBox);
            e.printStackTrace();
        }
    }

    private void shakeNode(VBox node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(70), node);
        tt.setFromX(-10);
        tt.setByX(20);
        tt.setCycleCount(4);
        tt.setAutoReverse(true);
        tt.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FadeTransition ft = new FadeTransition(Duration.millis(800), loginVBox);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public void showToast(String message, Runnable onToastDismissed) {
        // Criando a Label para o "toast"
        Label toast = new Label(message);
        toast.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-text-fill: white; -fx-padding: 10;");

        // Adicionando a label em um container (StackPane)
        StackPane root = new StackPane();
        root.getChildren().add(toast);
        loginVBox.getChildren().add(root); // Adiciona o "toast" à cena atual

        // Criando a animação de fade-in
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), toast);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // Criando a animação de fade-out
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), toast);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(2));

        fadeOut.setOnFinished(e -> {
            loginVBox.getChildren().remove(root); // Remove o toast após o fade-out
            onToastDismissed.run(); // Executa a navegação após o toast desaparecer
        });

        fadeIn.play();
        fadeOut.play();
    }
}
