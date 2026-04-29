package org.example.__laboras.model;

import java.time.LocalDate;

public abstract class AttendanceRecord {
     private Student student;
     private LocalDate date;

     public AttendanceRecord(Student student, LocalDate date){
         this.student = student;
         this.date = date;
     }

     public abstract String getStatus();

    public Student getStudent() {
        return student;
    }

    public LocalDate getDate() {
        return date;
    }

}
