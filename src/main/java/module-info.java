module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires poi;
    requires poi.ooxml;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}