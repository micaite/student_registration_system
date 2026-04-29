package org.example.__laboras.model;

import org.example.__laboras.interfaces.Exportable;
import org.example.__laboras.interfaces.Reportable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Group implements Exportable, Reportable {
    private String name;
    private List<Student> students = new ArrayList<>();

    public Group(String name){
        this.name = name;
    }

    public void addStudent (Student s){
        students.add(s);
    }

    public List<Student> getStudents() {
        return students;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<AttendanceRecord> generateReport (LocalDate dateFrom, LocalDate dateTo) {
        // implementuosiu veliau
        return new ArrayList<>();
    }

    @Override
    public void exportToPDF(String filePath) throws Exception {
        // implementuosim vėliau
    }

    @Override
    public void exportToCSV(String filePath) throws Exception {
        // implementuosim vėliau
    }

    @Override
    public void exportToExcel(String filePath) throws Exception {
        // implementuosim vėliau
    }

}
