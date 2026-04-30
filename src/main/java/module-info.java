module org.example.__laboras {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.csv;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;


    opens org.example.__laboras to javafx.fxml;
    exports org.example.__laboras;
    exports org.example.__laboras.model;
    opens org.example.__laboras.model to javafx.fxml;
}