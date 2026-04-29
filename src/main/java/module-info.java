module org.example.__laboras {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.__laboras to javafx.fxml;
    exports org.example.__laboras;
    exports org.example.__laboras.model;
    opens org.example.__laboras.model to javafx.fxml;
}