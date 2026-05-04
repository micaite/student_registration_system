package org.example.__laboras.interfaces;

import org.example.__laboras.model.AttendanceRecord;

import java.time.LocalDate;
import java.util.List;

public interface Reportable {
    List<AttendanceRecord> generateReport(LocalDate dateFrom, LocalDate dateTo);

}
