package org.example.__laboras.model;

import org.example.__laboras.interfaces.Exportable;

public class Student implements Exportable {
    final private String id;
    final private String name;
    final private String surname;
    private String groupName;

    public Student (String id, String firstName, String lastName, String groupName){
        this.id = id;
        this.name = firstName;
        this.surname = lastName;
        this.groupName = groupName;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getGroupName() {return groupName; }
    // ir setteriai

    @Override
    public void exportToCSV(String filePath) throws Exception{
        // implementuosiu veliau
    }

    @Override
    public void exportToExcel (String filePath) throws Exception{
        // veliau
    }
}
