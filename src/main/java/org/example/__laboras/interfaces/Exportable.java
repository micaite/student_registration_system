package org.example.__laboras.interfaces;

public interface Exportable {
    void exportToCSV(String filePath) throws Exception;
    void exportToExcel(String filePath) throws Exception;
}
