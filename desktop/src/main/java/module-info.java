module com.ipvc.desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;

    opens com.ipvc.desktop to javafx.fxml;

    exports com.ipvc.desktop;
    exports com.ipvc.desktop.models;
    opens com.ipvc.desktop.models to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.ipvc.desktop.controller;
    opens com.ipvc.desktop.controller to javafx.fxml;
    exports com.ipvc.desktop.controllers;
    opens com.ipvc.desktop.controllers to javafx.fxml;
    exports com.ipvc.desktop.Request;
    opens com.ipvc.desktop.Request to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.ipvc.desktop.Response;
    opens com.ipvc.desktop.Response to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.ipvc.desktop.Service;
    opens com.ipvc.desktop.Service to com.fasterxml.jackson.databind, javafx.fxml;
}