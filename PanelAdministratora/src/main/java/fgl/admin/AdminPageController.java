package fgl.admin;

import fgl.product.Game;
import fgl.userPanel.User;
import fgl.userPanel.UserType;

import javafx.application.Application;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class AdminPageController extends Application {
  User loggedUser;
  ModerationPanel panel;

  public static void main( String[] args ) {
    launch( args );
  }

  @Override
  public void start( Stage primaryStage ) throws IOException {

    // TODO logged user handler
    loggedUser = new User(4L, "Jaro", "S", "", "", UserType.MODERATOR, false);

    if ( loggedUser.getType() == UserType.ADMINISTRATOR ) {
      panel = new AdministrationPanel();
    } else if ( loggedUser.getType() == UserType.MODERATOR ) {
      panel = new ModerationPanel();
    } else {
      throw new AccessDeniedException( "Users are forbidden to be in Administarion panel!" );
    }

    try {
      panel.test();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    Parent root = FXMLLoader.load( getClass().getResource( panel.getPath() ) );

    primaryStage.setTitle( panel.getTitle() );
    primaryStage.setScene( new Scene( root ) );
    primaryStage.show();
  }

  public void showGameCard( Game game ) {
    // TODO ProductCard integration
    // ProductCard pd = new ProductCard( game );
    // pd.show();
  }

}
