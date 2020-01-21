
    package fgl.catalog;

    import fgl.drive.DriveDao;
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
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.layout.AnchorPane;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.VBox;

    //import java.awt.*;
    import java.io.File;
    import java.io.IOException;
    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;

public class GameContenerGUI {

    private Login login;

    private int recordPerPage = 5;
    private int pageNumber = 0;
    private int pageCount = 0;

    public Button wszystkie;
    public Button naCzasie;
    public Button polecane;
    public Button historia;
    public Button panelAkt;
    public Button prevPage;
    public Button nextPage;

    private GameContener gc = new GameContener();

    @FXML
    private AnchorPane root;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox gamesBox;
    @FXML
    private TextField phraseField;
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

        gc.setUserID(login.getUserSession().getCurrentUser().getId());
        gc.setCategory(0);
        try {
            displayGames();
        } catch (Exception e) {}


        //gc.updateDisplayedGames();
    }

    @FXML
    public void setSearchPhrase(String searchPhrase) {
        gc.setSearchPhrase(searchPhrase);
    }

    public void displayChangelogs() throws  Exception {
        gamesBox.getChildren().clear();

        List<Changelog> displayedChangelogs = gc.getDisplayedChangelogs();
        pageCount = (int)Math.ceil((double) gc.getRecordCount() / recordPerPage);

        int start = pageNumber * recordPerPage;
        int length = ((displayedChangelogs.size() - start) < recordPerPage) ? (displayedChangelogs.size() - start) : recordPerPage;

        for (int i = start; i < start + length; ++i) {
        //for (Changelog changelog : displayedChangelogs) {
            Changelog changelog = displayedChangelogs.get(i);
            HBox gameBox = new HBox();

            GameDAO gameDao = new GameDAO();
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
            //gameBox.getChildren().add(label);
            gameBox.getChildren().add(labelDescription);

            HBox.setMargin(button, new Insets(0, 10, 0, 10));
            //HBox.setMargin(label, new Insets(0, 10, 0, 10));
            HBox.setMargin(labelDescription, new Insets(0, 10, 0, 10));
            VBox.setMargin(gameBox, new Insets(10, 0, 5, 0));

            gamesBox.getChildren().add(gameBox);
        }
    };

    private ArrayList<File> downloadScreenshots (List<Game> games) throws Exception {
        DriveDao driveDao = new DriveDao();

        ArrayList<File> files = new ArrayList<>();
        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 1; i++) {
            final int var = i;
            Runnable runnable = () -> {
                try {
                    files.add( var, driveDao.downloadScreenshot(games.get(var).getTitle()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            Thread t = new Thread(runnable);
            threads.add(t);
            t.start();
        }

        for (int i = 0; i < threads.size(); i++) {
            if (threads.get(i).isAlive()) {
                threads.get(i).join();
            }
        }

        return files;
    }

    public void displayGames() throws Exception {
        gamesBox.getChildren().clear();

        List<Game> displayedGames = gc.getDisplayedGames();
        //ArrayList<File> files = downloadScreenshots(displayedGames);

        pageCount = (int)Math.ceil((double) gc.getRecordCount() / recordPerPage);

        int start = pageNumber * recordPerPage;
        int length = ((displayedGames.size() - start) < recordPerPage) ? (displayedGames.size() - start) : recordPerPage;



        for (int i = start; i < start + length; ++i) {
            Game game = displayedGames.get(i);
            HBox gameBox = new HBox();

            //Image image = new Image( files.get(0).toURI().toString(), 120, 120, true, false );
            //ImageView imageView = new ImageView(image);

            Label label = new Label(game.getTitle());
            Button button = new Button("Open Card");

            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductCard.fxml"));

                        root.getChildren().clear();
                        root.getChildren().add(loader.load());
                        ImageView imageView;

                        GameManager gm = loader.getController();
                        gm.ShowProductCard(game);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            //gameBox.getChildren().add(imageView);
            gameBox.getChildren().add(button);
            gameBox.getChildren().add(label);

            HBox.setMargin(button, new Insets(0, 10, 0, 10));
            HBox.setMargin(label, new Insets(0, 10, 0, 10));
            //HBox.setMargin(imageView, new Insets(0, 10, 0, 10));
            VBox.setMargin(gameBox, new Insets(10, 0, 5, 0));

            gamesBox.getChildren().add(gameBox);
        }
    }

    @FXML
    void buttonOnAction(ActionEvent event) throws Exception {

        setSearchPhrase(phraseField.getText());

        if (tagsField.getText().isEmpty() == false) {
            String[] tags = tagsField.getText().split(",");
            gc.setTags(Arrays.asList(tags));
        } else {
            gc.setTags(new ArrayList<String>());
        }

        pageNumber = 0;

        displayGames();

    }

    @FXML
    void handlePageButton(ActionEvent event) throws Exception {
        boolean dirtyFlag = false;

        if (prevPage.isHover()) {
            if (pageNumber > 0) {
                pageNumber--;
                dirtyFlag = true;
            }
        }
        if (nextPage.isHover()) {
            if (pageNumber < pageCount - 1) {
                pageNumber++;
                dirtyFlag = true;
            }
        }

        if (!dirtyFlag)
            return;

        if (gc.getCategory() != 4)
            displayGames();
        else
            displayChangelogs();
    }

    @FXML
    void handleCategoryButton(ActionEvent event) throws Exception {

        phraseField.clear();
        setSearchPhrase(phraseField.getText());
        pageNumber = 0;

        boolean showChangelogs = false;

        if (wszystkie.isHover()) {
            gc.setCategory(0);
        }
        if (naCzasie.isHover()) {
            gc.setCategory(1);
        }
        if (polecane.isHover()) {
            gc.setCategory(2);
        }
        if (historia.isHover()) {
            gc.setCategory(3);
        }
        if (panelAkt.isHover()) {
            gc.setCategory(4);
            showChangelogs = true;
        }

        if (!showChangelogs)
            displayGames();
        else
            displayChangelogs();

        pageCount = (int)Math.ceil((double) gc.getRecordCount() / recordPerPage);

    }

}

