package org.example.__laboras.model;

import org.example.__laboras.interfaces.Exportable;

public class Student implements Exportable {
    private String id;
    private String name;
    private String surname;

    public Student (String id, String firstName, String lastName){
        this.id = id;
        this.name = firstName;
        this.surname = lastName;
    }

    // getteriai ir setteriai

    @Override
    public void exportToCSV(String filePath) throws Exception{
        // implementuosiu veliau
    }

    @Override
    public void exportToExcel (String filePath){
        // veliau
    }
}
