package org.example.__laboras;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("demo.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 620, 840);
        stage.setTitle("Studentų registravimo sistema");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
