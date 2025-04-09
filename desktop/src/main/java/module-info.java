module com.ipvc.desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens com.ipvc.desktop to javafx.fxml;

    exports com.ipvc.desktop;
    exports com.ipvc.desktop.models;
    opens com.ipvc.desktop.models to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.ipvc.desktop.controller;
    opens com.ipvc.desktop.controller to javafx.fxml;
}