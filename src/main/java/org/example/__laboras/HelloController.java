package org.example.__laboras;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.__laboras.model.Group;
import org.example.__laboras.model.Student;
import org.example.__laboras.service.AttendenceService;
import org.example.__laboras.service.ImportExportService;

import java.util.List;

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
    @FXML private TableColumn<Student, Void> colActions;

    ///
    @FXML private ListView<Group> groupsList;
    @FXML private TableView<Student> groupStudentsTable;
    @FXML private TableColumn<Student, String> colGroupStudentId;
    @FXML private TableColumn<Student, String> colGroupFirstName;
    @FXML private TableColumn<Student, String> colGroupLastName;

    ///
    @FXML private ComboBox comboGroupName;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;

    private ObservableList<Student> studentList = FXCollections.observableArrayList(
            new Student("2513666", "Ona", "Onutaitė", "Not assigned"),
            new Student("2513667","Paulius", "Pauliunas", "Not assigned"),
            new Student("2513668","Saule", "Saulytė", "Not assigned")
    );
    private ObservableList<Group> groupList = FXCollections.observableArrayList();
    private AttendenceService attendenceService = new AttendenceService();
    private ImportExportService importExportService = new ImportExportService();

    @FXML
    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGroup.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        studentsTable.setItems(studentList);
        ///
        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            {
                editBtn.setOnAction(e -> {
                    Student s = getTableView().getItems().get(getIndex());
                    handleEditStudent(s);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : editBtn);
            }
        });
        ///
        colGroupStudentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colGroupFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colGroupLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        groupsList.setItems(groupList);

        groupsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                groupStudentsTable.setItems(FXCollections.observableArrayList(newVal.getStudents()));
            }
        });
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
                return new Student(idField.getText(), firstNameField.getText(), lastNameField.getText(), "Not assigned" );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(student -> studentList.add(student));
    }

    @FXML
    private void handleImportCSV() {
        try{
            List<Student> importedStudents = importExportService.importStudentsFromCSV("students.csv");

            studentList.clear();
            studentList.addAll(importedStudents);

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    private void handleImportExcel() {
                try{
            List<Student> importedStudents = importExportService.importStudentsFromExcel("students.xlsx");

            studentList.clear();
            studentList.addAll(importedStudents);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleEditStudent(Student student) {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Edit Student");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField idField = new TextField(student.getId());
        TextField firstNameField = new TextField(student.getFirstName());
        TextField lastNameField = new TextField(student.getLastName());

        ComboBox<String> groupCombo = new ComboBox<>();
        groupCombo.getItems().addAll(groupList.stream()
                .map(Group::getName)
                .toList());
        groupCombo.setValue(student.getGroupName());

        VBox vbox = new VBox(8,
                new Label("ID:"), idField,
                new Label("First Name:"), firstNameField,
                new Label("Last Name:"), lastNameField,
                new Label("Group:"), groupCombo
        );
        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                student.setId(idField.getText());
                student.setFirstName(firstNameField.getText());
                student.setLastName(lastNameField.getText());
                student.setGroupName(groupCombo.getValue());
                return student;
            }
            return null;
        });

        String oldGroupName = student.getGroupName();

        dialog.showAndWait();

        String newGroupName = student.getGroupName();
        if (newGroupName != null && !newGroupName.equals(oldGroupName)) {
            groupList.forEach(g -> g.getStudents().remove(student));
            groupList.stream()
                    .filter(g -> g.getName().equals(newGroupName))
                    .findFirst()
                    .ifPresent(g -> g.addStudent(student));
        }

        studentsTable.refresh();
        Group selectedGroup = groupsList.getSelectionModel().getSelectedItem();
        if (selectedGroup != null) {
            groupStudentsTable.setItems(FXCollections.observableArrayList(selectedGroup.getStudents()));
        }
    }

    ///
    @FXML
    private void handleGererate(){

    }
    ///

    @FXML
    private void handleNewGroup() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Group");
        dialog.setContentText("Group name:");
        dialog.showAndWait().ifPresent(name -> {
            groupList.add(new Group(name));
            groupFilter.getItems().add(name);
        });
    }

    @FXML
    private void handleDeleteGroup() {
        Group selected = groupsList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            groupList.remove(selected);
            groupFilter.getItems().remove(selected.getName());
        }
    }

    @FXML
    private void handleAddStudentToGroup() {
        Group selectedGroup = groupsList.getSelectionModel().getSelectedItem();
        if (selectedGroup == null) return;

        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Add Student to Group");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Student> studentCombo = new ComboBox<>();
        studentCombo.setItems(studentList);
        studentCombo.setPromptText("Select student");

        dialog.getDialogPane().setContent(new VBox(8, new Label("Student:"), studentCombo));

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) return studentCombo.getValue();
            return null;
        });

        dialog.showAndWait().ifPresent(student -> {
            student.setGroupName(selectedGroup.getName());
            selectedGroup.addStudent(student);
            groupStudentsTable.setItems(FXCollections.observableArrayList(selectedGroup.getStudents()));
            studentsTable.refresh();
        });
    }

    @FXML
    private void handleRemoveStudentFromGroup() {
        Group selectedGroup = groupsList.getSelectionModel().getSelectedItem();
        Student selectedStudent = groupStudentsTable.getSelectionModel().getSelectedItem();
        if (selectedGroup == null || selectedStudent == null) return;

        selectedGroup.getStudents().remove(selectedStudent);
        selectedStudent.setGroupName("");
        groupStudentsTable.setItems(FXCollections.observableArrayList(selectedGroup.getStudents()));
        studentsTable.refresh();
    }
}
