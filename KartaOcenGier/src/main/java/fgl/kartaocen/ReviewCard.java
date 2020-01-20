// ////////////////////////////////////////////////////////////////// Package //
package fgl.kartaocen;

// ////////////////////////////////////////////////////////////////// Imports //
// ================================================================ JavaFX == //
import fgl.product.Game;
import fgl.product.GameDAO;
import fgl.userPanel.User;
import fgl.userPanel.UserDAO;
import fgl.userPanel.UserType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

// ================================================================= Other == //
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// //////////////////////////////////////////////////////// Class: ReviewCard //
public class ReviewCard {

    // ============================================================== Data == //
    private User loggedUser;
    private Game game;
    private Long ID;
    public ReviewDao reviewDao;
    public CommentDao commentDao;
    public List<Review> reviews;
    public List<Comment> comments;
    private int rating;

    // ========================================================= Behaviour == //
    public void setGame(Long id) {
//        this.game = id;
    }

    private boolean isUserAuthor() {
        return loggedUser.getId().equals(game.getUserId());
    }

    private boolean hasUserReviewedBefore() {
        for (Review review : reviews) {
            if (loggedUser.getId().equals(review.getUser().getId())) {
                return true;
            }
        }
        return false;
    }

    public ReviewCard(/*Game game, User user*/) throws SQLException {
        reviewDao = new ReviewDao();
        commentDao = new CommentDao();

        reviews = reviewDao.getAll();
        comments = commentDao.getAll();

        // todo: remove this
        loggedUser = (new UserDAO()).get(2L);
        game = (new GameDAO()).get(2L);
    }

    @FXML
    public void initialize() throws SQLException {
        // Set label
        labelGameTitle.setText(game.getTitle());

        // Create button reference
        buttonRatings = new ArrayList<>();
        buttonRatings.add(buttonRating1);
        buttonRatings.add(buttonRating2);
        buttonRatings.add(buttonRating3);
        buttonRatings.add(buttonRating4);
        buttonRatings.add(buttonRating5);
        buttonRatings.add(buttonRating6);
        buttonRatings.add(buttonRating7);
        buttonRatings.add(buttonRating8);
        buttonRatings.add(buttonRating9);
        buttonRatings.add(buttonRating10);

//        progressBar.setVisible(false);

        if (hasUserReviewedBefore()) {
            for (Review review : reviews) {
                if (loggedUser.getId().equals(review.getUser().getId())) {
                    rating = review.getRating();
                    unhover();
                }
            }
        }
    }
    //Now we use test1()
   /* private void writeReviewToDatabase(Review review) throws IOException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Opinions " +
                             "(UserID, GameID, Date, Comment, Opinion, Rate) " +
                             "VALUES (?, ?, ?, ?, ?, ?)")) {

            // Construct insert statement
            statement.setLong(1, loggedUser);
            statement.setLong(2, game);
            statement.setDate(3, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
            statement.setString(4, textAreaReview.getText());
            statement.setObject(5, null);
            statement.setInt(6, rating);

            statement.executeUpdate();

            labelStatus.setText("written correctly");

        } catch (SQLException se) {
            se.printStackTrace();
            labelStatus.setText("database error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

    public void render() {
    }

    public void handleEvents() {
    }

    //public double getAverageRating(Game game) {
    //    return 0.0;
    //}

    @FXML
    private void addReview() throws IOException, ClassNotFoundException, SQLException {
        progressBar.setVisible(true);
        progressBar.setProgress(0.0);

        Review review = new Review(0L, game, loggedUser, rating);
        Comment comment = new Comment(0L, review, textAreaReview.getText(), null, false);

        progressBar.setProgress(0.5);

        reviewDao.insert(review);
        reviews.add(review);

        progressBar.setProgress(0.75);

        commentDao.insert(comment);
        comments.add(comment);

        progressBar.setProgress(1.0);
        progressBar.setVisible(false);
        labelStatus.setText("written correctly");

        textAreaReview.setDisable(true);
    }

    private void editReview(Review review) {
    }

    private void notifyAuthor() {
    }

    //Now we use test()
   /* @FXML
    private void readReview() throws ClassNotFoundException, SQLException  {
        Class.forName(JDBC_DRIVER);
        Connection connection;
        Statement statement;

        connection = DriverManager.getConnection(DB_URL, USER, PASS);
        statement = connection.createStatement();
        String sql = "SELECT * from Opinions";
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()){
            int id = resultSet.getInt("id");
            int userID = resultSet.getInt("UserID");
            int gameID = resultSet.getInt("GameID");
            int rate = resultSet.getInt("Rate");
            String comment = resultSet.getString("Comment");

            for (int i = 0; i < 1; i++){
                if ((long)gameID == game) {
                    boxReviews.getChildren().add(new Label("id: " + Integer.toString(id) + " UserID: " + Integer.toString(userID) + " GameID: " +  Integer.toString(gameID) + " user's rate:  " + rate + " user's commentt: " + comment));
                }
            }
        }
    }*/
    @FXML
    private void test() throws ClassNotFoundException, SQLException {

//        for (int i = 1; i < reviews.size(); i++) {
//            review = reviews.get(i);
//            for (int j = 0; j < 1; j++) {
//                boxReviews.getChildren().add(new Label("ID: " + review.getId() + " " + "date: " + new java.sql.Date(Calendar.getInstance().getTime().getTime()) + " UserID: " + review.getUser() + " GameID: " + review.getGame() + " user's rate:  " + review.getRating() + " user's commentt: " + review.getComment()));
//            }
//        }
    }

    @FXML
    private void test1(Review review) throws SQLException {
    }

    @FXML
    private void authorsReply() throws SQLException {
//        Long ID = Long.parseLong(textAreaID.getText());
//
//        for (int i = 0; i < reviews.size(); i++) {
//            if (reviews.get(i).getId().equals(ID)) {
//                reviews.get(i).setAuthorsReply(textAreaReply.getText());
//                reviewDao.update(reviews.get(i));
//            }
//        }
    }


    // TODO: Definitely refactor this!
    private void setRating(int r) {
        rating = r;
    }

    private void unhover() {
        hover(rating);
    }

    private void hover(int stars) {
        for (int i = 0; i < buttonRatings.size(); i++) {
            if (i < stars) {
                buttonRatings.get(i).setText(Character.toString('\u2605'));
            }
            else {
                buttonRatings.get(i).setText(Character.toString('\u2606'));
            }
        }
    }

    @FXML private void unhover1() { unhover(); }
    @FXML private void unhover2() { unhover(); }
    @FXML private void unhover3() { unhover(); }
    @FXML private void unhover4() { unhover(); }
    @FXML private void unhover5() { unhover(); }
    @FXML private void unhover6() { unhover(); }
    @FXML private void unhover7() { unhover(); }
    @FXML private void unhover8() { unhover(); }
    @FXML private void unhover9() { unhover(); }
    @FXML private void unhover10() { unhover(); }
    @FXML private void hover1() { hover(1); }
    @FXML private void hover2() { hover(2); }
    @FXML private void hover3() { hover(3); }
    @FXML private void hover4() { hover(4); }
    @FXML private void hover5() { hover(5); }
    @FXML private void hover6() { hover(6); }
    @FXML private void hover7() { hover(7); }
    @FXML private void hover8() { hover(8); }
    @FXML private void hover9() { hover(9); }
    @FXML private void hover10() { hover(10); }

    @FXML
    private void setRating1() {
        setRating(1);
    }

    @FXML
    private void setRating2() {
        setRating(2);
    }

    @FXML
    private void setRating3() {
        setRating(3);
    }

    @FXML
    private void setRating4() {
        setRating(4);
    }

    @FXML
    private void setRating5() {
        setRating(5);
    }

    @FXML
    private void setRating6() {
        setRating(6);
    }

    @FXML
    private void setRating7() {
        setRating(7);
    }

    @FXML
    private void setRating8() {
        setRating(8);
    }

    @FXML
    private void setRating9() {
        setRating(9);
    }

    @FXML
    private void setRating10() {
        setRating(10);
    }


    private List<Button> buttonRatings;
    @FXML private Button buttonRating1;
    @FXML private Button buttonRating2;
    @FXML private Button buttonRating3;
    @FXML private Button buttonRating4;
    @FXML private Button buttonRating5;
    @FXML private Button buttonRating6;
    @FXML private Button buttonRating7;
    @FXML private Button buttonRating8;
    @FXML private Button buttonRating9;
    @FXML private Button buttonRating10;

    @FXML private ProgressBar progressBar;

    @FXML
    private Label labelGameTitle;
    @FXML
    private TextArea textAreaReview;
    @FXML
    private Button buttonAddReview;
    @FXML
    private Button buttonAddReply;
    @FXML
    private Button buttonShowReview;
    @FXML
    private Label labelStatus;
    @FXML
    private Label labelRating;
    @FXML
    private Label labelReview;
    @FXML
    private TextArea textAreaReply;
    @FXML
    private TextArea textAreaID;
    @FXML
    private VBox boxReviews;
}
