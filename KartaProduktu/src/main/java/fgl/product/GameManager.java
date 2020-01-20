package fgl.product;

import fgl.userPanel.Login;
import fgl.userPanel.UserType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fgl.userPanel.UserDAO;
import javafx.scene.layout.AnchorPane;

import fgl.kartaocen.ReviewCard;

public class GameManager {

    public GameDAO dao;
    public UserDAO userDAO;
    public List <Game> games;

    private Game currentGame;

    @FXML private AnchorPane root;

    @FXML private Label gameTitle;
    @FXML private Label gameAuthor;
    @FXML private Label gameTags;
    @FXML private Label gameUserCount;
    @FXML private TextArea gameDescription;
    @FXML private Button editButton;
    @FXML private Button removeButton;
    @FXML private TextField newTitle;
    @FXML private TextField newTags;
    @FXML private Button buttonReviews;
    @FXML private Button buttonBack;
    @FXML private Button saveButton;

    public GameManager() throws SQLException {

        dao = new GameDAO();
        userDAO = new UserDAO();

        games = dao.getAll();
    }

    public Game GetProductCard(String title)
    {
        for(int i = 0; i < games.size(); i++)
        {
            if(games.get(i).getTitle().equals(title)) {
                return games.get(i);
            }
        }

        System.out.println("Game " + title + "not found");
        return null;
    }

    public void ShowProductCard(Game game) throws SQLException {

        currentGame = game;

        String author = userDAO.get(game.getUserId()).getName();

        gameTitle.setText(game.getTitle());
        gameTags.setText(game.getTags());
        gameAuthor.setText(author);
        gameUserCount.setText(game.getUserCount().toString());
        gameDescription.setText(game.getDescription());

        SetDefaultProductCardDisplaySettings();
        AddAdditionalButtons(game);

        System.out.println("Game: " + game.getTitle());
        System.out.println("Author: " + game.getUserId());
        System.out.println("Tags: " + game.getTags());
        //System.out.println("Genre: " + games.get(i).getGenre());
        System.out.println("Description: " + game.getDescription());
    }

    public void ShowProductCard(String title) throws SQLException {

        for(int i = 0; i < games.size(); i++)
        {
            if(games.get(i).getTitle().equals(title))
            {
                currentGame = games.get(i);

                String author = userDAO.get(games.get(i).getUserId()).getName();

                gameTitle.setText(games.get(i).getTitle());
                gameTags.setText(games.get(i).getTags());
                gameAuthor.setText(author);
                gameUserCount.setText(games.get(i).getUserCount().toString());
                gameDescription.setText(games.get(i).getDescription());

                SetDefaultProductCardDisplaySettings();
                AddAdditionalButtons(currentGame);

                System.out.println("Game: " + games.get(i).getTitle());
                System.out.println("Author: " + games.get(i).getUserId());
                System.out.println("Tags: " + games.get(i).getTags());
                //System.out.println("Genre: " + games.get(i).getGenre());
                System.out.println("Description: " + games.get(i).getDescription());

                break;
            }
        }
    }

    public void ShowReviews() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/RatingsCard.fxml"));

        root.getChildren().clear();
        root.getChildren().add(loader.load());
        ((ReviewCard)loader.getController()).setGame(currentGame.getId());
    }

    public void RemoveProductCard(String title) throws SQLException {

        for(int i = 0; i < games.size(); i++)
        {
            if(games.get(i).getTitle().equals(title))
            {
                dao.delete(games.get(i));
                games.remove(i);

                System.out.println("Game " + title + " successfully deleted.");
                break;
            }

            if (i == games.size() - 1)
                System.out.println("Game " + title + "not found. Game wasn't deleted");
        }
    }

    public void CreateProductCard(Long userId, String title, String tags, String path, String genre, String description) throws SQLException
    {
        boolean canCreate = true;

        for(int i = 0; i < games.size(); i++)
            if(games.get(i).getTitle().equals(title))
                canCreate = false;

        if(canCreate)
        {
            Game game = new Game(userId, title, tags, genre, description);

            games.add(game);

            dao.insert(game);
        }
        else
        {
            System.out.println("Game with this title \"" + title + "\" already exists!");
        }
    }

    // this should be written better, some overrides
    public void EditProductCard(String title, String newTitle, String tags, String path, String genre, String description) throws SQLException
    {
        if(currentGame != null)
        {
            if(newTitle != null)
                currentGame.setTitle(newTitle);

            if(tags != null)
                currentGame.setTags(tags);

            if(genre != null)
                currentGame.setGenre(genre);

            if(description != null)
                currentGame.setDescription(description);

            dao.update(currentGame);

            System.out.println("Game " + title + "successfully edited and saved.");
        }
    }

    @FXML
    private void buttonEditProductCard(ActionEvent event) throws Exception {

        newTitle.setText(gameTitle.getText());
        newTags.setText(gameTags.getText());

        gameTitle.setVisible(false);
        gameTags.setVisible(false);
        editButton.setVisible(false);
        removeButton.setVisible(false);
        buttonReviews.setVisible(false);

        newTags.setVisible(true);
        newTitle.setVisible(true);
        gameDescription.setEditable(true);
        buttonBack.setVisible(true);
        saveButton.setVisible(true);
    }

    @FXML
    private void SetDefaultProductCardDisplaySettings()
    {
        gameTitle.setVisible(true);
        gameTitle.setText(currentGame.getTitle());

        gameTags.setVisible(true);
        gameTags.setText(currentGame.getTags());

        editButton.setVisible(true);
        removeButton.setVisible(true);
        buttonReviews.setVisible(true);

        newTags.setVisible(false);
        newTitle.setVisible(false);
        gameDescription.setEditable(false);
        gameDescription.setText(currentGame.getDescription());

        buttonBack.setVisible(false);
        saveButton.setVisible(false);
    }

    @FXML
    private void SaveEditedProductCard()
    {
        System.out.println(gameDescription.getText());

        try
        {
            EditProductCard(currentGame.getTitle(), newTitle.getText(), newTags.getText(),
                    null, null, gameDescription.getText());
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception while editing product card!");
        }

        SetDefaultProductCardDisplaySettings();
    }

    @FXML
    private void ButtonRemoveProductCard()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Removing game");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to remove this game?");

        alert.showAndWait();

        if(alert.getResult() == ButtonType.OK)
        {
            try
            {
                RemoveProductCard(currentGame.getTitle());

                Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                informationAlert.setTitle("Game removed");
                informationAlert.setHeaderText(null);
                informationAlert.setContentText("The game has been successfully removed.");

                informationAlert.showAndWait();

                try {
                    AnchorPane loadedFxml = FXMLLoader.load( getClass().getResource("/CatalogCard.fxml") );

                    root.getChildren().clear();
                    root.getChildren().add( loadedFxml );

                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
            catch (SQLException e)
            {
                System.out.println("SQL Exception while removing product card!");
            }
        }
    }

    private void AddAdditionalButtons(Game game)
    {
        if(Login.userSession.getCurrentUser().getId() != game.getUserId()
            && Login.userSession.getCurrentUser().getType() == UserType.USER)
        {
            editButton.setVisible(false);

            removeButton.setVisible(false);
        }
    }
}
