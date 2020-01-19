package fgl.catalog;

import fgl.product.*;
import fgl.userPanel.Login;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameContener {

    private List<Game> games = new ArrayList<>();
    private List<Game> displayedGames = new ArrayList<>();

    private List<Changelog> changelogs = new ArrayList<>();
    private List<Changelog> displayedChangelogs = new ArrayList<>();

    private List<String> tags = new ArrayList<String>();
    private int category = 0;   // 0 - no category (display all)
    private int typeOfSort; // 0 - no sort
    private String searchPhrase = new String();
    private Login login;

    private GameDAO gameDao = new GameDAO();
    private ChangelogDAO changelogDao = new ChangelogDAO();

    public Button wszystkie;
    public Button naCzasie;
    public Button polecane;
    public Button historia;
    public Button panelAkt;

    @FXML
    private AnchorPane root;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox gamesBox;

    @FXML
    private TextField phaseField;
    @FXML
    private TextField tagsField;

    public void initialize() {
        scrollPane.setContent(gamesBox);
        main(null);
    }

    public void main(String[] args) {

        login = new Login();
        try {
            login.validate("js", "js");
        }
        catch (Exception e) {}

        System.out.println(login.getUserSession().getCurrentUser().getId());

        updateDisplayedGames();
    }

    public List<Game> getDisplayedGames() {
        return displayedGames;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setTypeOfSort(int typeOfSort) {
        this.typeOfSort = typeOfSort;
    }

    @FXML
    public void setSearchPhrase(String searchPhrase) {
        this.searchPhrase = searchPhrase;
    }

    public void updateDisplayedChangelogs() throws Exception{
        //displayedChangelogs.clear();

        // check title

        displayedChangelogs = changelogs;

        displayChangelogs();

    }

    public void updateDisplayedGames() {
        displayedGames.clear();

        for (Game game : games) {

            boolean categoryFlag = true;
            boolean searchPhraseFlag = true;
            boolean tagsFlag = true;

            if (category != 0 && category != 3 && category != 1)
                categoryFlag = false;

            if (!searchPhrase.isEmpty())
                searchPhraseFlag = (game.getTitle().contains(searchPhrase));

            if (!tags.isEmpty() && game.getTags() != null) {
                tagsFlag = false;
                for (String tag : tags) {
                    if (game.getTags().contains(tag)) {
                        tagsFlag = true;
                        break;
                    }
                }
            }

            if (categoryFlag && searchPhraseFlag && tagsFlag)
                displayedGames.add(game);
        }

        displayGames();
    }

    public void displayChangelogs() throws  Exception {
        gamesBox.getChildren().clear();

        for (Changelog changelog : displayedChangelogs) {
            HBox gameBox = new HBox();

            Game game = gameDao.get(changelog.getGameId());

            Label label = new Label(game.getTitle());
            Label labelDescription = new Label(changelog.getDescription());
            Button button = new Button("Open Card");


            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductCard.fxml"));

                        root.getChildren().clear();
                        root.getChildren().add(loader.load());

                        GameManager gm = loader.getController();
                        gm.ShowProductCard(game);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            gameBox.getChildren().add(button);
            gameBox.getChildren().add(label);
            gameBox.getChildren().add(labelDescription);

            HBox.setMargin(button, new Insets(0, 10, 0, 10));
            HBox.setMargin(label, new Insets(0, 10, 0, 10));
            HBox.setMargin(labelDescription, new Insets(0, 10, 0, 10));
            VBox.setMargin(gameBox, new Insets(10, 0, 5, 0));

            gamesBox.getChildren().add(gameBox);
        }
    };


    public void displayGames() {
        gamesBox.getChildren().clear();
        System.out.println(displayedGames.size());

        for (Game game : displayedGames) {
            HBox gameBox = new HBox();

            Label label = new Label(game.getTitle());
            Button button = new Button("Open Card");

            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductCard.fxml"));

                        root.getChildren().clear();
                        root.getChildren().add(loader.load());

                        GameManager gm = loader.getController();
                        gm.ShowProductCard(game);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            gameBox.getChildren().add(button);
            gameBox.getChildren().add(label);

            HBox.setMargin(button, new Insets(0, 10, 0, 10));
            HBox.setMargin(label, new Insets(0, 10, 0, 10));
            VBox.setMargin(gameBox, new Insets(10, 0, 5, 0));

            gamesBox.getChildren().add(gameBox);
        }
    }

    @FXML
    void buttonOnAction(ActionEvent event) {

        setSearchPhrase(phaseField.getText());

        if (tagsField.getText().isEmpty() == false) {
            String[] tags = tagsField.getText().split(",");
            setTags(Arrays.asList(tags));
        } else {
            setTags(new ArrayList<String>());
        }

        updateDisplayedGames();
    }

    @FXML
    void handleCategoryButton(ActionEvent event) throws Exception {

        phaseField.clear();
        setSearchPhrase(phaseField.getText());

        if (wszystkie.isHover()) {
            category = 0;

            try {
                this.games = gameDao.getAll();
            } catch (SQLException e) {
            }
        }
        if (naCzasie.isHover()) {
            category = 1;

            try {
                this.games = gameDao.getAllWithQuery("SELECT A.ID, A.UserID, A.Title, A.Version, A.Tags, A.UserCount, A.IsReported FROM Games A, Users_Games B WHERE A.ID = B.GameID and B.LastInstallation >= curdate() - INTERVAL DAYOFWEEK(curdate())+6 DAY GROUP BY A.ID ORDER BY COUNT(*) DESC LIMIT 2");
            } catch (SQLException e) {
            }
        }
        if (polecane.isHover()) {
            category = 2;
        }
        if (historia.isHover()) {
            category = 3;

            try {
                this.games = gameDao.getAllWithQuery("SELECT ID, UserID, Title, Version, Tags, UserCount, IsReported FROM Games WHERE (Games.ID IN (SELECT GameID FROM Users_Games WHERE (Users_Games.UserID = " + login.getUserSession().getCurrentUser().getId() + ")))");
            } catch (SQLException e) {
            }
        }
        if (panelAkt.isHover()) {
            category = 4;

            try {
                this.changelogs = changelogDao.getAll();
            } catch (SQLException e) {
            }
        }

        if (category != 4)
            updateDisplayedGames();
        else
            updateDisplayedChangelogs();

    }
}

