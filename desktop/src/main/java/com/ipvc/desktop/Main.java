package com.ipvc.desktop;

import com.ipvc.desktop.models.AuthService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        AuthService authService = new AuthService();
        FXMLLoader fxmlLoader;

        if (authService.existemUtilizadores()) {
            fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        } else {
            fxmlLoader = new FXMLLoader(Main.class.getResource("registo.fxml"));
        }

        Scene scene = new Scene(fxmlLoader.load(), 320, 300);
        stage.setTitle("Autenticação");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
