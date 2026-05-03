package org.example.__laboras.model;

import org.example.__laboras.interfaces.Exportable;
import org.example.__laboras.service.ImportExportService;

public class Student implements Exportable {
    private String id;
    private String firstName;
    private String lastName;
    private String groupName;

    public Student (String id, String firstName, String lastName, String groupName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupName = groupName;
    }

    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getGroupName() {return groupName; }
    // ir setteriai
    public void setId(String id) {this.id= id;}
    public void setFirstName(String firstName) {this.firstName = firstName; }
    public void setLastName(String lastName) {this.lastName = lastName; }
    public void setGroupName(String groupName) {this.groupName = groupName; }


    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public void exportToCSV(String filePath) throws Exception{
        new ImportExportService().exportStudentsToCSV(java.util.List.of(this), filePath);
    }

    @Override
    public void exportToExcel (String filePath) throws Exception{
        new ImportExportService().exportStudentsToExcel(java.util.List.of(this), filePath);
    }
}
