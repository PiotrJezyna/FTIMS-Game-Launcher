package fgl.admin;

import fgl.product.Game;
import fgl.userPanel.User;
import fgl.userPanel.UserType;

import javafx.application.Application;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class AdminPageController {

  // TODO get rid of this ID
  public static final long ID = 4L;
  private User loggedUser;
  private ModerationPanel panel;

  @FXML private ListView<User> usersListView;
  @FXML private ListView<Game> reportedGamesListView;
  @FXML private MenuItem refreshMenuItem;

  /*public static void main( String[] args ) {
    launch( args );
  }*/

  //@Override
  public void start( AnchorPane stage ) throws IOException {

    // TODO logged user handler
    loggedUser = new User(
            ID, "Jaro", "S", "", "", UserType.MODERATOR, false );

    if ( loggedUser.getType() == UserType.ADMINISTRATOR ) {
      panel = new AdministrationPanel();
    } else if ( loggedUser.getType() == UserType.MODERATOR ) {
      panel = new ModerationPanel();
    } else {
      throw new AccessDeniedException( "Users are forbidden to be here!" );
    }

    try {
      panel.test();
    } catch ( InterruptedException e ) {
      e.printStackTrace();
    }

    AnchorPane test = FXMLLoader.load( getClass().getResource( panel.getPath() ) );
    stage.getChildren().clear();
    stage.getChildren().add(test);
  }

  public User getLoggedUser() {
    return loggedUser;
  }

  public void setLoggedUser( User loggedUser ) {
    this.loggedUser = loggedUser;
  }

  public ModerationPanel getPanel() {
    return panel;
  }

  public void setPanel( ModerationPanel panel ) {
    this.panel = panel;
  }

  public void showGameCard( Game game ) {
    // TODO ProductCard integration
    // ProductCard pd = new ProductCard( game );
    // pd.show();
  }

}
