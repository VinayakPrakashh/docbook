package com.example.docbook;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GeneratePdf extends AppCompatActivity {

    private File invoiceFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_pdf);

        // Generate the invoice PDF
        generateInvoice("John Doe", "Product 1, Product 2", "$100");

        // Set up the download button
        Button downloadButton = findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadInvoice();
            }
        });
    }

    private void generateInvoice(String customerName, String orderDetails, String summary) {
        // Create a new PDF document
        Document document = new Document();
        try {
            // Set the file path and name for the PDF file
            invoiceFile = new File(getExternalFilesDir(null), "invoice.pdf");
            FileOutputStream fileOutputStream = new FileOutputStream(invoiceFile);

            // Set up the PDF writer
            PdfWriter.getInstance(document, fileOutputStream);

            // Open the document for writing
            document.open();

            // Add the customer name, order details, and summary to the document
            document.add(new Paragraph("Customer Name: " + customerName));
            document.add(new Paragraph("Order Details: " + orderDetails));
            document.add(new Paragraph("Summary: " + summary));

            // Close the document
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadInvoice() {
        File srcFile = new File(getExternalFilesDir(null), "invoice.pdf");
        File destFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "invoice.pdf");
        try {
            copyFile(srcFile, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Invoice downloaded to Downloads directory", Toast.LENGTH_SHORT).show();
    }
    private static void copyFile(File sourceFile, File destFile) throws IOException {
        InputStream inputStream = new FileInputStream(sourceFile);
        OutputStream outputStream = new FileOutputStream(destFile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }


}
