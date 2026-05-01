package org.example.__laboras;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.apache.poi.hssf.record.pivottable.StreamIDRecord;
import org.example.__laboras.model.Group;
import org.example.__laboras.model.Student;
import org.example.__laboras.service.AttendenceService;
import org.example.__laboras.service.ImportExportService;

public class HelloController {
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> groupFilter;
    @FXML
    private TableView<Student> studentsTable;
    @FXML private TableColumn<Student, String> colId;
    @FXML private TableColumn<Student, String> colFirstName;
    @FXML private TableColumn<Student, String> colLastName;
    @FXML private TableColumn<Student, String> colGroup;


    private ObservableList<Student> studentList = FXCollections.observableArrayList(
            new Student("2513666", "Ona", "Onutaite"),
            new Student("2513667","Paulius", "Pauliunas"),
            new Student("2513668","Saule", "Saulyte")
    );
    private ObservableList<Group> groupList = FXCollections.observableArrayList();
    private AttendenceService attendenceService = new AttendenceService();
    private ImportExportService importExportService = new ImportExportService();

    @FXML
    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("surname"));
        //colGroup.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        studentsTable.setItems(studentList);
    }

    @FXML
    private void handleAddStudent() {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Add Student");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField idField = new TextField();
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();

        VBox vbox = new VBox(8,
                new Label("Student ID:"), idField,
                new Label("First Name:"), firstNameField,
                new Label("Last Name:"), lastNameField
        );
        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return new Student(idField.getText(), firstNameField.getText(), lastNameField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(student -> studentList.add(student));
    }

    @FXML
    private void handleImportCSV() {

    }

    @FXML
    private void handleImportExcel() {

    }
}
