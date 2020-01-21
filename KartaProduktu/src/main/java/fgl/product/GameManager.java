package fgl.product;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import fgl.userPanel.*;
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

        System.out.println("Game: " + game.getTitle());
        System.out.println("Author: " + game.getUserId());
        System.out.println("Tags: " + game.getTags());
        //System.out.println("Genre: " + games.get(i).getGenre());
        //System.out.println("Description: " + games.get(i).getDescription());
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

                System.out.println("Game: " + games.get(i).getTitle());
                System.out.println("Author: " + games.get(i).getUserId());
                System.out.println("Tags: " + games.get(i).getTags());
                //System.out.println("Genre: " + games.get(i).getGenre());
                //System.out.println("Description: " + games.get(i).getDescription());

                break;
            }
        }
    }

    public void showReviews() throws IOException {
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
        for(int i = 0; i < games.size(); i++)
        {
            if(games.get(i).getTitle().equals(title))
            {
                if(newTitle != null)
                    games.get(i).setTitle(newTitle);

                if(tags != null)
                    games.get(i).setTags(tags);

                if(genre != null)
                    games.get(i).setGenre(genre);

                if(description != null)
                    games.get(i).setDescription(description);

                dao.update(games.get(i));

                System.out.println("Game " + title + "successfully edited and saved.");
                break;
            }

            if(i == games.size() - 1)
                System.out.println("Game " + title + "not found");
        }
    }

    // ---according to the class scheme from the presentation--
    /*
    public void CreateProductCard(String title, User author, String linkToDownload, String md5, String genre, List<String> tags,
                      String description, List<Image> screenshots, Image icon, int downloads)
    {
        Game game = new Game(title, author, linkToDownload, md5, genre, tags, description, screenshots, icon, downloads);
    }

    public void EditProductCard(String title, User author, String linkToDownload, String md5, String genre, List<String> tags,
                                  String description, List<Image> screenshots, Image icon, int downloads)
    {

    } */
}
