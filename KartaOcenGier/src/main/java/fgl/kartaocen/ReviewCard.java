// ////////////////////////////////////////////////////////////////// Package //
package fgl.kartaocen;

// ////////////////////////////////////////////////////////////////// Imports //
// ================================================================ JavaFX == //
import fgl.product.Game;
import fgl.product.GameDAO;
import fgl.userPanel.User;
import fgl.userPanel.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

// ================================================================= Other == //
import java.sql.*;
import java.util.*;

// //////////////////////////////////////////////////////// Class: ReviewCard //
public class ReviewCard {

    // ============================================================== Data == //
    private User loggedUser;
    private Game game;

    private int rating = 10;
    private boolean showReviewEditHistory = false;

    static private ReviewDao reviewDao = new ReviewDao();
    static private CommentDao commentDao = new CommentDao();

    private List<Review> currentGameReviews = new ArrayList<>();
    private List<Comment> currentGameComments = new ArrayList<>();

    private Map<Review, List<Comment>> userCommentsPerReview = new HashMap<>();
    private Map<Review, List<Comment>> authorRepliesPerReview = new HashMap<>();

    private ListIterator<Review> currentGameReviewIterator;
    private ListIterator<Comment> currentGamesCurrentCommentIterator;

    private Review getCurrentGameReview() {
        if (currentGameReviewIterator.hasNext()) {
            Review review = currentGameReviewIterator.next();
            currentGameReviewIterator.previous();
            return review;
        }
        else {
            Review review = currentGameReviewIterator.previous();
            currentGameReviewIterator.next();
            return review;
        }
    }

    private Comment getCurrentGamesCurrentComment() {
        if (currentGamesCurrentCommentIterator.hasNext()) {
            Comment comment = currentGamesCurrentCommentIterator.next();
            currentGamesCurrentCommentIterator.previous();
            return comment;
        }
        else {
            Comment comment = currentGamesCurrentCommentIterator.previous();
            currentGamesCurrentCommentIterator.next();
            return comment;
        }
    }

    private boolean doesCurrentGameReviewBelongToLoggedUser() {
        return getCurrentGameReview().getUser().getId().equals(
                loggedUser.getId());
    }

    // ========================================================= Behaviour == //
    private void updateUserInterface() {
        // .................................................................. //
        // Update average rating information
        if (currentGameReviews.size() > 0) {
            labelAverageRating.setText(
                    (int) getAverageRating(game) +
                            " / 10 (based on " +
                            currentGameReviews.size() +
                            " review" +
                            (currentGameReviews.size() > 1 ? "s" : "") +
                            ")"
            );
        } else {
            labelAverageRating.setText("There are no reviews");
        }

        // .................................................................. //
        // Show author's reply only if exists
        setRowVisibility(mainGrid, 4, isUserAuthor() ||
                !authorRepliesPerReview.get(
                        getCurrentGameReview()).isEmpty());

        // .................................................................. //
        // Set review's username information
        if (doesCurrentGameReviewBelongToLoggedUser()) {
            labelReviewUsername.setText("/ Your review");
        } else {
            labelReviewUsername.setText("/ " +
                    getCurrentGameReview().getUser().getUsername() +
                    "'s review");
        }

        // .................................................................. //
        // Enable review's rating buttons if needed
        for (Button button : ratingButtons) {
            button.setDisable(!doesCurrentGameReviewBelongToLoggedUser());
        }

        // .................................................................. //
        // Set appropriate star rating
        for (int i = 0; i < ratingButtons.size(); ++i) {
            if (i < getCurrentGameReview().getRating()) {
                ratingButtons.get(i).setText(Character.toString('\u2605'));
            } else {
                ratingButtons.get(i).setText(Character.toString('\u2606'));
            }
        }

        // .................................................................. //
        // Set comment's content
        textAreaReview.setText(getCurrentGamesCurrentComment().getContent());

        // .................................................................. //
        // Disable comment's content if showing edit history
        textAreaReview.setEditable(!showReviewEditHistory);

        // .................................................................. //
        // Set comment's submission date
        labelReviewDate.setText(
                getCurrentGamesCurrentComment().getSubmissionDate().toString());

        // .................................................................. //
        // If user has more than one comment
        if (editNumber() == 0) {
            buttonShowEditHistory.setVisible(false);
            hboxNavigation.getChildren().get(0).setVisible(false);
            hboxNavigation.getChildren().get(1).setVisible(false);
            labelReviewDate.setVisible(false);
        } else if (editNumber() == 1) {
            buttonShowEditHistory.setVisible(false);
            hboxNavigation.getChildren().get(0).setVisible(false);
            hboxNavigation.getChildren().get(1).setVisible(false);
            labelReviewDate.setVisible(true);
            labelReviewDate.setText(
                    userCommentsPerReview.get(getCurrentUserReview()).get(0).getSubmissionDate().toString());
            textAreaReview.setText(
                    userCommentsPerReview.get(getCurrentUserReview()).get(0).getContent());
        } else {
            buttonShowEditHistory.setVisible(true);
            hboxNavigation.getChildren().get(0).setVisible(true);
            hboxNavigation.getChildren().get(1).setVisible(true);
        }
        writtenComment();
    }

    public void setGame(Long id) {
        try {
            game = (new GameDAO()).get(id);
            init();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isUserAuthor() {
        return loggedUser.getId().equals(game.getUserId());
    }

    private boolean hasUserReviewedBefore() {
        for (Review review : currentGameReviews) {
            if (loggedUser.getId().equals(review.getUser().getId())) {
                return true;
            }
        }
        return false;
    }

    public ReviewCard() {
    }

    private void getCurrentGamesReviews() throws SQLException {
        currentGameReviews.clear();

        List<Review> allReviews = reviewDao.getAll();
        for (Review review : allReviews) {
            if (review.getGame().getId().equals(game.getId())) {
                currentGameReviews.add(review);
            }
        }
    }

    private void getCurrentGamesComments() throws SQLException {
        currentGameComments.clear();

        List<Comment> allComments = commentDao.getAll();
        for (Comment comment : allComments) {
            if (comment.getReview().getGame().getId().equals(game.getId())) {
                currentGameComments.add(comment);
            }
        }
    }

    private void organizeUserCommentsAndAuthorsRepliesPerReview() {
        for (Review review : currentGameReviews) {
            userCommentsPerReview.put(review, new ArrayList<>());
            authorRepliesPerReview.put(review, new ArrayList<>());

            for (Comment comment : currentGameComments) {
                if (!review.getId().equals(comment.getReview().getId())) {
                    continue;
                }

                if (comment.isReply()) {
                    authorRepliesPerReview.get(review).add(comment);
                } else {
                    userCommentsPerReview.get(review).add(comment);
                }
            }
        }
    }


    //    @FXML
    public void init() throws SQLException {
        //--------------------------------------------------
        // todo: remove this
        loggedUser = (new UserDAO()).get(8L);
        game = (new GameDAO()).get(9L);

        // .................................................................. //
        getCurrentGamesReviews();
        getCurrentGamesComments();
        organizeUserCommentsAndAuthorsRepliesPerReview();

        currentGameReviewIterator = currentGameReviews.listIterator();
        currentGamesCurrentCommentIterator = userCommentsPerReview.get(
                getCurrentGameReview()).listIterator(
                userCommentsPerReview.get(
                        getCurrentGameReview()).size());

        //--------------------------------------------------
        // Set label
        labelGameTitle.setText(game.getTitle());

        // Create button reference
        ratingButtons = new ArrayList<>();
        ratingButtons.add(buttonRating1);
        ratingButtons.add(buttonRating2);
        ratingButtons.add(buttonRating3);
        ratingButtons.add(buttonRating4);
        ratingButtons.add(buttonRating5);
        ratingButtons.add(buttonRating6);
        ratingButtons.add(buttonRating7);
        ratingButtons.add(buttonRating8);
        ratingButtons.add(buttonRating9);
        ratingButtons.add(buttonRating10);

//        progressBar.setVisible(false);


        // .................................................................. //
        if (isUserAuthor()) {
            //todo
        } else {
            if (hasUserReviewedBefore()) {
                Review userReview = null;
                for (Review review : currentGameReviews) {
                    if (loggedUser.getId().equals(review.getUser().getId())) {
                        userReview = review;
                        rating = userReview.getRating();
                        unhover();
                    }
                }

                if (userCommentsPerReview.get(getCurrentUserReview()).size() > 1) {
                    buttonShowEditHistory.setVisible(true);
                } else {
                    buttonShowEditHistory.setVisible(false);
                    hboxNavigation.getChildren().get(0).setVisible(false);
                    hboxNavigation.getChildren().get(1).setVisible(false);
                }

            } else {
                labelReviewDate.setText("");
                buttonShowEditHistory.setVisible(false);
                hboxNavigation.getChildren().get(0).setVisible(false);
                hboxNavigation.getChildren().get(1).setVisible(false);
//                authorsReplyConstraints.setPrefHeight(0.0);
//                deleteRow(mainGrid, 4);
            }
        }

        updateUserInterface();
    }

    static private void setRowVisibility(GridPane grid, int row,
                                         boolean value) {
        Set<Node> nodes = new HashSet<>();

        for (Node child : grid.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(child);

            if (((rowIndex == null) ? 0 : rowIndex) == row) {
                child.setVisible(value);
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

    public double getAverageRating(Game game) {
        int sumOfRatings = 0;
        for (Review review : currentGameReviews) {
            sumOfRatings += review.getRating();
        }
        return ((double) sumOfRatings) / currentGameReviews.size();
    }

    private int editNumber() {
        if (getCurrentUserReview() != null) {
            return userCommentsPerReview.get(getCurrentUserReview()).size();
        }
        return 0;
    }

    @FXML
    private void addReview() throws SQLException {
        Review review = new Review(0L, game, loggedUser, rating);
        Comment comment = new Comment(0L, review, textAreaReview.getText(), null, false);

        if (editNumber() == 0) {
            reviewDao.insert(review);
            currentGameReviews.add(review);
        } else {
            review.setId(getCurrentUserReview().getId());
            reviewDao.update(review);
            getCurrentUserReview().setRating(review.getRating());
        }

//        if (getCurrentUserReview() == null
//                || !userCommentsPerReview.get(getCurrentUserReview())
//                .get(0).getContent().equals(textAreaReview.getText())) {
        commentDao.insert(comment);
        currentGameComments.add(comment);

        labelReviewDate.setVisible(true);
        labelReviewDate.setText(comment.getSubmissionDate().toString());
//        }

        organizeUserCommentsAndAuthorsRepliesPerReview();

//        labelStatus.setText("written correctly");


        updateUserInterface();
    }

    @FXML
    private void writtenComment() {
//        int editNumber = userCommentsPerReview.get(getCurrentUserReview()).size();
//
//        if (editNumber == 0) {
//            buttonAddReview.setDisable(false);
//        }
//        else {
//            String test = textAreaReview.getText();
//            if (userCommentsPerReview.get(getCurrentUserReview())
//                    .get(0).getContent().equals(textAreaReview.getText())) {
//                buttonAddReview.setDisable(true);
//            }
//            else {
//                buttonAddReview.setDisable(false);
//            }
//        }
    }


    private Review getCurrentUserReview() {
        for (Review review : currentGameReviews) {
            if (loggedUser.getId().equals(review.getUser().getId())) {
                return review;
            }
        }
        return null;
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
        for (int i = 0; i < ratingButtons.size(); i++) {
            if (i < stars) {
                ratingButtons.get(i).setText(Character.toString('\u2605'));
            } else {
                ratingButtons.get(i).setText(Character.toString('\u2606'));
            }
        }
    }


    @FXML
    private void showEditHistory() {
        showReviewEditHistory = !showReviewEditHistory;
        if (showReviewEditHistory) {
            currentGamesCurrentCommentIterator = userCommentsPerReview.get(getCurrentUserReview()).listIterator(
                    userCommentsPerReview.get(getCurrentUserReview()).size()
            );

            buttonShowEditHistory.setText("Hide edit history");
            hboxNavigation.getChildren().get(0).setVisible(true);
            hboxNavigation.getChildren().get(1).setVisible(true);
//            textAreaReview.setEditable(false);
            buttonAddReview.setVisible(false);
        } else {
            buttonShowEditHistory.setText("Show edit history");
            hboxNavigation.getChildren().get(0).setVisible(false);
            hboxNavigation.getChildren().get(1).setVisible(false);
//            textAreaReview.setEditable(true);
            buttonAddReview.setVisible(true);
        }
    }

    @FXML
    private void previousEdit() {
        if (currentGamesCurrentCommentIterator.hasPrevious()) {
            buttonPrevious.setDisable(false);
            Comment newComment = currentGamesCurrentCommentIterator.previous();
            textAreaReview.setText(newComment.getContent());
        } else {
            buttonPrevious.setDisable(true);
            buttonNext.setDisable(false);
        }
        String breakpoint = "aa";
    }

    @FXML
    private void nextEdit() {
        if (currentGamesCurrentCommentIterator.hasNext()) {
            buttonNext.setDisable(false);
            Comment newComment = currentGamesCurrentCommentIterator.next();
            textAreaReview.setText(newComment.getContent());
        } else {
            buttonNext.setDisable(true);
            buttonPrevious.setDisable(false);
        }
        String breakpoint = "aa";
    }


    @FXML
    private void unhover1() {
        unhover();
    }

    @FXML
    private void unhover2() {
        unhover();
    }

    @FXML
    private void unhover3() {
        unhover();
    }

    @FXML
    private void unhover4() {
        unhover();
    }

    @FXML
    private void unhover5() {
        unhover();
    }

    @FXML
    private void unhover6() {
        unhover();
    }

    @FXML
    private void unhover7() {
        unhover();
    }

    @FXML
    private void unhover8() {
        unhover();
    }

    @FXML
    private void unhover9() {
        unhover();
    }

    @FXML
    private void unhover10() {
        unhover();
    }

    @FXML
    private void hover1() {
        hover(1);
    }

    @FXML
    private void hover2() {
        hover(2);
    }

    @FXML
    private void hover3() {
        hover(3);
    }

    @FXML
    private void hover4() {
        hover(4);
    }

    @FXML
    private void hover5() {
        hover(5);
    }

    @FXML
    private void hover6() {
        hover(6);
    }

    @FXML
    private void hover7() {
        hover(7);
    }

    @FXML
    private void hover8() {
        hover(8);
    }

    @FXML
    private void hover9() {
        hover(9);
    }

    @FXML
    private void hover10() {
        hover(10);
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


    @FXML private Label labelAverageRating;
    @FXML private Label labelGameTitle;
    @FXML private Label labelReviewUsername;
    @FXML private Label labelReviewDate;

    @FXML private TextArea textAreaReview;

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
    private List<Button> ratingButtons;

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
    @FXML
    private Button buttonShowEditHistory;
    @FXML
    private Button buttonPrevious;
    @FXML
    private Button buttonNext;
    @FXML
    private HBox hboxNavigation;
    @FXML
    private GridPane mainGrid;
    @FXML
    private RowConstraints rowConstraintsAuthorsReply;
}
