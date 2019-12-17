package fgl.admin;

import fgl.communication.MailHandler;
import fgl.product.Game;
import fgl.product.GameDAO;
import fgl.userPanel.User;
import fgl.userPanel.UserDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModerationPanel {

  public class UserBox extends HBox {
    User user;
    Label label = new Label();
    Button button = new Button();

    UserBox( User user ) {
      super();

      this.user = user;
      label.setText( user.getUsername() );
      label.setMaxWidth(Double.MAX_VALUE);
      HBox.setHgrow(label, Priority.ALWAYS);

      if ( user.isBlocked() ) {
        button = new Button( "Unblock user" );
      } else {
        button = new Button( "Block user" );
      }

      button.setOnAction( new EventHandler<ActionEvent>() {
        @Override
        public void handle( ActionEvent event ) {
          if ( user.isBlocked() ) {
            unblockUser( user );
            button.setText( "Block user" );
            MailHandler.sendMail( user, "unblock" );
          } else {
            blockUser( user );
            button.setText( "Unblock user" );
            MailHandler.sendMail( user, "block" );
          }
        }
      });

      this.getChildren().addAll(label, button);
    }
  }

  // Data
  private static final String TITLE = "Moderation Panel";
  private static final String PATH = "/ModerationPanel.fxml";
  private UserDAO userDAO;
  private GameDAO gameDAO;
  private List<User> users;
  private List<Game> reportedGames;

  @FXML
  private ListView<UserBox> usersListView;

  public ModerationPanel() {
    userDAO = new UserDAO();
    gameDAO = new GameDAO();
  }

  @FXML
  void initialize(){
    refresh();
  }

  public String getTitle() {
    return TITLE;
  }

  public String getPath() {
    return PATH;
  }

  protected UserDAO getUserDAO() {
    return userDAO;
  }

  protected GameDAO getGameDAO() {
    return gameDAO;
  }

  public void refresh() {
    if ( !loadAllUsersFromDB() ) {
      // TODO show error window
      System.out.println( "Users data was not loaded." );
    }
    if ( !loadReportedGamesFromDB() ) {
      // TODO show error window
      System.out.println( "Reported games data was not loaded." );
    }
  }

  /**
   * Load information about all users from DB into list in this class
   *
   * @return if loading has succeed or not
   */
  public boolean loadAllUsersFromDB() {
    try {
      users = userDAO.getAll();
    } catch ( SQLException e ) {
      e.printStackTrace();
      return false;
    }

    List<UserBox> list = new ArrayList<>();

    for (User user: users) {
      list.add(new UserBox( user ));
    }

    ObservableList<UserBox> myObservableList = FXCollections.observableList( list );
    usersListView.setItems( myObservableList );

    return true;
  }

  /**
   * Load information about reported games from DB into list in this class
   *
   * @return if loading has succeed or not
   */
  public boolean loadReportedGamesFromDB() {
    try {
      List<Game> allGames = gameDAO.getAll();
      for ( Game g : allGames ) {
        if ( g.isReported() ) {
          reportedGames.add( g );
        }
      }
      allGames.clear();
    } catch ( SQLException e ) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   *
   * @param game
   * @return
   */
  public boolean discardReport( Game game ) {
    try {
      game.setReported( false );
      gameDAO.update( game );
    } catch ( SQLException e ) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   *
   * @param game
   * @return
   */
  public boolean deleteGame( Game game ) {
    try {
      gameDAO.delete( game );
    } catch ( SQLException e ) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   *
   * @param user
   * @return
   */
  public boolean blockUser( User user ) {
    try {
      user.setBlocked( true );
      userDAO.update( user );
    } catch ( SQLException e ) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   *
   * @param user
   * @return
   */
  public boolean unblockUser( User user ) {
    try {
      user.setBlocked( false );
      userDAO.update( user );
    } catch ( SQLException e ) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

}
