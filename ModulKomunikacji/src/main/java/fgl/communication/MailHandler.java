package fgl.communication;

import fgl.userPanel.User;
import fgl.userPanel.UserType;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//import javax.activation.*;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.nio.file.Paths;
//import java.util.Scanner;
/*String email = "", password = "";
File file = new File(getClass().getClassLoader().getResource("password.txt").getFile());
try {
    Scanner sc = new Scanner(file);
    sc.useDelimiter("\n");
    email = sc.next();
    password = sc.next();

} catch (FileNotFoundException e) {
    e.printStackTrace();
}*/

import java.util.Properties;

public final class MailHandler {
  private static final Long ID = 4L;

  private MailHandler() {
  }

  public static void sendMail( User to, String type ) {
    Properties props = new Properties();
    props.put( "mail.smtp.host", "smtp.gmail.com" );
    props.put( "mail.smtp.socketFactory.port", "465" );
    props.put( "mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory" );
    props.put( "mail.smtp.auth", "true" );
    props.put( "mail.smtp.port", "465" );
    String email = "ftims.game.launcher@gmail.com";
    String password = "W6j8Vm$nx@";
    Session session = Session.getDefaultInstance( props,
            new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication( email, password );
              }
            } );
    try {
      Message message = new MimeMessage( session );
      message.setFrom( new InternetAddress( email ) );
      message.setRecipients( Message.RecipientType.TO,
              InternetAddress.parse( to.getEmail() ) );
      if ( type.compareTo( "block" ) == 0 ) {
        message.setSubject( "Account Blocked" );
        message.setText( "Hello " + to.getUsername() + "," +
                "\n\n Your accont on Ftims Game Luncher has been blocked." +
                "\n For further informations please " +
                "contact us via email adress: " + email +
                "\n\n Sincerly," +
                "\n Ftims Game Luncher Team" );
      } else if ( type.compareTo( "unblock" ) == 0 ) {
        message.setSubject( "Account Unblocked" );
        message.setText( "Hello " + to.getUsername() + "," +
                "\n\n Your accont on Ftims Game Luncher has been unblocked." +
                "\n For further informations please " +
                "contact us via email adress: " + email +
                "\n\n Sincerly," +
                "\n Ftims Game Luncher Team" );
      } else {
        message.setSubject( "Testing Subject" );
        message.setText( "Hello this is not spam," +
                "\n\n This is a JavaMail test...!" );
      }
      Transport.send( message );
    } catch ( MessagingException e ) {
      throw new RuntimeException( e );
    }
  }

  public static void main( String[] args ) {
    User u = new User ( ID, "x", "x", "x", "216894@edu.p.lodz.pl", UserType.USER, false );
    MailHandler.sendMail( u, "empty path" );
  }
}

