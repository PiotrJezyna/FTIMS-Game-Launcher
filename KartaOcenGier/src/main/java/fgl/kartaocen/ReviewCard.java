// ////////////////////////////////////////////////////////////////// Package //
package fgl.kartaocen;

// ////////////////////////////////////////////////////////////////// Imports //
// ================================================================ JavaFX == //
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

// ================================================================= Other == //
import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import java.util.List;

// //////////////////////////////////////////////////////// Class: ReviewCard //
public class ReviewCard {

    // ============================================================== Data == //
    private Long loggedUser = 1L;
    private Long game = 2L;
    private Long ID;
    public ReviewDao dao;
    public List<Review> reviews;
    Review review;
    private int rating;

    // ========================================================= Behaviour == //
    public void setGame(Long id) {
        this.game = id;
    }

    public ReviewCard(Game game, User user) throws SQLException {
        dao = new ReviewDao();

        reviews = dao.getAll();
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
        // Create review
        Review review = new Review(ID, textAreaReview.getText(),
                rating, null, null, game, loggedUser);
        test1(review);
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

        for (int i = 1; i < reviews.size(); i++) {
            review = reviews.get(i);
            for (int j = 0; j < 1; j++) {
                boxReviews.getChildren().add(new Label("ID: " + review.getId() + " " + "date: " + new java.sql.Date(Calendar.getInstance().getTime().getTime()) + " UserID: " + review.getUser() + " GameID: " + review.getGame() + " user's rate:  " + review.getRating() + " user's commentt: " + review.getComment()));
            }
        }
    }

    @FXML
    private void test1(Review review) throws SQLException {
        dao.insert(review);
        labelStatus.setText("written correctly");
    }

    @FXML
    private void authorsReply() throws SQLException {
        Long ID = Long.parseLong(textAreaID.getText());

        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getId().equals(ID)) {
                reviews.get(i).setAuthorsReply(textAreaReply.getText());
                dao.update(reviews.get(i));
            }
        }
    }


    // TODO: Definitely refactor this!
    private void setRating(int r) {
        rating = r;
        labelRating.setText(Integer.toString(rating) + "/10");
    }

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
