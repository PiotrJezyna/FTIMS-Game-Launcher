import fgl.product.Game;
import javafx.scene.paint.Stop;
import org.apache.commons.lang3.time.StopWatch;

import java.io.IOException;
import java.sql.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static java.time.LocalDateTime.*;

public class Statistics implements LibraryObserver{
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://remotemysql.com/5VexXpVWzU";
    static final String USER = "5VexXpVWzU";
    static final String PASS = "apQqybLdoW";
    private int statisticsID;
    private Long gameID;
    private double gameTime = 0;
    private Date firstInstallationDate;
    private Date lastInstallationDate;
    private Long longestSession;
    private int sessionCount;
    private Date recentlyPlayed;
    private StopWatch stopWatch = new StopWatch();

    public Statistics(Long gameID){
        this.gameID = gameID;
    }

    public Statistics(Long gameID, double gameTime){
        this.gameID = gameID;
        this.gameTime = gameTime;
    }

    public double getGameTime() {
        return gameTime;
    }

    public void setGameTime(double gameTime) {
        this.gameTime += gameTime;
    }

    public Date getLastInstallationDate() {
        return lastInstallationDate;
    }

    public void setLastInstallationDate(Date lastInstallationDate) {
        this.lastInstallationDate = lastInstallationDate;
    }

    public Date getFirstInstallationDate() {
        return lastInstallationDate;
    }

    public void setFirstInstallationDate(Date firstInstallationDate) {
        this.firstInstallationDate = firstInstallationDate;
    }

    public Date getRecentlyPlayed() {
        return recentlyPlayed;
    }

    public Long getLongestSession() {
        return longestSession;
    }

    public void setLongestSession(Long longestSession) {
        this.longestSession = longestSession;
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }

    public void setRecentlyPlayed(Date recentlyPlayed) {
        this.recentlyPlayed = recentlyPlayed;
    }

    public void onGameStart(Long userID, Long gameID) throws ClassNotFoundException {
        stopWatch.start();
        Class.forName(JDBC_DRIVER);

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Users_Games SET SessionCount = SessionCount + 1 WHERE UserID = ? AND GameID = ?")){
            statement.setLong(1, userID);
            statement.setLong(2, gameID);
            System.out.println(statement.toString());
            statement.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onGameClosed(Long userID,Long gameID) throws ClassNotFoundException {
        stopWatch.stop();
        setGameTime(((double)stopWatch.getTime()) / 60000.0);
        Class.forName(JDBC_DRIVER);
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Users_Games SET PlayTime = ? WHERE UserID = ? AND GameID = ?")){
            statement.setDouble(1, gameTime);
            statement.setLong(2, userID);
            statement.setLong(3, gameID);

            System.out.println(statement.toString());
            statement.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT LongestSession FROM Users_Games WHERE UserID = ? AND GameID = ?")){
            statement.setLong(1, userID);
            statement.setLong(2, gameID);
            System.out.println(statement.toString());
            final ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                if(resultSet.getDouble("LongestSession") < stopWatch.getTime()/60000.0){
                    PreparedStatement preparedStatement = connection.prepareStatement(
                            "UPDATE Users_Games SET LongestSession=? WHERE UserID=? AND GameID=?");
                    preparedStatement.setDouble(1,stopWatch.getTime()/60000.0);
                    preparedStatement.setLong(2, userID);
                    preparedStatement.setLong(3, gameID);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopWatch.reset();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Users_Games SET LastSession = ? WHERE UserID = ? AND GameID = ?")){
            java.util.Date tempDate = new java.util.Date();
            Date date = new Date(tempDate.getTime());
            statement.setDate(1, date);
            statement.setLong(2, userID);
            statement.setLong(3, gameID);
            statement.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
