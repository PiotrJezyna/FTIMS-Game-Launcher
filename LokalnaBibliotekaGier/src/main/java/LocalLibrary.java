import fgl.product.Game;
import fgl.product.GameManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;

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
    private List<Game> localGames;
    private List<Statistics> gameList;
    //private ProductManager productManager;
    private boolean isPlaying;
    private List<GameDownloader> downloaderList;
    private boolean isDownloading;

    @FXML AnchorPane root;
    @FXML ScrollPane scrollPane;
    @FXML VBox localGamesBox;

    public void initialize() throws SQLException {
        scrollPane.setContent(localGamesBox);
        LocalGamesDAO dao = new LocalGamesDAO();
        localGames = dao.getAll();

        for (Game game: localGames) {

            HBox gameBox = new HBox();

            Label label = new Label(game.getTitle());
            Button buttonOpenCard = new Button("Open Card");
            Button buttonLaunch = new Button("Launch");

            buttonOpenCard.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductCard.fxml"));
                        AnchorPane loadedFxml = loader.load();

                        root.getChildren().clear();
                        root.getChildren().add(loadedFxml);

                        GameManager gm = loader.getController();
                        gm.ShowProductCard(game);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            buttonLaunch.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        installGame(game);
                        play(game);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

            gameBox.getChildren().add(buttonOpenCard);
            gameBox.getChildren().add(buttonLaunch);
            gameBox.getChildren().add(label);

            HBox.setMargin(buttonOpenCard, new Insets(0, 10, 0, 10));
            HBox.setMargin(buttonLaunch, new Insets(0, 10, 0, 10));
            HBox.setMargin(label, new Insets(0, 10, 0, 10));
            VBox.setMargin(gameBox, new Insets(10, 0, 5, 0));

            localGamesBox.getChildren().add(gameBox);
        }
    }

    public LocalLibrary() throws ClassNotFoundException {
        this.userID = 1;
        gameList = new ArrayList<Statistics>();
        downloaderList = new ArrayList<GameDownloader>();
        Class.forName(JDBC_DRIVER);
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM Users_Games WHERE UserID = ?")) {
            preparedStatement.setInt(1, this.userID);
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

    public void play(Game game){
        Runtime runtime = Runtime.getRuntime();
        try{
            gameList.get(localGames.indexOf(game)).onGameStart();
            isPlaying = true;

            String folder = "C:\\FtimsGameLauncher\\" + game.getTitle() + "\\";
            String executable = game.getTitle() + ".exe";

            Process process = runtime.exec(folder + executable, null, new File(folder));
            process.waitFor();
            gameList.get(localGames.indexOf(game)).onGameClosed(userID, game.getId().intValue());
            isPlaying = false;
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void installGame(Game game) throws ClassNotFoundException{
        GameDownloader gameDownloader = new GameDownloader();
        downloaderList.add(gameDownloader);
        Class.forName(JDBC_DRIVER);
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT PlayTime FROM Users_Games WHERE UserID = ? AND GameID = ?")){
            statement.setInt(1, this.userID);
            statement.setInt(2, game.getId().intValue());
            final ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()){
                Statistics statistics = new Statistics(game.getId().intValue());
                gameList.add(statistics);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT Users_Games VALUES (?,?,?)");
                preparedStatement.setInt(1, userID);
                preparedStatement.setInt(2, game.getId().intValue());
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
