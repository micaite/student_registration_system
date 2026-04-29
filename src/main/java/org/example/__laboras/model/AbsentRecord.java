package org.example.__laboras.model;

import java.time.LocalDate;

public class AbsentRecord extends AttendanceRecord {
    public AbsentRecord(Student student, LocalDate date) {
        super(student, date);
    }

    @Override
    public String getStatus() {
        return "Nebuvo";
    }
}
