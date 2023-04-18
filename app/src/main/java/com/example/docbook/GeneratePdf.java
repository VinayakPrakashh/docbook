package com.example.docbook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GeneratePdf extends AppCompatActivity {

    private File invoiceFile;
    String date2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String doctorName = getIntent().getStringExtra("doctorName");
        String specialization = getIntent().getStringExtra("specialization");
        String hospital = getIntent().getStringExtra("hospital");
        String myName = getIntent().getStringExtra("myName");
        String age = getIntent().getStringExtra("age");
        String gender = getIntent().getStringExtra("gender");
        String reason = getIntent().getStringExtra("reason");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");

// Generate the invoice PDF using the retrieved values
        generateInvoice(doctorName, specialization, hospital, myName, age, gender, reason, date, time);
                downloadInvoice();
                finish();

    }

    public void generateInvoice(String doctorName, String specialization, String hospital, String myName, String age, String gender, String reason, String date, String time) {
        // Create a new PDF document
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        date2=date;
        try {
            // Set the file path and name for the PDF file
            invoiceFile = new File(getExternalFilesDir(null), "Appointment("+date+").pdf");
            FileOutputStream fileOutputStream = new FileOutputStream(invoiceFile);

            // Set up the PDF writer
            PdfWriter.getInstance(document, fileOutputStream);

            // Open the document for writing
            document.open();

            // Add the header
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph header = new Paragraph("INVOICE", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            // Add a line separator
            LineSeparator line = new LineSeparator();
            document.add(new Chunk(line));

            // Add the customer details
            Font customerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Paragraph customerDetails = new Paragraph();
            customerDetails.add(new Phrase("Patient Details", customerFont));
            customerDetails.add(Chunk.NEWLINE);
            customerDetails.add(new Phrase("Name: " + myName));
            customerDetails.add(Chunk.NEWLINE);
            customerDetails.add(new Phrase("Age: " + age));
            customerDetails.add(Chunk.NEWLINE);
            customerDetails.add(new Phrase("Gender: " + gender));
            customerDetails.add(Chunk.NEWLINE);
            document.add(customerDetails);

            // Add a line separator
            document.add(new Chunk(line));

            // Add the doctor details
            Font doctorFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Paragraph doctorDetails = new Paragraph();
            doctorDetails.add(new Phrase("Doctor Details", doctorFont));
            doctorDetails.add(Chunk.NEWLINE);
            doctorDetails.add(new Phrase("Name: " + doctorName));
            doctorDetails.add(Chunk.NEWLINE);
            doctorDetails.add(new Phrase("Specialization: " + specialization));
            doctorDetails.add(Chunk.NEWLINE);
            doctorDetails.add(new Phrase("Hospital: " + hospital));
            doctorDetails.add(Chunk.NEWLINE);
            document.add(doctorDetails);

            // Add a line separator
            document.add(new Chunk(line));

            // Add the appointment details
            Font appointmentFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Paragraph appointmentDetails = new Paragraph();
            appointmentDetails.add(new Phrase("Appointment Details", appointmentFont));
            appointmentDetails.add(Chunk.NEWLINE);
            appointmentDetails.add(new Phrase("Reason for appointment: " + reason));
            appointmentDetails.add(Chunk.NEWLINE);
            appointmentDetails.add(new Phrase("Date: " + date));
            appointmentDetails.add(Chunk.NEWLINE);
            appointmentDetails.add(new Phrase("Time: " + time));
            appointmentDetails.add(Chunk.NEWLINE);
            document.add(appointmentDetails);

            // Close the document
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadInvoice() {

        File srcFile = new File(getExternalFilesDir(null), "Appointment("+date2+").pdf");
        File destFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Appointment("+date2+").pdf");
        try {
            copyFile(srcFile, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Launch the PDF viewer app
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", destFile);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);

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
