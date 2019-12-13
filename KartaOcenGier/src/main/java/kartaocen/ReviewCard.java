// ////////////////////////////////////////////////////////////////// Package //
package kartaocen;

// ////////////////////////////////////////////////////////////////// Imports //
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import java.util.List;

public class ReviewCard {

    // Database
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://remotemysql.com/5VexXpVWzU";

    static final String USER = "5VexXpVWzU";
    static final String PASS = "apQqybLdoW";

    private void writeReviewToDatabase(Review review) throws IOException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Opinions " +
                             "(UserID, GameID, Date, Comment, Opinion, Rate) " +
                             "VALUES (?, ?, ?, ?, ?, ?)")) {

            // Construct insert statement
            statement.setInt(1, review.getUser().id);
            statement.setInt(2, review.getGame().id);
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

    }

    public void render() {
    }

    public void handleEvents() {
    }

    public double getAverageRating(Game game) {
        return 0.0;
    }

    @FXML
    private void addReview() throws IOException, ClassNotFoundException {
        // TODO: Change this to actual User and Game passed from the app
        // Prepare test objects
        User testUser = new User();
        testUser.id = 1;
        Game testGame = new Game();
        testGame.id = 2;

        // Create review
        Review review = new Review(textAreaReview.getText(),
                rating, null, null, testGame, testUser);
        writeReviewToDatabase(review);
    }

    private void editReview(Review review) {
    }

    private void notifyAuthor() {
    }

    private User loggedUser;
    private Game game;
    private List<Review> reviews;
    private int rating;

    // TODO: Definitely refactor this!
    private void setRating(int r) {
        rating = r;
        labelRating.setText(Integer.toString(rating) + "/10");
    }
    @FXML private void setRating1() { setRating(1); }
    @FXML private void setRating2() { setRating(2); }
    @FXML private void setRating3() { setRating(3); }
    @FXML private void setRating4() { setRating(4); }
    @FXML private void setRating5() { setRating(5); }
    @FXML private void setRating6() { setRating(6); }
    @FXML private void setRating7() { setRating(7); }
    @FXML private void setRating8() { setRating(8); }
    @FXML private void setRating9() { setRating(9); }
    @FXML private void setRating10() { setRating(10); }


    @FXML private Label labelGameTitle;
    @FXML private TextArea textAreaReview;
    @FXML private Button buttonAddReview;
    @FXML private Label labelStatus;
    @FXML private Label labelRating;
}
