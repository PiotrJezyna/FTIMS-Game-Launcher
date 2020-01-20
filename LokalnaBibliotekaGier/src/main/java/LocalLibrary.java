import fgl.product.Game;
import fgl.product.GameManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;

import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.time.StopWatch;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
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
    private Long userID;                                             //tu ID z klasy User
    private List<Game> localGames;
    private List<Statistics> gameList;
    private boolean isPlaying;
    private List<GameDownloader> downloaderList;
    private boolean isDownloading;
    private LocalGamesDAO dao;

    @FXML AnchorPane root;
    @FXML ScrollPane scrollPane;
    @FXML VBox localGamesBox;

    public void initialize() throws SQLException {
        scrollPane.setContent(localGamesBox);
        dao = new LocalGamesDAO();
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
                    } catch (SQLException | IOException e) {
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

    public void changePath(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("New Directory Path");
        window.setMinWidth(400);

        Label label = new Label();
        label.setText("Set new directory path");

        TextField field = new TextField();
        Button browse = new Button("Browse");
        Button close = new Button("Confirm");
        field.setText(dao.getPath().getAbsolutePath());

        browse.setOnAction(event -> {
            final DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(dao.getPath());
            File file = directoryChooser.showDialog(window);
            //dao.changePath(file);
            field.setText(file.getAbsolutePath());
        });

        if(field.getText() != null)
        {
            close.setOnAction(event ->
            {
                dao.changePath(new File(field.getText()));
                window.close();
            });
        }

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, field,browse, close);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public LocalLibrary() throws ClassNotFoundException {
        this.userID = 1L;
        gameList = new ArrayList<Statistics>();
        downloaderList = new ArrayList<GameDownloader>();

        Class.forName(JDBC_DRIVER);
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM Users_Games WHERE UserID = ?")) {
            preparedStatement.setLong(1, this.userID);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Statistics statistics = new Statistics(resultSet.getLong(2), resultSet.getDouble(3));
                gameList.add(statistics);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public int getLibraryID() {
        return this.libraryID;
    }

    public Long getUserID(){
        return userID;
    }

    public void play(Game game){
        Runtime runtime = Runtime.getRuntime();
        try{
            // Zamienic usera gry na zalogowanego usera !!!!
            gameList.get(localGames.indexOf(game)).onGameStart(userID, game.getId());
            isPlaying = true;

            String folder = dao.getPath().getAbsolutePath() + "\\" + game.getTitle() + "\\";
            String executable = game.getTitle() + ".exe";

            Process process = runtime.exec(folder + executable, null, new File(folder));
            process.waitFor();
            gameList.get(localGames.indexOf(game)).onGameClosed(userID, game.getId());
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
            statement.setLong(1, this.userID);
            statement.setLong(2, game.getId());
            final ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()){
                Statistics statistics = new Statistics(game.getId());
                gameList.add(statistics);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT Users_Games VALUES (?,?,?)");
                preparedStatement.setLong(1, userID);
                preparedStatement.setLong(2, game.getId());
                preparedStatement.setDouble(3, 0);
                preparedStatement.executeUpdate();
            }
        } catch (Exception se) {
            se.printStackTrace();
        }
    }


}
