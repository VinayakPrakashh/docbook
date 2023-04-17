package com.example.docbook;
// Import the necessary classes from your PDF library

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class InvoiceGenerator {

    public static void generateInvoice(String customerName, String orderDetails, String summary) {

        // Create a new PDF document
        Document document = new Document();
        try {
            // Set the file path and name for the PDF file
            PdfWriter.getInstance(document, new FileOutputStream("invoice.pdf"));

            // Open the document for writing
            document.open();

            // Add the customer name, order details, and summary to the document
            document.add(new Paragraph("Customer Name: " + customerName));
            document.add(new Paragraph("Order Details: " + orderDetails));
            document.add(new Paragraph("Summary: " + summary));

            // Close the document
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
