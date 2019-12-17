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

    public static void sendMail(User to, String type) {
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
                        String email = "ftims.game.launcher@gmail.com", password = "W6j8Vm$nx@";
                        /*File file = new File(getClass().getClassLoader().getResource("password.txt").getFile());
                        try {
                            Scanner sc = new Scanner(file);
                            sc.useDelimiter("\n");
                            email = sc.next();
                            password = sc.next();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }*/

                        return new PasswordAuthentication(email,password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ftims.game.launcher@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to.getEmail()));

            if(type.compareTo("block") == 0)
            {
                message.setSubject( "Account Blocked" );
                message.setText( "Hello " + to.getUsername() + "," +
                        "\n\n Your accont on Ftims Game Luncher has been blocked." +
                        "\n For further informations please contact us via email adress: ftims.game.launcher@gmail.com" +
                        "\n\n Sincerly," +
                        "\n Ftims Game Luncher Team" );
            }
            else if(type.compareTo("unblock") == 0)
            {
                message.setSubject( "Account Unblocked" );
                message.setText( "Hello " + to.getUsername() + "," +
                        "\n\n Your accont on Ftims Game Luncher has been unblocked." +
                        "\n For further informations please contact us via email adress: ftims.game.launcher@gmail.com" +
                        "\n\n Sincerly," +
                        "\n Ftims Game Luncher Team" );
            }
            else
            {
                message.setSubject("Testing Subject");
                message.setText("Hello this is not spam," +
                        "\n\n This is a JavaMail test...!");
            }

            Transport.send(message);

            System.out.println("Done");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        MailHandler.sendMail(new User (ID, "Bożena", "Borowska", "bb", "bozena.borowska@p.lodz.pl", UserType.USER, false ), "empty path");
    }
}

