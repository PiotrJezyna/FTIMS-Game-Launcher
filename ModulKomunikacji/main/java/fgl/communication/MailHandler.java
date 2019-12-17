package fgl.communication;

import fgl.userPanel.User;
import fgl.userPanel.UserType;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

public class MailHandler {
    private static final Long ID = 4L;

    public static void sendMail(User to, String pathToTemplate) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        String email = "", password = "";
                        File file = new File(getClass().getClassLoader().getResource("password.txt").getFile());
                        try {
                            Scanner sc = new Scanner(file);
                            sc.useDelimiter("\n");
                            email = sc.next();
                            password = sc.next();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        return new PasswordAuthentication(email,password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ftims.game.launcher@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to.getEmail()));
            message.setSubject("Testing Subject");
            message.setText("Hello this is not spam," +
                    "\n\n This is a JavaMail test...!");

            Transport.send(message);

            System.out.println("Done");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        MailHandler.sendMail(new User (ID, "Jaro", "S", "sucharek69", "jedrak32@gmail.com", UserType.ADMINISTRATOR, false ), "empty path");
    }
}

