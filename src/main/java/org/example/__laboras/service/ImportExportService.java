package org.example.__laboras.service;

import org.apache.commons.csv.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.__laboras.model.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ImportExportService {
    public void exportStudentsToCSV(List<Student> students, String filePath) throws Exception {
        FileWriter fw = new FileWriter(filePath);
        CSVPrinter printer = new CSVPrinter(fw, CSVFormat.DEFAULT
                .withHeader("ID", "Vardas", "Pavardė", "Grupė"));

        for (Student s: students) {
            printer.printRecord(s.getId(), s.getFirstName(), s.getLastName(), s.getGroupName());
        }

        printer.close();

    }

    public List<Student> importStudentsFromCSV(String filePath) throws Exception {
        List<Student> students = new ArrayList<>();
        Reader reader = new FileReader(filePath);
        CSVParser parser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(reader);

        for (CSVRecord record : parser) {
            students.add(new Student(
                    record.get("ID"),
                    record.get("Vardas"),
                    record.get("Pavardė"),
                    record.get("Grupė")
            ));
        }

        return students;
    }

    public void exportStudentsToExcel(List<Student> students, String filePath) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Studentai");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Vardas");
        header.createCell(2).setCellValue("Pavardė");
        header.createCell(3).setCellValue("Grupė");

        int rowNum = 1;
        for (Student s : students) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(s.getId());
            row.createCell(1).setCellValue(s.getFirstName());
            row.createCell(2).setCellValue(s.getLastName());
            row.createCell(3).setCellValue(s.getGroupName());
        }

        FileOutputStream fos = new FileOutputStream(filePath);
        workbook.write(fos);
        workbook.close();
    }

    public List<Student> importStudentsFromExcel(String filePath) throws Exception {
        List<Student> students = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            students.add(new Student(
                    row.getCell(0).getStringCellValue(),
                    row.getCell(1).getStringCellValue(),
                    row.getCell(2).getStringCellValue(),
                    row.getCell(3).getStringCellValue()
            ));
        }

        workbook.close();
        return students;
    }


}
