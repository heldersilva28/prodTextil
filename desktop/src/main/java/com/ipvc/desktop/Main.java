package com.ipvc.desktop;

import com.ipvc.desktop.Service.AuthService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        AuthService authService = new AuthService();

        String fxmlPath = authService.existemUtilizadores()
                ? "/com/ipvc/desktop/views/login.fxml"
                : "/com/ipvc/desktop/views/registo.fxml";

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
        Scene scene = new Scene(root, 1000, 600); // largura maior para o layout em HBox

        scene.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/com/ipvc/desktop/style/login.css")).toExternalForm());

        stage.setTitle("Gestão Têxtil - Autenticação");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
