package org.example.__laboras;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.example.__laboras.model.AttendanceRecord;
import org.example.__laboras.model.Group;
import org.example.__laboras.model.Student;
import org.example.__laboras.service.AttendenceService;
import org.example.__laboras.service.ImportExportService;
import org.example.__laboras.service.ReportService;
import org.example.__laboras.service.StudentStorageService;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class HelloController {

    // Students tab
    @FXML private TableView<Student> studentsTable;
    @FXML private TableColumn<Student, String> colId;
    @FXML private TableColumn<Student, String> colFirstName;
    @FXML private TableColumn<Student, String> colLastName;
    @FXML private TableColumn<Student, String> colGroup;
    @FXML private TableColumn<Student, Void> colActions;

    // Groups tab
    @FXML private ListView<Group> groupsList;
    @FXML private TableView<Student> groupStudentsTable;
    @FXML private TableColumn<Student, String> colGroupStudentId;
    @FXML private TableColumn<Student, String> colGroupFirstName;
    @FXML private TableColumn<Student, String> colGroupLastName;

    // Attendance tab
    @FXML private ComboBox<Group>   attendanceGroupFilter;
    @FXML private ComboBox<Student> attendanceStudentFilter;
    @FXML private DatePicker        attendanceDatePicker;
    @FXML private TableView<Student>       attendanceTable;
    @FXML private TableColumn<Student, String> colAttStudent;
    @FXML private TableColumn<Student, String> colAttStatus;
    @FXML private TableColumn<Student, Void>   colAttMark;
    @FXML private TableView<LocalDate>       filledDaysTable;
    @FXML private TableColumn<LocalDate, String> colFilledDay;

    // Report tab
    @FXML private ComboBox<Group> comboGroupName;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;

    // Data and services
    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private ObservableList<Group>   groupList   = FXCollections.observableArrayList();

    private final AttendenceService   attendenceService   = new AttendenceService();
    private final ImportExportService importExportService = new ImportExportService();
    private final StudentStorageService storageService    = new StudentStorageService();

    @FXML
    public void initialize() {

        // demo data
        studentList.addAll(
                new Student("2513666", "Ona",    "Onutaitė",  "Not assigned"),
                new Student("2513667", "Paulius","Pauliunas",  "Not assigned"),
                new Student("2513668", "Saule",  "Saulytė",   "Not assigned")
        );

        groupList.addAll(new Group("A"), new Group("B"));
        groupList.get(0).addStudent(studentList.get(0));
        groupList.get(1).addStudent(studentList.get(1));

        // Students table
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGroup.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        studentsTable.setItems(studentList);

        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            {
                editBtn.setOnAction(e -> handleEditStudent(
                        getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : editBtn);
            }
        });

        // Groups tab
        colGroupStudentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colGroupFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colGroupLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        groupsList.setItems(groupList);
        groupsList.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n != null)
                groupStudentsTable.setItems(FXCollections.observableArrayList(n.getStudents()));
        });

        // Attendance tab – group filter
        attendanceGroupFilter.setItems(groupList);

        // Attendance tab – student filter
        attendanceStudentFilter.setItems(studentList);
        attendanceStudentFilter.setPromptText("Filter by student (optional)");

        colAttStudent.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getFirstName() + " " + c.getValue().getLastName()));

        colAttStatus.setCellValueFactory(c -> {
            Student s = c.getValue();
            LocalDate date = attendanceDatePicker.getValue();
            if (date == null) return new SimpleStringProperty("");
            boolean present = attendenceService.getByStudent(s).stream()
                    .anyMatch(r -> r.getDate().equals(date) && r.getStatus().equals("Present"));
            return new SimpleStringProperty(present ? "Present" : "Absent");
        });

        colAttMark.setCellFactory(col -> new TableCell<>() {
            private final CheckBox cb = new CheckBox();
            {
                cb.setOnAction(e -> {
                    Student s = getTableView().getItems().get(getIndex());
                    LocalDate date = attendanceDatePicker.getValue();
                    if (date == null) return;
                    attendenceService.getAll().removeIf(
                            r -> r.getStudent().equals(s) && r.getDate().equals(date));
                    attendenceService.markAttendance(s, date, cb.isSelected());
                    attendanceTable.refresh();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                Student s = getTableView().getItems().get(getIndex());
                LocalDate date = attendanceDatePicker.getValue();
                if (date != null) {
                    boolean present = attendenceService.getAll().stream()
                            .anyMatch(r -> r.getStudent().equals(s)
                                    && r.getDate().equals(date)
                                    && r.getStatus().equals("Present"));
                    cb.setSelected(present);
                }
                setGraphic(cb);
            }
        });

        // Filled-days table
        colFilledDay.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().toString()));
        filledDaysTable.setItems(FXCollections.observableArrayList());

        // Report tab
        comboGroupName.setItems(groupList);
    }

    // Students tab handlers

    @FXML
    private void handleAddStudent() {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Add Student");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField idField        = new TextField();
        TextField firstNameField = new TextField();
        TextField lastNameField  = new TextField();

        dialog.getDialogPane().setContent(new VBox(8,
                new Label("Student ID:"), idField,
                new Label("First Name:"), firstNameField,
                new Label("Last Name:"),  lastNameField));

        dialog.setResultConverter(btn -> btn == ButtonType.OK
                ? new Student(idField.getText(), firstNameField.getText(),
                lastNameField.getText(), "Not assigned")
                : null);

        dialog.showAndWait().ifPresent(s -> {
            studentList.add(s);
            storageService.save(studentList);
        });
    }

    @FXML
    private void handleImportCSV() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Import CSV");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fc.showOpenDialog(studentsTable.getScene().getWindow());
        if (file == null) return;
        try {
            studentList.setAll(importExportService.importStudentsFromCSV(file.getAbsolutePath()));
            storageService.save(studentList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to import CSV");
        }
    }

    @FXML
    private void handleImportExcel() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Import Excel");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fc.showOpenDialog(studentsTable.getScene().getWindow());
        if (file == null) return;
        try {
            studentList.setAll(importExportService.importStudentsFromExcel(file.getAbsolutePath()));
            storageService.save(studentList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to import Excel");
        }
    }

    private void handleEditStudent(Student student) {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Edit Student");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField idField        = new TextField(student.getId());
        TextField firstNameField = new TextField(student.getFirstName());
        TextField lastNameField  = new TextField(student.getLastName());

        ComboBox<String> groupCombo = new ComboBox<>();
        groupCombo.getItems().addAll(groupList.stream().map(Group::getName).toList());
        groupCombo.setValue(student.getGroupName());

        dialog.getDialogPane().setContent(new VBox(8,
                new Label("ID:"),         idField,
                new Label("First Name:"), firstNameField,
                new Label("Last Name:"),  lastNameField,
                new Label("Group:"),      groupCombo));

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                student.setId(idField.getText());
                student.setFirstName(firstNameField.getText());
                student.setLastName(lastNameField.getText());
                student.setGroupName(groupCombo.getValue());
                return student;
            }
            return null;
        });

        String oldGroup = student.getGroupName();
        dialog.showAndWait();
        String newGroup = student.getGroupName();

        if (newGroup != null && !newGroup.equals(oldGroup)) {
            groupList.forEach(g -> g.getStudents().remove(student));
            groupList.stream()
                    .filter(g -> g.getName().equals(newGroup))
                    .findFirst()
                    .ifPresent(g -> g.addStudent(student));
        }

        storageService.save(studentList);
        studentsTable.refresh();

        Group sel = groupsList.getSelectionModel().getSelectedItem();
        if (sel != null)
            groupStudentsTable.setItems(FXCollections.observableArrayList(sel.getStudents()));

        attendanceStudentFilter.setItems(studentList);
    }

    // Groups tab handlers

    @FXML private void handleNewGroup() {
        TextInputDialog d = new TextInputDialog();
        d.setTitle("New Group"); d.setContentText("Group name:");
        d.showAndWait().ifPresent(name -> {
            groupList.add(new Group(name));
            attendanceGroupFilter.setItems(groupList);
            comboGroupName.setItems(groupList);
        });
    }

    @FXML private void handleDeleteGroup() {
        Group sel = groupsList.getSelectionModel().getSelectedItem();
        if (sel != null) groupList.remove(sel);
    }

    @FXML private void handleAddStudentToGroup() {
        Group group = groupsList.getSelectionModel().getSelectedItem();
        if (group == null) return;

        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Add Student to Group");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        ComboBox<Student> combo = new ComboBox<>(studentList);
        combo.setPromptText("Select student");
        dialog.getDialogPane().setContent(new VBox(8, new Label("Student:"), combo));
        dialog.setResultConverter(btn -> btn == ButtonType.OK ? combo.getValue() : null);

        dialog.showAndWait().ifPresent(s -> {
            s.setGroupName(group.getName());
            group.addStudent(s);
            groupStudentsTable.setItems(FXCollections.observableArrayList(group.getStudents()));
            studentsTable.refresh();
            storageService.save(studentList);
        });
    }

    @FXML private void handleRemoveStudentFromGroup() {
        Group group    = groupsList.getSelectionModel().getSelectedItem();
        Student student = groupStudentsTable.getSelectionModel().getSelectedItem();
        if (group == null || student == null) return;
        group.getStudents().remove(student);
        student.setGroupName("");
        groupStudentsTable.setItems(FXCollections.observableArrayList(group.getStudents()));
        studentsTable.refresh();
        storageService.save(studentList);
    }

    @FXML private void handleExportCSV() {
        Group sel = groupsList.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Error", "Please select a group"); return; }
        try {
            String path = System.getProperty("user.home") + "/Desktop/students.csv";
            sel.exportToCSV(path);
            showAlert("Success", "CSV exported to Desktop");
        } catch (Exception e) { e.printStackTrace(); showAlert("Error", "Failed to export CSV"); }
    }

    @FXML private void handleExportExcel() {
        Group sel = groupsList.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Error", "Please select a group"); return; }
        try {
            String path = System.getProperty("user.home") + "/Desktop/students.xlsx";
            sel.exportToExcel(path);
            showAlert("Success", "Excel exported to Desktop");
        } catch (Exception e) { e.printStackTrace(); showAlert("Error", "Failed to export Excel"); }
    }

    // Attendance tab handlers

    @FXML
    private void handleLoadAttendance() {
        Student studentFilter = attendanceStudentFilter.getValue();
        Group   groupFilter   = attendanceGroupFilter.getValue();

        if (studentFilter != null) {
            attendanceTable.setItems(FXCollections.observableArrayList(studentFilter));
        } else if (groupFilter != null) {
            attendanceTable.setItems(FXCollections.observableArrayList(groupFilter.getStudents()));
        } else {
            showAlert("Info", "Please choose a group or a student first.");
        }
        attendanceTable.refresh();
    }

    @FXML
    private void handleSaveAttendance() {
        Group group    = attendanceGroupFilter.getValue();
        LocalDate date = attendanceDatePicker.getValue();
        if (group == null || date == null) return;

        for (Student s : group.getStudents()) {
            if (attendenceService.getByStudent(s).stream()
                    .noneMatch(r -> r.getDate().equals(date))) {
                attendenceService.markAttendance(s, date, false);
            }
        }
        attendanceTable.refresh();
        handleShowFilledDays();
    }


    @FXML
    private void handleShowFilledDays() {
        List<LocalDate> days = attendenceService.getFilledDays().stream()
                .sorted()
                .collect(Collectors.toList());
        filledDaysTable.setItems(FXCollections.observableArrayList(days));
    }

    // Report tab handlers

    @FXML
    private void handleGenerate() {
        Group     group = comboGroupName.getValue();
        LocalDate from  = dateFrom.getValue();
        LocalDate to    = dateTo.getValue();

        if (group == null || from == null || to == null) {
            showAlert("Error", "Please select a group and date range");
            return;
        }
        try {
            List<AttendanceRecord> groupRecords = attendenceService.getByGroup(group);
            group.setAttendanceRecords(groupRecords);
            List<AttendanceRecord> report = group.generateReport(from, to);

            String path = System.getProperty("user.home") + "/Desktop/report_"
                    + group.getName() + "_" + from + "_to_" + to + ".pdf";

            new ReportService().exportToPDF(group, report, from, to, path);

            showAlert("✅ PDF saved", "Report saved to:\n" + path);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to generate PDF:\n" + e.getMessage());
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}