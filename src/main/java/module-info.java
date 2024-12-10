module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    exports com.example.demo.controller;
    opens com.example.demo.levels to javafx.fxml;
    exports com.example.demo.levels;
    exports com.example.demo.objects;
    opens com.example.demo.objects to javafx.fxml;
    opens com.example.demo.controller to javafx.fxml;
}