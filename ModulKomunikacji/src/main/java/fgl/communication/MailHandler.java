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
          "\n For further informations please " +
          "contact us via email adress: " + EMAIL +
          "\n\n Sincerly," +
          "\n Ftims Game Luncher Team";

  private MailHandler() {
  }

  public static void sendMail( User to, String type ) {
    Properties props = new Properties();
    props.put( "mail.smtp.host", "smtp.gmail.com" );
    props.put( "mail.smtp.socketFactory.port", "465" );
    props.put( "mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory" );
    props.put( "mail.smtp.auth", "true" );
    props.put( "mail.smtp.port", "465" );
    Session session = Session.getDefaultInstance( props,
            new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication( EMAIL, PASSWORD );
              }
            } );
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
    } catch ( MessagingException e ) {
      throw new RuntimeException( e );
    }
  }

  public static String blockText( User to ) {
    return "Hello " + to.getUsername() + "," +
            "\n\n Your accont on Ftims Game Luncher has been blocked." +
            FOOTER;
  }

  public static String unblockText( User to ) {
    return "Hello " + to.getUsername() + "," +
            "\n\n Your accont on Ftims Game Luncher has been unblocked." +
            FOOTER;
  }

}

