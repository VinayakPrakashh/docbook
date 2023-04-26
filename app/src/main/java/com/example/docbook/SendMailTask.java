package com.example.docbook;

import android.os.AsyncTask;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTask extends AsyncTask<Void, Void, Void> {
    private String email;
    private String subject;
    private String message;
    private String fromEmail;
    private String fromPassword;

    public SendMailTask(String email, String subject, String message, String fromEmail, String fromPassword) {
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.starttls.enable", "true");

            Authenticator auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {

                    return new PasswordAuthentication(fromEmail, fromPassword);
                }
            };

            Session session = Session.getInstance(props, auth);

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            msg.setSubject(subject);
            msg.setText(message);

            Transport.send(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
