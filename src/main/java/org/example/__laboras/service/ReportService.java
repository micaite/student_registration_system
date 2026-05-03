package org.example.__laboras.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.__laboras.model.AttendanceRecord;
import org.example.__laboras.model.Group;
import org.example.__laboras.model.Student;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class ReportService {

    public void exportToPDF (Group group, List<AttendanceRecord> records, LocalDate dateFrom, LocalDate dateTo, String filePath) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        document.add(new Paragraph("Group: " + group.getName()));
        document.add(new Paragraph("Timeline: " + dateFrom + " - " + dateTo));
        document.add(new Paragraph(" "));

        for (AttendanceRecord r : records){
            Student s = r.getStudent();
            String e  = s.getFirstName() + " " + s.getLastName()
                    + " | " + r.getDate()
                    + " | " + r.getStatus();
            document.add(new Paragraph(e));
        }

        document.close();
    }
}
