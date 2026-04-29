package org.example.__laboras;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TableX extends Application {

    ObservableList<Person> personData = FXCollections.observableArrayList(
            new Person("Ona", "Onutaite", 25),
            new Person("Paulius", "Pauliunas", 78),
            new Person("Saule", "Saulyte", 7)
    );

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = new AnchorPane();

        Scene scene = new Scene(root, 600, 400);
        primaryStage.show();
        primaryStage.setScene(scene);

        TableView<Person> table = new TableView();
        root.getChildren().addAll(table);

        TableColumn nameCol = new TableColumn("Vardas");
        TableColumn surnameCol = new TableColumn("Pavarde");
        TableColumn ageCol = new TableColumn("Amzius");

        nameCol.setMinWidth(100);
        surnameCol.setMinWidth(100);
        ageCol.setMinWidth(100);

        nameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("vardas"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("pavarde"));
        ageCol.setCellValueFactory(new PropertyValueFactory<Person, Integer>("amzius"));

        table.getColumns().addAll(nameCol, surnameCol, ageCol);
        table.setItems(personData);


    }
}