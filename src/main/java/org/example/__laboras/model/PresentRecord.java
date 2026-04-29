package org.example.__laboras.model;

import java.time.LocalDate;

public class PresentRecord extends AttendanceRecord {
    public PresentRecord (Student student, LocalDate date){
        super(student, date);
    }

     @Override
    public String getStatus(){
        return "Buvo";
     }
}
