import org.apache.commons.lang3.text.StrSubstitutor;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {

    private static String USER_NAME = "sandhyc@uci.edu";
    private static String PASSWORD = "san@uci123";
    private static String RECIPIENT = "sandhya2392@gmail.com";
    private static String NAME = "Sandhya Chandramohan";

    public static void main(String[] args) {
        String from = USER_NAME;
        String pass = PASSWORD;
        String to = RECIPIENT;
        String name = NAME;
        String subject = "Java send mail example";

        StringBuilder bldr = new StringBuilder();
        String str;
        try {
            BufferedReader in = new BufferedReader(new FileReader("email-template.html"));
            while((str = in.readLine())!=null)
                bldr.append(str);
            in.close();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }

        Map<String, String> h = null;
        try {
             h = GSheet.readData();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        String body = bldr.toString();
        StrSubstitutor sub = new StrSubstitutor(h);
        body = sub.replace(body);

        sendFromGMail(from, pass, to, subject, body, name);


    }

    private static void sendFromGMail(String from, String pass, String to, String subject, String body, String name) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", true);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USER_NAME, PASSWORD);
                    }
                });

        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from, name));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);


            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body, "utf-8", "html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            String file = "sandhya_resume.pdf";
            String fileName = "sandhya_resume";
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);

            Transport transport = session.getTransport("smtps");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception ae) {
            ae.printStackTrace();
        }
    }

}