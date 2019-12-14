package fgl.admin;

import fgl.product.Game;
import fgl.product.GameDAO;
import fgl.userPanel.User;
import fgl.userPanel.UserDAO;

import java.sql.SQLException;
import java.util.List;

public class ModerationPanel {
  // Data
  private static final String title = "Moderation Panel";
  private static final String path = "/ModerationPanel.fxml";
  private UserDAO userDAO;
  private GameDAO gameDAO;
  private List<User> users;
  private List<Game> reportedGames;

  public ModerationPanel() {
    userDAO = new UserDAO();
    gameDAO = new GameDAO();
    refresh();
  }

  public String getTitle() {
    return title;
  }

  public String getPath() {
    return path;
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

  // TODO JUNK move to test folder or sth
  void test() throws InterruptedException {
    System.out.println( "test" );

//    discardReport(reportedGames.get(0));
//    System.out.println( "zgloszenie" );

//    deleteGame(reportedGames.get(0));
//    System.out.println( "usuniecie" );

//    blockUser(users.get(2));
//    System.out.println( "zablokuj" );

//    unblockUser(users.get(2));
//    System.out.println( "odblokuj" );
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
