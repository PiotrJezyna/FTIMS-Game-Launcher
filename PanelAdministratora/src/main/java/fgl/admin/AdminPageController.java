package fgl.admin;

import fgl.userPanel.User;
import fgl.userPanel.UserSession;
import fgl.userPanel.UserType;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class AdminPageController {

  private UserSession userSession;
  private ModerationPanel panel;

  public void start( AnchorPane stage, UserSession user ) throws IOException {
    userSession = user;

    if ( userSession.getCurrentUser().getType() == UserType.ADMINISTRATOR ) {
      panel = new AdministrationPanel();
    } else if ( userSession.getCurrentUser().getType() == UserType.MODERATOR ) {
      panel = new ModerationPanel();
    } else {
      throw new AccessDeniedException( "Users are forbidden to be here!" );
    }

    AnchorPane test = FXMLLoader.load( getClass().getResource( panel.getPath() ) );
    stage.getChildren().clear();
    stage.getChildren().add( test );
  }

  public User getUserSession() {
    return userSession.getCurrentUser();
  }

  public void setUserSession( UserSession userSession ) {
    this.userSession = userSession;
  }

  public ModerationPanel getPanel() {
    return panel;
  }

  public void setPanel( ModerationPanel panel ) {
    this.panel = panel;
  }

}
