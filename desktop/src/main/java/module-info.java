module com.ipvc.desktop {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ipvc.desktop to javafx.fxml;
    exports com.ipvc.desktop;
}