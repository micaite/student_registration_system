package org.example.__laboras.model;

import org.example.__laboras.interfaces.Exportable;
import org.example.__laboras.interfaces.Reportable;
import org.example.__laboras.service.ReportService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        LocalDate from = attendanceRecords.stream()
                .map(AttendanceRecord::getDate)
                .min(LocalDate::compareTo)
                .orElse(LocalDate.now());

        LocalDate to = attendanceRecords.stream()
                .map(AttendanceRecord::getDate)
                .max(LocalDate::compareTo)
                .orElse(LocalDate.now());

        new org.example.__laboras.service.ReportService()
                .exportToPDF(this, attendanceRecords, from, to, filePath);
    }

    @Override
    public void exportToCSV(String filePath) throws Exception {
        new org.example.__laboras.service.ImportExportService()
                .exportStudentsToCSV(students, filePath);
    }

    @Override
    public void exportToExcel(String filePath) throws Exception {
        new org.example.__laboras.service.ImportExportService()
                .exportStudentsToExcel(students, filePath);
    }

}
