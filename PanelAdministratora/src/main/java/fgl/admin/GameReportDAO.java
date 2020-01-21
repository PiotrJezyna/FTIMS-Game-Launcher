package fgl.admin;

import fgl.database.AbstractDao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameReportDAO extends AbstractDao<GameReport> {
  @Override
  protected GameReport get( Long id ) throws SQLException {
    connectSQL();

    try {

      String query = "SELECT UserID, GameID, Explanation, Status " +
              "FROM ReportedGames WHERE GameID = %s";
      query = String.format( query, id );

      System.out.println( query );

      stmt = conn.createStatement();
      rs = stmt.executeQuery( query );
      rs.next();

      long userId = rs.getLong( "UserID" );
      long gameID = rs.getLong( "GameID" );
      String explanation = rs.getString( "Explanation" );
      boolean status = rs.getBoolean( "Status" );

      return new GameReport( userId, gameID, explanation, status );

    } catch ( SQLException e ) {

      throw new SQLException( e.getMessage() );

    } finally {

      rs.close();
      stmt.close();
      disconnectSQL();

    }
  }

  @Override
  protected List<GameReport> getAll() throws SQLException {
    connectSQL();

    try {

      String query = "SELECT UserID, GameID, Explanation, Status FROM ReportedGames";
      stmt = conn.createStatement();
      rs = stmt.executeQuery( query );

      List<GameReport> reports = new ArrayList<GameReport>();

      while ( rs.next() ) {

        long userId = rs.getLong( "UserID" );
        long gameID = rs.getLong( "GameID" );
        String explanation = rs.getString( "Explanation" );
        boolean status = rs.getBoolean( "Status" );

        GameReport gameReport = new GameReport( userId, gameID, explanation, status );
        reports.add( gameReport );
      }

      return reports;

    } catch ( SQLException e ) {

      throw new SQLException( e.getMessage() );

    } finally {

      rs.close();
      stmt.close();
      disconnectSQL();

    }
  }

  protected List<GameReport> getAllFor( Long id ) throws SQLException {
    connectSQL();

    try {

      String query = "SELECT UserID, GameID, Explanation, Status FROM ReportedGames " +
              "WHERE GameID = " + id + " AND Status = 0";
      stmt = conn.createStatement();
      rs = stmt.executeQuery( query );

      List<GameReport> reports = new ArrayList<GameReport>();

      while ( rs.next() ) {

        long userId = rs.getLong( "UserID" );
        long gameID = rs.getLong( "GameID" );
        String explanation = rs.getString( "Explanation" );
        boolean status = rs.getBoolean( "Status" );

        GameReport gameReport = new GameReport( userId, gameID, explanation, status );
        reports.add( gameReport );
      }

      return reports;

    } catch ( SQLException e ) {

      throw new SQLException( e.getMessage() );

    } finally {

      rs.close();
      stmt.close();
      disconnectSQL();

    }
  }

  @Override
  protected void insert( GameReport gameReport ) throws SQLException {
    connectSQL();

    String query = "INSERT INTO ReportedGames(UserID, GameID, Explanation, Status)" +
            " VALUES (%s, %s, '%s', %s)";
    query = String.format( query, gameReport.getUserID(), gameReport.getGameID(),
            gameReport.getExplanation(), gameReport.getStatus() );

    System.out.println( query );

    try {

      stmt = conn.createStatement();
      stmt.executeUpdate( query );

    } catch ( SQLException e ) {

      throw new SQLException( e.getMessage() );

    } finally {

      disconnectSQL();

    }
  }

  @Override
  protected void update( GameReport gameReport ) throws SQLException {
    connectSQL();

    String query =
            "UPDATE ReportedGames " +
                    "SET " +
                    "UserID = %s, " +
                    "GameID = %s, " +
                    "Explanation = '%s', " +
                    "Status = %s " +
                    "WHERE UserID = " + gameReport.getUserID() +
                    " AND GameID = " + gameReport.getGameID();

    query = String.format( query, gameReport.getUserID(), gameReport.getGameID(),
            gameReport.getExplanation(), gameReport.getStatus() );
    query = query.replace( "false", "0" );
    query = query.replace( "true", "1" );

    System.out.println( query );

    try {

      stmt = conn.createStatement();
      stmt.executeUpdate( query );

    } catch ( SQLException e ) {

      throw new SQLException( e.getMessage() );

    } finally {

      disconnectSQL();

    }
  }

  @Override
  protected void delete( GameReport gameReport ) throws SQLException {
    connectSQL();

    String query = "DELETE FROM ReportedGames " +
            "WHERE UserID = " + gameReport.getUserID() +
            "AND GameID = " + gameReport.getGameID();

    System.out.println( query );

    try {

      stmt = conn.createStatement();
      stmt.executeUpdate( query );

    } catch ( SQLException e ) {

      throw new SQLException( e.getMessage() );

    } finally {

      disconnectSQL();

    }
  }
}
