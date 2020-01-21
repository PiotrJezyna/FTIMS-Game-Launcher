package fgl.product;

import fgl.drive.DriveDao;
import fgl.userPanel.Login;
import fgl.userPanel.UserSession;
import fgl.userPanel.UserType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.awt.image.DirectColorModel;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.Date;

import fgl.userPanel.UserDAO;
import javafx.scene.layout.AnchorPane;

import fgl.kartaocen.ReviewCard;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GameManager {

    public GameDAO dao;
    public UserDAO userDAO;
    public ChangelogDAO changelogDAO;
    public List <Game> games;

    private Game currentGame;
    private Changelog currentChangelog;

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
    @FXML private TextArea changelog;
    @FXML private Label changelogDate;

    @FXML private Label pathZipLabel;
    @FXML private Label pathScreenshotLabel;
    @FXML private Button pathZipButton;
    @FXML private Button pathScreenshotButton;

    @FXML private TextField updatedGamePathZip;
    @FXML private TextField updatedGamePathScreenshot;

    @FXML private AnchorPane anchorid;
    @FXML private TextField newGameTitle;
    @FXML private TextField newGameTags;
    @FXML private TextField newGamePathZip;
    @FXML private TextField newGamePathScreenshot;
    @FXML private TextArea newGameDescription;




    public GameManager() throws SQLException {

        dao = new GameDAO();
        userDAO = new UserDAO();
        changelogDAO = new ChangelogDAO();

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

        if(changelogDAO.get(currentGame) != null)
            currentChangelog = changelogDAO.get(currentGame);
        else
            currentChangelog = new Changelog(0L, currentGame.getId(), 0L,
                    "", new Date(new java.util.Date().getTime()), currentGame.getTitle());

        String author = userDAO.get(game.getUserId()).getName();

        gameTitle.setText(game.getTitle());
        gameTags.setText(game.getTags());
        gameAuthor.setText(author);
        gameUserCount.setText(game.getUserCount().toString());
        gameDescription.setText(game.getDescription());

        SetDefaultProductCardDisplaySettings();
        AddAdditionalButtons(game);
        SetGameStats(game);

        System.out.println("Game: " + game.getTitle());
        System.out.println("Author: " + game.getUserId());
        System.out.println("Tags: " + game.getTags());
        System.out.println("Description: " + game.getDescription());
    }

    public void ShowProductCard(String title) throws SQLException {

        for(int i = 0; i < games.size(); i++)
        {
            if(games.get(i).getTitle().equals(title))
            {
                currentGame = games.get(i);

                if(changelogDAO.get(currentGame) != null)
                    currentChangelog = changelogDAO.get(currentGame);
                else
                    currentChangelog = new Changelog(0L, currentGame.getId(), 0L,
                            "", new Date(new java.util.Date().getTime()), currentGame.getTitle());

                String author = userDAO.get(games.get(i).getUserId()).getName();

                gameTitle.setText(games.get(i).getTitle());
                gameTags.setText(games.get(i).getTags());
                gameAuthor.setText(author);
                gameUserCount.setText(games.get(i).getUserCount().toString());
                gameDescription.setText(games.get(i).getDescription());

                SetDefaultProductCardDisplaySettings();
                AddAdditionalButtons(currentGame);
                SetGameStats(currentGame);

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
                //dao.delete(games.get(i));

                games.get(i).setDeleted(true);
                dao.update(games.get(i));

                games.remove(i);

                System.out.println("Game " + title + " successfully deleted.");
                break;
            }

            if (i == games.size() - 1)
                System.out.println("Game " + title + "not found. Game wasn't deleted");
        }
    }

    public void CreateProductCard(Long userId, String title, Integer version,  String description, String tags) throws SQLException
    {
        boolean canCreate = true;

        for(int i = 0; i < games.size(); i++)
            if(games.get(i).getTitle().equals(title))
                canCreate = false;

        if(canCreate)
        {
            Game game = new Game(userId, title, 1,  description, tags);

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
    private void informationWindow(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    @FXML
    private void SendNewGameButton(){
        System.out.println(newGameTags.getText());
        try
        {
            if(newGameTitle.getText().isEmpty())  {

                informationWindow("Błędne dane", "Tytuł gry jest pusty");
            }
            else if(newGameTags.getText().isEmpty())  {

                informationWindow("Błędne dane", "Tagi gry są puste");
            }
            else if(newGameDescription.getText().isEmpty())  {

                informationWindow("Błędne dane", "Opis gry jest pusty");
            }
            else if(newGamePathZip.getText().isEmpty())  {

                informationWindow("Błędne dane", "Podaj ścieżkę do pliku z grą");
            }
            else if(newGamePathScreenshot.getText().isEmpty())  {

                informationWindow("Błędne dane", "Podaj ścieżkę do pliku z zrzutem ekranu");
            }
            else{
                // -----------------------------------------
                // tutaj zmienne newGamePathZip i newGamePathScreenshot są już wybrane, można pobierać ich wartość
                // -----------------------------------------
                DriveDao driveDao = new DriveDao();
                try {
                    driveDao.uploadGame(newGameTitle.getText(), newGamePathZip.getText(), newGamePathScreenshot.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                CreateProductCard(UserSession.getUserSession().getCurrentUser().getId(),  newGameTitle.getText(), 1, newGameDescription.getText(), newGameTags.getText() );
                informationWindow("Sukces", "Gra została poprawnie dodana do platformy FTIMS Game Launcher");
                try {

                    AnchorPane loadedFxml = FXMLLoader.load( getClass().getResource("/CatalogCard.fxml") );

                    anchorid.getChildren().clear();
                    anchorid.getChildren().add( loadedFxml );

                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }

        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception while adding new product card!");
        }
    }
    @FXML
    private void newGamePathZipButton(ActionEvent event)
    {
        final FileChooser filChooser = new FileChooser();

        Stage stage = (Stage) anchorid.getScene().getWindow();

        File file = filChooser.showOpenDialog(stage);
        if(file != null)
        {
            newGamePathZip.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void newGamePathScreenshotButton(ActionEvent event)
    {
        final FileChooser filChooser = new FileChooser();

        Stage stage = (Stage) anchorid.getScene().getWindow();

        File file = filChooser.showOpenDialog(stage);
        if(file != null)
        {
            newGamePathScreenshot.setText(file.getAbsolutePath());
        }
    }
    @FXML
    private void updatedGamePathZipButton(ActionEvent event)
    {
        final FileChooser filChooser = new FileChooser();

        Stage stage = (Stage) root.getScene().getWindow();

        File file = filChooser.showOpenDialog(stage);
        if(file != null)
        {
            updatedGamePathZip.setText(file.getAbsolutePath());
        }
    }
    @FXML
    private void updatedGamePathScreenshotButton(ActionEvent event)
    {
        final FileChooser filChooser = new FileChooser();

        Stage stage = (Stage) root.getScene().getWindow();

        File file = filChooser.showOpenDialog(stage);
        if(file != null)
        {
            updatedGamePathScreenshot.setText(file.getAbsolutePath());
        }
    }


    @FXML
    private void buttonEditProductCard(ActionEvent event) throws Exception {

        newTitle.setText(gameTitle.getText());
        newTags.setText(gameTags.getText());

        //gameTitle.setVisible(false);
        gameTags.setVisible(false);
        editButton.setVisible(false);
        removeButton.setVisible(false);
        buttonReviews.setVisible(false);

        newTags.setVisible(true);
        //newTitle.setVisible(true);
        gameDescription.setEditable(true);
        buttonBack.setVisible(true);
        saveButton.setVisible(true);


        changelog.setText("");
        changelog.setEditable(true);

        pathZipLabel.setVisible(true);
        pathScreenshotLabel.setVisible(true);
        pathZipButton.setVisible(true);
        pathScreenshotButton.setVisible(true);

        updatedGamePathZip.setVisible(true);
        updatedGamePathScreenshot.setVisible(true);

    }

    @FXML
    private void SetDefaultProductCardDisplaySettings()
    {
        changelog.setText(currentChangelog.getDescription());
        changelogDate.setText(currentChangelog.getDate().toString());

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

        pathZipLabel.setVisible(false);
        pathScreenshotLabel.setVisible(false);
        pathZipButton.setVisible(false);
        pathScreenshotButton.setVisible(false);

        updatedGamePathZip.setVisible(false);
        updatedGamePathScreenshot.setVisible(false);

        try
        {
            setChangelogInfo();
        }
        catch (SQLException e)
        {
            System.out.println("SQL error while getting all changelogs");
        }

        changelog.setEditable(false);
    }

    @FXML
    private void SaveEditedProductCard()
    {
        System.out.println(gameDescription.getText());

        try
        {

            if(newTitle.getText().isEmpty())
            {
                informationWindow("Błędne dane", "Tytuł gry jest pusty");
            }
            else if(newTags.getText().isEmpty())
            {
                informationWindow("Błędne dane", "Tagi gry są puste");
            }
            else if(gameDescription.getText().isEmpty())
            {
                informationWindow("Błędne dane", "Opis gry jest pusty");
            }
            else {

                EditProductCard(currentGame.getTitle(), newTitle.getText(), newTags.getText(),
                        null, null, gameDescription.getText());

                currentChangelog.setDate(new Date(new java.util.Date().getTime()));
                currentChangelog.setDescription(changelog.getText());
                currentChangelog.setVersion(currentChangelog.getVersion() + 1);

                //changelogDAO.update(currentChangelog);

                changelogDAO.insert(new Changelog(0L, currentGame.getId(), currentChangelog.getVersion(),
                        currentChangelog.getDescription(), currentChangelog.getDate(), currentGame.getTitle()));

                SetDefaultProductCardDisplaySettings();
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQL Exception while editing product card!");
            SetDefaultProductCardDisplaySettings();
        }


    }

    private void setChangelogInfo() throws SQLException
    {
        List<Changelog> changelogs = changelogDAO.getAll();

        StringBuilder sB = new StringBuilder();

        for(int i = 0; i < changelogs.size(); i++)
        {
            if(changelogs.get(i).getGameId() == currentGame.getId())
            {
                sB.append("-----------\n");
                sB.append(changelogs.get(i).getDate().toString());
                sB.append("\n-----------\n");
                sB.append(changelogs.get(i).getDescription());
                sB.append("\n");
            }
        }

        changelog.setText(sB.toString());
    }

    @FXML
    private void downloadGameButton() throws IOException {

        DriveDao driveDao = new DriveDao();
        driveDao.downloadGame(currentGame.getTitle());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LibraryCard.fxml"));
        root.getChildren().clear();
        root.getChildren().add(loader.load());
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
        if(UserSession.getUserSession().getCurrentUser().getId() != game.getUserId()
            && UserSession.getUserSession().getCurrentUser().getType() == UserType.USER)
        {
            editButton.setVisible(false);

            removeButton.setVisible(false);
        }
    }

    @FXML private Label usersTimeSpent;
    @FXML private Label usersAvgTimeSpent;

    private void SetGameStats(Game game) throws SQLException
    {
        usersTimeSpent.setText((int)dao.GetTimeInGame(game) + " min");
        usersAvgTimeSpent.setText((int)dao.GetTimeInGamePerUser(game) + " min");
        //System.out.println(dao.GetTimeInGame(game));
        //System.out.println(dao.GetTimeInGamePerUser(game));
    }

}
