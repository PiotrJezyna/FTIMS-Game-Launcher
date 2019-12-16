import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class LocalLibrary {
    //Database
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://remotemysql.com/5VexXpVWzU";
    static final String USER = "5VexXpVWzU";
    static final String PASS = "apQqybLdoW";

    private int libraryID;
    private int userID;                                             //tu ID z klasy User
    private List<Statistics> gameList;
    //private ProductManager productManager;
    private boolean isPlaying;
    private List<GameDownloader> downloaderList;
    private boolean isDownloading;

    public LocalLibrary(int userID) throws ClassNotFoundException {
        this.userID = userID;
        gameList = new ArrayList<Statistics>();
        downloaderList = new ArrayList<GameDownloader>();
        Class.forName(JDBC_DRIVER);
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM Users_Games WHERE UserID = ?")) {
            preparedStatement.setInt(1, userID);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Statistics statistics = new Statistics(resultSet.getInt(2), resultSet.getInt(3));
                gameList.add(statistics);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public int getLibraryID() {
        return this.libraryID;
    }

    public int getUserID(){
        return userID;
    }

    public void play(int gameID){
        Runtime runtime = Runtime.getRuntime();
        try{
            gameList.get(0).onGameStart();
            isPlaying = true;
            Process process = runtime.exec("C:\\GOG Games\\Return of the Obra Dinn\\ObraDinn.exe", null, new File("C:\\GOG Games\\Return of the Obra Dinn\\"));
            process.waitFor();
            gameList.get(0).onGameClosed(userID);
            isPlaying = false;
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void installGame(int gameID) throws ClassNotFoundException{
        GameDownloader gameDownloader = new GameDownloader();
        downloaderList.add(gameDownloader);
        Class.forName(JDBC_DRIVER);
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT PlayTime FROM Users_Games WHERE UserID = ? AND GameID = ?")){
            statement.setInt(1, this.userID);
            statement.setInt(2, gameID);
            final ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()){
                Statistics statistics = new Statistics(gameID);
                gameList.add(statistics);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT Users_Games VALUES (?,?,?)");
                preparedStatement.setInt(1, userID);
                preparedStatement.setInt(2, gameID);
                preparedStatement.setDouble(3, 0);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Statistics statistics = new Statistics();
    }
}
