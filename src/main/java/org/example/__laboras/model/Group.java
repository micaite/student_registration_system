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
    private List<AttendanceRecord> attendanceRecords = new ArrayList<>();

    public Group(String name){
        this.name = name;
    }

    public void addStudent (Student s){
        students.add(s);
        s.setGroupName(this.name);
    }

    public List<Student> getStudents() {
        return students;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public void addAttendance(AttendanceRecord record) {
        attendanceRecords.add(record);
    }

    @Override
    public List<AttendanceRecord> generateReport(LocalDate dateFrom, LocalDate dateTo) {
        return attendanceRecords.stream()
                .filter(r -> !r.getDate().isBefore(dateFrom) && !r.getDate().isAfter(dateTo))
                .toList();
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
