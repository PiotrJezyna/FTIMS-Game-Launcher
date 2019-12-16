import javafx.scene.paint.Stop;
import org.apache.commons.lang3.time.StopWatch;

import java.io.IOException;
import java.util.Date;
import java.sql.*;
import java.util.concurrent.TimeUnit;

public class Statistics implements LibraryObserver{
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://remotemysql.com/5VexXpVWzU";
    static final String USER = "5VexXpVWzU";
    static final String PASS = "apQqybLdoW";
    private int statisticsID;
    private int gameID;                                        //tutaj ID z klasy Game
    private double gameTime = 0;
    private Date installationDate;
    private Date recentlyPlayed;
    private StopWatch stopWatch = new StopWatch();

    public Statistics(int gameID){
        this.gameID = gameID;
    }

    public Statistics(int gameID, double gameTime){
        this.gameID = gameID;
        this.gameTime = gameTime;
    }

    public double getGameTime() {
        return gameTime;
    }

    public void setGameTime(double gameTime) {
        this.gameTime += gameTime;
    }

    public Date getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(Date installationDate) {
        this.installationDate = installationDate;
    }

    public Date getRecentlyPlayed() {
        return recentlyPlayed;
    }

    public void setRecentlyPlayed(Date recentlyPlayed) {
        this.recentlyPlayed = recentlyPlayed;
    }

    public void showAllStatistics(){
        String allStatistics = gameTime + installationDate.toString() + recentlyPlayed.toString();
    }

    public void onGameStart(){
        stopWatch.start();
    }

    public void onGameClosed(int userID) throws ClassNotFoundException {
        stopWatch.stop();
        setGameTime(((double)stopWatch.getTime()) / 60000.0);
        Class.forName(JDBC_DRIVER);
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Users_Games SET PlayTime = ? WHERE UserID = ? AND GameID = ?")){

            statement.setDouble(1, gameTime);
            statement.setInt(2, userID);
            statement.setInt(3, gameID);
            statement.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(((double)stopWatch.getTime()) / 60000.0);
    }
}
