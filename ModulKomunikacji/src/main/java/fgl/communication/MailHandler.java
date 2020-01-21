package fgl.communication;

import fgl.userPanel.User;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public final class MailHandler {
  private static final String EMAIL = "ftims.game.launcher@gmail.com";
  private static final String PASSWORD = "W6j8Vm$nx@";
  private static final String FOOTER =
          "\n Po więcej informacji prosimy " +
          "kontaktować się z nami poprzez adres email: " + EMAIL +
          "\n\n Łączymy wyrazy szacunku," +
          "\n Ftims Game Luncher Team";

  private MailHandler() {
  }

  public static Session makeSession() {
    Properties props = new Properties();
    props.put( "mail.smtp.host", "smtp.gmail.com" );
    props.put( "mail.smtp.socketFactory.port", "465" );
    props.put( "mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory" );
    props.put( "mail.smtp.auth", "true" );
    props.put( "mail.smtp.port", "465" );
    return Session.getDefaultInstance( props,
            new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication( EMAIL, PASSWORD );
              }
            } );
  }

  public static void sendMailWithCode( User to, String type, String confirmCode ) {
    Session session = makeSession();
    try {
      Message message = new MimeMessage( session );
      message.setFrom( new InternetAddress( EMAIL ) );
      message.setRecipients( Message.RecipientType.TO,
              InternetAddress.parse( to.getEmail() ) );

      if ( type.compareTo( "registration" ) == 0 ) {
        message.setSubject( "Rejestracja" );
        message.setText( registrationText( to, confirmCode ) );
      } else if ( type.compareTo( "reminder" ) == 0 ) {
        message.setSubject( "Przypomnienie hasła" );
        message.setText( reminderText( to, confirmCode ) );
      }

      Transport.send( message );
    } catch ( MessagingException e ) {
      throw new RuntimeException( e );
    }
  }

  public static void sendMail( User to, String type ) {
    Session session = makeSession();
    try {
      Message message = new MimeMessage( session );
      message.setFrom( new InternetAddress( EMAIL ) );
      message.setRecipients( Message.RecipientType.TO,
              InternetAddress.parse( to.getEmail() ) );
      if ( type.compareTo( "block" ) == 0 ) {
        message.setSubject( "Account Blocked" );
        message.setText( blockText( to ) );
      } else if ( type.compareTo( "unblock" ) == 0 ) {
        message.setSubject( "Account Unblocked" );
        message.setText( unblockText( to ) );
      } else {
        message.setSubject( "Testing Subject" );
        message.setText( "Hello this is not spam," +
                "\n\n This is a JavaMail test...!" );
      }
      Transport.send( message );
      System.out.println( message );
    } catch ( MessagingException e ) {
      e.printStackTrace();
//      throw new RuntimeException( e );
    }
  }

  public static String blockText( User to ) {
    return "Witaj " + to.getUsername() + "," +
            "\n\n Your accont on Ftims Game Luncher has been blocked." +
            FOOTER;
  }

  public static String unblockText( User to ) {
    return "Witaj " + to.getUsername() + "," +
            "\n\n Your accont on Ftims Game Luncher has been unblocked." +
            FOOTER;
  }

  public static String registrationText( User to, String confirmCode ) {
    return "Witaj " + to.getUsername() + "," +
            "\n\n Aby dokończyć proces rejestracji prosimy " +
            "wpisać ten kod potwierdzający: " + confirmCode +
            FOOTER;
  }

  public static String reminderText( User to, String reminderCode ) {
    return "Witaj " + to.getUsername() + "," +
            "\n\n Aby przypomnieć hasło prosimy " +
            "wpisać ten kod potwierdzający: " + reminderCode +
            FOOTER;
  }

}

