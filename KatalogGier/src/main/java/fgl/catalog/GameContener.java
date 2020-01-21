package fgl.catalog;

import fgl.product.*;
import fgl.userPanel.Login;
import fgl.drive.*;

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

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.File;
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
    private Long userID;

    private GameDAO gameDao = new GameDAO();
    private ChangelogDAO changelogDao = new ChangelogDAO();

    public void main(String[] args) {

    }

    public List<Game> getDisplayedGames() throws Exception {
        updateDisplayedGames();
        return displayedGames;
    }
    public List<Changelog> getDisplayedChangelogs() throws Exception {
        updateDisplayedChangelogs();
        return displayedChangelogs;
    }

    public int getCategory() {
        return category;
    }

    public int getRecordCount() {
        if (category != 4)
            return displayedGames.size();
        else
            return displayedChangelogs.size();

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
    public void setSearchPhrase(String searchPhrase) {
        this.searchPhrase = searchPhrase;
    }
    public void setUserID (Long id) {
        this.userID = id;
    }

    private void downloadDataFromDatabase() throws Exception {

        if (category == 0) {
            this.games = gameDao.getAll();
        } else if (category == 1) {
            this.games = gameDao.getAllWithQuery("SELECT A.ID, A.UserID, A.Title, A.Version, A.Tags, A.UserCount, A.IsReported FROM Games A, Users_Games B WHERE A.ID = B.GameID and B.LastInstallation >= curdate() - INTERVAL DAYOFWEEK(curdate())+6 DAY GROUP BY A.ID ORDER BY COUNT(*) DESC LIMIT 2");
        } else if (category == 2) {
            this.games = gameDao.getAllWithQuery("SELECT Games.ID, Games.UserID, Games.Title, Games.Version, Games.Tags, Games.UserCount, Games.IsReported FROM Games " +
                    "INNER JOIN Games_Genres ON Games.ID = Games_Genres.GameID " +
                    "INNER JOIN Genres ON Games_Genres.GenreID = Genres.ID " +
                    "WHERE Genres.ID IN (SELECT Genres.ID FROM Genres, Games WHERE (Games.ID IN (SELECT GameID FROM Users_Games WHERE (Users_Games.UserID = " + userID + "))) AND " +
                    "(Genres.ID IN (SELECT GenreID FROM Games_Genres WHERE Games_Genres.GameID = Games.ID)))" +
                    "GROUP BY Games.ID");
        } else if (category == 3) {
            this.games = gameDao.getAllWithQuery("SELECT ID, UserID, Title, Version, Tags, UserCount, IsReported FROM Games WHERE (Games.ID IN (SELECT GameID FROM Users_Games WHERE (Users_Games.UserID = " + userID + ")))");
        } else if (category == 4) {
            this.changelogs = changelogDao.getAll();
        }
    }

    public void updateDisplayedChangelogs() throws Exception {
        //displayedChangelogs.clear();
        downloadDataFromDatabase();

        // check title

        displayedChangelogs = changelogs;

        //pageCount = (int)Math.ceil((double) displayedChangelogs.size() / recordPerPage);
        //displayChangelogs();

    }

    private void updateDisplayedGames() throws Exception {
        displayedGames.clear();
        downloadDataFromDatabase();

        for (Game game : games) {

            boolean categoryFlag = true;
            boolean searchPhraseFlag = true;
            boolean tagsFlag = true;

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
    }


}

