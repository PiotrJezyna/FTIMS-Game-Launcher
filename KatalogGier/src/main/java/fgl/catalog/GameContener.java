package fgl.catalog;

import fgl.product.Game;
import fgl.product.GameDAO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
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

import fgl.product.GameManager;

public class GameContener {

    private List<Game> allGames = new ArrayList<>();
    private List<Game> displayedGames = new ArrayList<>();

    private List<String> tags = new ArrayList<String>();
    private int category;   // 0 - no category (display all)
    private int typeOfSort; // 0 - no sort
    private String searchPhrase = new String();

    @FXML private AnchorPane productCard;
    @FXML private AnchorPane root;

    @FXML private ScrollPane scrollPane;
    @FXML private VBox gamesBox;

    @FXML private TextField phaseField;
    @FXML private TextField tagsField;

    public void initialize() {
        scrollPane.setContent(gamesBox);
        main(null);
    }

    public void main( String[] args ) {

        GameDAO games = new GameDAO();
        try {
            allGames = games.getAll();
        }
        catch(SQLException e) {};

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

    @FXML public void setSearchPhrase(String searchPhrase) {
        this.searchPhrase = searchPhrase;
    }

    public void updateDisplayedGames()
    {
        displayedGames.clear();

        for (Game game: allGames) {

            boolean categoryFlag = true;
            boolean searchPhraseFlag = true;
            boolean tagsFlag = true;

            /*if (category != 0)
                categoryFlag = (game.getCategory() == category);*/

            if (!searchPhrase.isEmpty())
                searchPhraseFlag = (game.getTitle().contains(searchPhrase));

            if (!tags.isEmpty())
            {
                tagsFlag = false;
                for (String tag: tags) {
                    if (game.getTags().contains(tag))
                    {
                        tagsFlag = true;
                        break;
                    }
                }
            }

            if (categoryFlag && searchPhraseFlag && tagsFlag)
                displayedGames.add(game);
        }

        gamesBox.getChildren().clear();
        System.out.println(displayedGames.size());
        for (Game game: displayedGames) {

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

    @FXML void buttonOnAction(ActionEvent event) {

        setSearchPhrase(phaseField.getText());

        if (tagsField.getText().isEmpty() == false)
        {
            String[] tags = tagsField.getText().split(",");
            setTags(Arrays.asList(tags));
        }
        else
        {
            setTags(new ArrayList<String>());
        }

        updateDisplayedGames();
    }
}
