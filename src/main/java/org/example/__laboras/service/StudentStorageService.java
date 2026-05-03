package org.example.__laboras.service;

import org.example.__laboras.model.Student;

import java.io.*;
import java.nio.file.*;
import java.util.List;


public class StudentStorageService {

    private static final String FILE_PATH = "students_data.txt";

    public void save(List<Student> students) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            for (Student s : students) {
                writer.write(
                        escape(s.getId())        + "|" +
                                escape(s.getFirstName()) + "|" +
                                escape(s.getLastName())  + "|" +
                                escape(s.getGroupName())
                );
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save student data: " + e.getMessage());
        }
    }

    private String escape(String value) {
        return value == null ? "" : value;
    }
}