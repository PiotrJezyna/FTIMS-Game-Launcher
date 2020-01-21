package fgl.admin;

import fgl.product.Game;
import fgl.product.GameDAO;
import fgl.userPanel.User;
import fgl.userPanel.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModerationPanel {

  protected static UserDAO userDAO = new UserDAO();
  protected static GameDAO gameDAO = new GameDAO();
  private static final String TITLE = "Moderation Panel";
  private static final String PATH = "/ModerationPanel.fxml";
  protected List<User> users;
  protected List<Game> reportedGames;
  @FXML
  protected ListView<UserBox> usersListView;
  @FXML
  protected ListView<ReportedGameBox> reportedGamesListView;

  public ModerationPanel() {
  }

  @FXML
  void initialize() {
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
   * @return if loading has succeed
   */
  public boolean loadAllUsersFromDB() {
    try {
      users = userDAO.getAll();
    } catch ( SQLException e ) {
      e.printStackTrace();
      return false;
    }

    List<UserBox> list = new ArrayList<>();

    for ( User user : users ) {
      list.add( new UserBox( user, false ) );
    }

    ObservableList<UserBox> myObservableList = FXCollections.observableList( list );
    usersListView.setItems( myObservableList );

    return true;
  }

  /**
   * Load information about reported games from DB into list in this class
   *
   * @return if loading has succeed
   */
  public boolean loadReportedGamesFromDB() {
    try {
      reportedGames = new ArrayList<>();

      List<Game> allGames = gameDAO.getAll();
      reportedGames = new ArrayList<>();
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

    List<ReportedGameBox> list = new ArrayList<>();

    for ( Game game : reportedGames ) {
      list.add( new ReportedGameBox( game ) );
    }

    ObservableList<ReportedGameBox> myObservableList =
            FXCollections.observableList( list );
    reportedGamesListView.setItems( myObservableList );

    return true;
  }

  public static boolean discardReport( Game game ) {
    try {
      game.setReported( false );
      gameDAO.update( game );
    } catch ( SQLException e ) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public static boolean deleteGame( Game game ) {
    try {
      gameDAO.delete( game );
    } catch ( SQLException e ) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public static boolean blockUser( User user ) {
    try {
      user.setBlocked( true );
      userDAO.update( user );
    } catch ( SQLException e ) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public static boolean unblockUser( User user ) {
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
