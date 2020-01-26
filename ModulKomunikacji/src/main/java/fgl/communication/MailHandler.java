package fgl.communication;

import fgl.product.Game;
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

  public static void sendMailWithCode( String to, String email, String type, String confirmCode ) {
    Session session = makeSession();
    try {
      Message message = new MimeMessage( session );
      message.setFrom( new InternetAddress( EMAIL ) );
      message.setRecipients( Message.RecipientType.TO,
              InternetAddress.parse( email ) );
      if ( type.compareTo( "registration" ) == 0 ) {
        message.setSubject( "Rejestracja" );
        message.setText( registrationText( to, confirmCode ) );
      } else if ( type.compareTo( "reminder" ) == 0 ) {
        message.setSubject( "Przypomnienie hasła" );
        message.setText( reminderText( to, confirmCode ) );
      } else {
        throw new MessagingException( "Message type not found" );
      }
      Transport.send( message );
    } catch ( MessagingException e ) {
      throw new RuntimeException( e );
    }
  }

  public static void sendMailWithGame( String to, String email, String type, Game game ) {
    Session session = makeSession();
    try {
      Message message = new MimeMessage( session );
      message.setFrom( new InternetAddress( EMAIL ) );
      message.setRecipients( Message.RecipientType.TO,
              InternetAddress.parse( email ) );
      if ( type.compareTo( "game_delete" ) == 0 ) {
        message.setSubject( "Twoje gra została usunięta" );
        message.setText( deleteGameText( to, game ) );
      } else {
        throw new MessagingException( "Message type not found" );
      }
      Transport.send( message );
      System.out.println( message );
    } catch ( MessagingException e ) {
      e.printStackTrace();
    }
  }

  public static void sendMail( String to, String email, String type ) {
    Session session = makeSession();
    try {
      Message message = new MimeMessage( session );
      message.setFrom( new InternetAddress( EMAIL ) );
      message.setRecipients( Message.RecipientType.TO,
              InternetAddress.parse( email ) );
      if ( type.compareTo( "block" ) == 0 ) {
        message.setSubject( "Twoje konto zostało zablokowane" );
        message.setText( blockText( to ) );
      } else if ( type.compareTo( "unblock" ) == 0 ) {
        message.setSubject( "Twoje konto zostało odblokowane" );
        message.setText( unblockText( to ) );
      } else {
        throw new MessagingException( "Message type not found" );
      }
      Transport.send( message );
      System.out.println( message );
    } catch ( MessagingException e ) {
      e.printStackTrace();
    }
  }

  private static String blockText( String to ) {
    return "Witaj " + to + "," +
            "\n\n Twoje konto na Ftims Game Luncher zostało zablokowane." +
            FOOTER;
  }

  private static String unblockText( String to ) {
    return "Witaj " + to + "," +
            "\n\n Twoje konto na Ftims Game Luncher zostało odblokowane." +
            FOOTER;
  }

  private static String registrationText( String to, String confirmCode ) {
    return "Witaj " + to + "," +
            "\n\n Aby dokończyć proces rejestracji prosimy " +
            "wpisać ten kod potwierdzający: " + confirmCode +
            FOOTER;
  }

  private static String reminderText( String to, String reminderCode ) {
    return "Witaj " + to + "," +
            "\n\n Aby przypomnieć hasło prosimy " +
            "wpisać ten kod potwierdzający: " + reminderCode +
            FOOTER;
  }

  private static String deleteGameText(String to, Game game) {
    return "Witaj " + to + "," +
            "\n\n Twoja gra " + game.getTitle() +
            " została usunięta z naszej biblioteki gier." +
            FOOTER;
  }
}

