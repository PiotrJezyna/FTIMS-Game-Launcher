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
  protected static GameReportDAO reportDAO = new GameReportDAO();
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

  public static UserDAO getUserDAO() {
    return userDAO;
  }

  public static GameDAO getGameDAO() {
    return gameDAO;
  }

  public void refresh() {
    if ( !loadAllUsersFromDB() ) {
      System.out.println( "Users data was not loaded." );
    }
    if ( !loadReportedGamesFromDB() ) {
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

    makeReportedGamesListView();

    return true;
  }

  private void makeReportedGamesListView() {
    List<ReportedGameBox> list = new ArrayList<>();
    reportedGamesListView.setItems( null );

    for ( Game game : reportedGames ) {
      list.add( new ReportedGameBox( game, this ) );
    }

    ObservableList<ReportedGameBox> myObservableList =
            FXCollections.observableList( list );
    reportedGamesListView.setItems( myObservableList );
  }

  public boolean discardReport( Game game, List<GameReport> reports ) {
    try {
      game.setReported( false );
      gameDAO.update( game );

      for ( GameReport gr : reports ) {
        gr.setStatus( true );
        reportDAO.update( gr );
      }

      reportedGames.remove( game );
      makeReportedGamesListView();
    } catch ( SQLException e ) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean deleteGame( Game game, List<GameReport> reports ) {
    try {
      game.setDeleted( true );
      gameDAO.update( game );

      for ( GameReport gr : reports ) {
        gr.setStatus( true );
        reportDAO.update( gr );
      }

      reportedGames.remove( game );
      makeReportedGamesListView();
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
