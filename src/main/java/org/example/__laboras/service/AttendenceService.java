package org.example.__laboras.service;

import org.example.__laboras.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AttendenceService {
    private List<AttendanceRecord> records = new ArrayList<>();

    public void markAttendance (Student student, LocalDate date, boolean present){
        if (present) {
            records.add(new PresentRecord(student, date));
        } else {
            records.add(new AbsentRecord(student, date));
        }
    }

    public List<AttendanceRecord> getByStudent(Student student){
        return records.stream()
                .filter(r -> r.getStudent().getId().equals(student.getId()))
                .collect(Collectors.toList());
    }

    public List<AttendanceRecord> getByGroup(Group group) {
        return records.stream()
                .filter(r -> group.getStudents().contains(r.getStudent()))
                .collect(Collectors.toList());
    }

    public List<LocalDate> getFilledDays() {
        return records.stream()
                .map(AttendanceRecord::getDate)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<AttendanceRecord> getAll() {
        return records;
    }


}
