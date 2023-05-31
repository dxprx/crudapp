/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliares;

/**
 *
 * @author oscar
 */
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class JListEnlazadosExample {

    public static void main(String[] args) {
        // Crear un nuevo documento PDF
        Document document = new Document();

        try {
            // Crear instancia de PdfWriter para escribir el documento en un archivo
            PdfWriter.getInstance(document, new FileOutputStream("tabla.pdf"));

            // Abrir el documento
            document.open();

            // Crear una tabla con 3 columnas
            PdfPTable table = new PdfPTable(3);

            // Agregar celdas a la tabla
            table.addCell(new PdfPCell(new Phrase("Columna 1")));
            table.addCell(new PdfPCell(new Phrase("Columna 2")));
            table.addCell(new PdfPCell(new Phrase("Columna 3")));

            // Agregar más filas y celdas según sea necesario
            table.addCell(new PdfPCell(new Phrase("Dato 1")));
            table.addCell(new PdfPCell(new Phrase("Dato 2")));
            table.addCell(new PdfPCell(new Phrase("Dato 3")));

            table.addCell(new PdfPCell(new Phrase("Dato 4")));
            table.addCell(new PdfPCell(new Phrase("Dato 5")));
            table.addCell(new PdfPCell(new Phrase("Dato 6")));

            // Agregar la tabla al documento
            document.add(table);

            // Cerrar el documento
            document.close();

            System.out.println("El archivo PDF se ha generado correctamente.");
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}